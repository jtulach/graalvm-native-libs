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
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeModel;
import java.lang.classfile.CodeTransform;
import java.lang.classfile.MethodElement;
import java.lang.classfile.MethodModel;
import java.lang.classfile.MethodTransform;
import java.lang.constant.ConstantDescs;
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
        return clazzFile.transformClass(model, new Samsa());
    }

    private static final class Samsa implements ClassTransform {
        @Override
        public void accept(ClassBuilder builder, ClassElement element) {
            if (element instanceof MethodModel method) {
                builder.transformMethod(method, (mb, me) -> {
                    System.err.println("rewriting " + method.methodName() + " " + me);
                    mb.with(me);
                });
            } else {
                builder.with(element);
            }
        }

    }
}
