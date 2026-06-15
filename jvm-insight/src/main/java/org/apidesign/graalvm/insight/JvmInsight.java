/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apidesign.graalvm.insight;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.classfile.AccessFlags;
import java.lang.classfile.AttributeMapper;
import java.lang.classfile.Attributes;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.CodeModel;
import java.lang.classfile.CodeTransform;
import java.lang.classfile.FieldModel;
import java.lang.classfile.Label;
import java.lang.classfile.MethodElement;
import java.lang.classfile.MethodModel;
import java.lang.classfile.MethodTransform;
import java.lang.classfile.attribute.LocalVariableTableAttribute;
import java.lang.classfile.instruction.LocalVariable;
import java.lang.classfile.instruction.StoreInstruction;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public final class JvmInsight extends URLClassLoader {
    private final ClassFile clazzFile;

    JvmInsight(ClassLoader l, URL... u) {
        super(u, l);
        this.clazzFile = ClassFile.of();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        var slashName = name.replace('.', '/') + ".class";
        var is = getResourceAsStream(slashName);
        if (is == null) {
            throw new ClassNotFoundException(name);
        }
        try {
            var arr = is.readAllBytes();
            var newArr = patch(arr);
            try (
                var os = new FileOutputStream(new File("/tmp/Clazz.class"))
            ) {
                os.write(newArr);
            }
            return defineClass(name, newArr, 0, newArr.length);
        } catch (IOException ex) {
            throw new ClassNotFoundException(name, ex);
        }
    }


    private byte[] patch(byte[] arr) {
        var model = clazzFile.parse(arr);
        return clazzFile.transformClass(model, new Samsa(model));
    }

    private static final class Samsa implements ClassTransform {
        private ClassModel model;
        private boolean field;
        private final ClassDesc callbackClass;

        public Samsa(ClassModel clazz) {
            this.model = clazz;
            this.callbackClass = ClassDesc.of("java.util.function.BiConsumer");
        }

        @Override
        public void accept(ClassBuilder builder, ClassElement element) {
            if (!field) {
                field = true;
                builder.withField("TRACE", callbackClass, AccessFlag.PUBLIC.mask() | AccessFlag.STATIC.mask());
            }

            if (element instanceof MethodModel method) {
                builder.transformMethod(method, (mb, me) -> {
                    System.err.println("me: " + me.getClass());
                    if (me instanceof CodeModel code) {
                        mb.withCode((cb) -> {
                            var count = method.methodTypeSymbol().parameterCount();
                            if (!method.flags().flags().contains(AccessFlag.STATIC)) {
                                count++;
                            }
                            var enterGenerated = false;
                            var localTypes = new HashMap<Integer, LocalVariable>();
                            var locals = new HashMap<Integer, LocalVariable>();
                            for (var instr : code.elementList()) {
                                cb.with(instr);
                                System.err.println("  instr: " + instr);
                                if (instr instanceof LocalVariable localVar) {
                                    localTypes.put(localVar.slot(), localVar);
                                    if (!localVar.name().equalsString("this")
                                    ) {
                                        if (localVar.slot() < count) {
                                            locals.put(localVar.slot(), localVar);
                                        }
                                    }
                                    System.err.println("     defined var until: " + localVar.endScope());
                                }
                                if (instr instanceof StoreInstruction store) {
                                    var initializedVar = localTypes.get(store.slot());
                                    locals.put(store.slot(), initializedVar);
                                }
                                if (instr instanceof Label label) {
                                    System.err.println("     label: " + label);
                                    if (!enterGenerated) {
                                        enterMethod(method.methodName().stringValue(), locals.values(), cb);
                                        enterGenerated = true;
                                    }
                                    var it = localTypes.entrySet().iterator();
                                    while (it.hasNext()) {
                                        var en = it.next();
                                        if (en.getValue().endScope() == label) {
                                            it.remove();
                                            locals.remove(en.getKey());
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        mb.with(me);
                    }
                });
            } else {
                builder.with(element);
            }
        }

        private void enterMethod(String methodName, Collection<LocalVariable> locals, CodeBuilder cb) {
            cb.getstatic(model.thisClass().asSymbol(), "TRACE", callbackClass);
            var noCallback = cb.newLabel();
            cb.ifnull(noCallback);
            cb.getstatic(model.thisClass().asSymbol(), "TRACE", callbackClass);
            cb.loadConstant(methodName);
            for (var l : locals) {
                cb.loadConstant(l.name().stringValue());
                loadObjectWraper(cb, l);
            }
            var Map = ClassDesc.of("java.util.Map");
            var ofArgsType = MethodTypeDesc.of(Map, Collections.nCopies(locals.size() * 2, ConstantDescs.CD_Object));
            cb.invokestatic(Map, "of", ofArgsType, true);
            var consumeType = MethodTypeDesc.of(ConstantDescs.CD_void, ConstantDescs.CD_Object, ConstantDescs.CD_Object);
            cb.invokeinterface(callbackClass, "accept", consumeType);
            cb.labelBinding(noCallback);
        }

        private void loadObjectWraper(CodeBuilder cb, LocalVariable l) {
            System.err.println("variable name " + l.name() + " at: " + l.slot() + " type: " + l.typeSymbol() + " p: " + l.typeSymbol().isPrimitive());
            if (l.typeSymbol().isPrimitive()) {
                cb.iload(l.slot());
                System.err.println("converting primitive type: " + l.typeSymbol());
                var type = MethodTypeDesc.of(ConstantDescs.CD_Integer, ConstantDescs.CD_int);
                cb.invokestatic(ConstantDescs.CD_Integer, "valueOf", type);
            } else {
                cb.aload(l.slot());
            }
        }

    }
}
