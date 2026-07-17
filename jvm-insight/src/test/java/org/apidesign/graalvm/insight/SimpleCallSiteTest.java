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

import org.apidesign.graalvm.insight.samples.Factorial;
import java.io.IOException;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeModel;
import java.lang.classfile.MethodModel;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodTypeDesc;
import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

public class SimpleCallSiteTest {
    /**
     * This is the {@link Factorial} class loaded by different classloader. That
     * classloader patches the bytecode of the loaded classes to be
     * {@link JvmInsight} capable. As the class is loaded by different
     * classloader that this testing class, we have to access it via reflection.
     */
    private static Class<?> FactorialHosted;

    @BeforeAll
    public static void initFactorialHosted() throws Exception {
        var cp = Factorial.class.getProtectionDomain().getCodeSource().getLocation();
        var loader = new CallSiteLoader(new AvoidClassLoader(Factorial.class.getClassLoader()), cp);
        FactorialHosted = loader.loadClass(Factorial.class.getName());
        assertNotEquals(Factorial.class, FactorialHosted, "Factorial shall be masked from this loader");
        assertNotNull(FactorialHosted, "Factorial class is loaded");
    }

    private static final class CallSiteLoader extends URLClassLoader {
        CallSiteLoader(ClassLoader parent, URL... urls) {
            super(urls, parent);
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
            var clazzFile = ClassFile.of();
            var model = clazzFile.parse(arr);
            return clazzFile.transformClass(model, new ClassTransform() {
                @Override
                public void accept(ClassBuilder builder, ClassElement element) {
                    if (element instanceof MethodModel method && method.methodName().equalsString("callsite")) {
                        builder.transformMethod(method, (mb, me) -> {
                            if (me instanceof CodeModel code) {
                                mb.withCode(cb -> {
                                    var clazz = model.thisClass().asSymbol();
                                    var boot = ConstantDescs.ofCallsiteBootstrap(clazz, "meaningBootstrap", ConstantDescs.CD_CallSite);
                                    var ref = DynamicCallSiteDesc.of(boot, MethodTypeDesc.of(ConstantDescs.CD_int));
                                    cb.invokedynamic(ref);
                                    cb.ireturn();
                                });
                            } else {
                                mb.with(me);
                            }
                        });
                    } else {
                        builder.with(element);
                    }
                }
            });
        }
    }

    @Test
    public void testCallsite() throws Exception {
        var exp = -1;
        for (int i = 0; i <= 1_000_000; i++) {
            var number = (Number) FactorialHosted.getMethod("callsite").invoke(null);
            assertEquals(exp, number.intValue(), "Round #" + i);
            if (i == 999_000) {
                exp = 42;
                // switch to call the meaning() method
                FactorialHosted.getMethod("enableDynamicMeaning").invoke(null);
            }
        }
        var countMeaning = (Number) FactorialHosted.getField("countMeaning").get(null);
        assertEquals(1000, countMeaning, "Thousand calls into meaning() method");
    }

}
