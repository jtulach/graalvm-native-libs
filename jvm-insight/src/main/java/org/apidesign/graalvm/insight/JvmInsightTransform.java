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

import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.CodeModel;
import java.lang.classfile.Label;
import java.lang.classfile.MethodModel;
import java.lang.classfile.TypeKind;
import java.lang.classfile.attribute.LocalVariableInfo;
import java.lang.classfile.constantpool.ClassEntry;
import java.lang.classfile.instruction.LineNumber;
import java.lang.classfile.instruction.LocalVariable;
import java.lang.classfile.instruction.StoreInstruction;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/** Transformer patching byte code to be {@link JvmInsight}-ready.
 */
final class JvmInsightTransform implements ClassTransform {
    private final ClassModel model;
    private final ClassDesc callbackClass;

    public JvmInsightTransform(ClassModel clazz) {
        this.model = clazz;
        this.callbackClass = ClassDesc.of("java.util.function.BiConsumer");
    }

    @Override
    public void accept(ClassBuilder builder, ClassElement element) {
        if (element instanceof MethodModel method) {
            builder.transformMethod(method, (mb, me) -> {
                if (me instanceof CodeModel code) {
                    mb.withCode(cb -> {
                        var count = method.methodTypeSymbol().parameterCount();
                        if (!method.flags().flags().contains(AccessFlag.STATIC)) {
                            count++;
                        }
                        var enterGenerated = false;
                        var localTypes = new HashMap<Integer, LocalVariableInfo>();
                        var locals = new HashMap<Integer, LocalVariableInfo>();
                        for (var instr : code.elementList()) {
                            cb.with(instr);
                            // System.err.println("  instr: " + instr);
                            if (instr instanceof LocalVariableInfo localVar) {
                                if (localVar.typeSymbol() == ConstantDescs.CD_long || localVar.typeSymbol() == ConstantDescs.CD_double) {
                                    // long and double vars occupy two slots
                                    count++;
                                }
                                localTypes.put(localVar.slot(), localVar);
                                if (!localVar.name().equalsString("this")) {
                                    if (localVar.slot() < count) {
                                        locals.put(localVar.slot(), localVar);
                                    }
                                }
                            }
                            if (instr instanceof StoreInstruction store) {
                                var initializedVar = localTypes.get(store.slot());
                                if (initializedVar != null) {
                                    // initializedVar can be null when there is no debug info
                                    locals.put(store.slot(), initializedVar);
                                }
                            }
                            if (instr instanceof Label label) {
                                if (!enterGenerated) {
                                    onEnter("ROOTS", method, -1, locals.values(), cb);
                                    enterGenerated = true;
                                }
                                var it = localTypes.entrySet().iterator();
                                while (it.hasNext()) {
                                    var en = it.next();
                                    if (
                                        en.getValue() instanceof LocalVariable localVar
                                        && localVar.endScope() == label
                                    ) {
                                        it.remove();
                                        locals.remove(en.getKey());
                                    }
                                }
                            }
                            if (instr instanceof LineNumber line) {
                                onEnter("STATEMENTS", method, line.line(), locals.values(), cb);
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

    private void onEnter(String fieldName, MethodModel method, int line, Collection<LocalVariableInfo> locals, CodeBuilder cb) {
        var insightClazz = ClassDesc.of(JvmInsight.class.getName());
        var boot = ConstantDescs.ofCallsiteBootstrap(insightClazz, "metafactory", ConstantDescs.CD_CallSite);
        var ref = DynamicCallSiteDesc.of(boot, fieldName, MethodTypeDesc.of(callbackClass));
        cb.invokedynamic(ref);
        var noCallback = cb.newLabel();
        cb.ifnull(noCallback);
        cb.invokedynamic(ref);
        cb.loadConstant(fqn(model.thisClass(), method, line));
        var argumentCount = 0;
        for (var l : locals) {
            cb.loadConstant(l.name().stringValue());
            loadObjectWraper(cb, l);
            argumentCount += 2;
        }
        var Map = ClassDesc.of("java.util.Map");
        var ofArgsType = MethodTypeDesc.of(Map, Collections.nCopies(argumentCount, ConstantDescs.CD_Object));
        cb.invokestatic(Map, "of", ofArgsType, true);
        var consumeType = MethodTypeDesc.of(ConstantDescs.CD_void, ConstantDescs.CD_Object, ConstantDescs.CD_Object);
        cb.invokeinterface(callbackClass, "accept", consumeType);
        cb.labelBinding(noCallback);
    }

    private void loadObjectWraper(CodeBuilder cb, LocalVariableInfo l) {
        var kind = TypeKind.fromDescriptor(l.typeSymbol().descriptorString());
        cb.loadLocal(kind, l.slot());
        switch (kind) {
            case BOOLEAN -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Boolean, ConstantDescs.CD_boolean);
                cb.invokestatic(ConstantDescs.CD_Boolean, "valueOf", type);
            }
            case BYTE -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Byte, ConstantDescs.CD_byte);
                cb.invokestatic(ConstantDescs.CD_Byte, "valueOf", type);
            }
            case CHAR -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Character, ConstantDescs.CD_char);
                cb.invokestatic(ConstantDescs.CD_Character, "valueOf", type);
            }
            case SHORT -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Short, ConstantDescs.CD_short);
                cb.invokestatic(ConstantDescs.CD_Short, "valueOf", type);
            }
            case INT -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Integer, ConstantDescs.CD_int);
                cb.invokestatic(ConstantDescs.CD_Integer, "valueOf", type);
            }
            case LONG -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Long, ConstantDescs.CD_long);
                cb.invokestatic(ConstantDescs.CD_Long, "valueOf", type);
            }
            case FLOAT -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Float, ConstantDescs.CD_float);
                cb.invokestatic(ConstantDescs.CD_Float, "valueOf", type);
            }
            case DOUBLE -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Double, ConstantDescs.CD_double);
                cb.invokestatic(ConstantDescs.CD_Double, "valueOf", type);
            }
            case REFERENCE -> {
                // no conversion
            }
            default -> throw new IllegalStateException("Unknown descriptor: " + l.typeSymbol().descriptorString());
        }
    }

    private ConstantDesc fqn(ClassEntry clazz, MethodModel method, int line) {
        var methodName = "L" + clazz.asInternalName() + ";." + method.methodName() + method.methodTypeSymbol().descriptorString();
        return "" + line + ":" + methodName;
    }
}
