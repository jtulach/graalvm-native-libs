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

import java.io.IOException;
import java.lang.classfile.AccessFlags;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeModel;
import java.lang.classfile.CodeTransform;
import java.lang.classfile.FieldModel;
import java.lang.classfile.MethodElement;
import java.lang.classfile.MethodModel;
import java.lang.classfile.MethodTransform;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.net.URL;
import java.net.URLClassLoader;

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
                    if (me instanceof CodeModel code) {
                        mb.withCode((cb) -> {
                            cb.getstatic(model.thisClass().asSymbol(), "TRACE", callbackClass);
                            var noCallback = cb.newLabel();
                            cb.ifnull(noCallback);
                            cb.getstatic(model.thisClass().asSymbol(), "TRACE", callbackClass);
                            cb.loadConstant(method.methodName().stringValue());
                            cb.aconst_null();
                            var consumeType = MethodTypeDesc.of(ConstantDescs.CD_void, ConstantDescs.CD_Object, ConstantDescs.CD_Object);
                            cb.invokeinterface(callbackClass, "accept", consumeType);
                            cb.labelBinding(noCallback);
                            for (var instr : code.elementList()) {
                                cb.with(instr);
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

    }
}
