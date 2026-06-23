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
import java.lang.classfile.ClassFile;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Class loader that is using {@link JvmInsightTransform} to
 * patch bytecode to be {@link JvmInsight}-ready.
 */
final class JvmInsightLoader extends URLClassLoader {
    private final ClassFile clazzFile;

    JvmInsightLoader(ClassLoader l, URL... u) {
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
            var newArr = slashName.startsWith("org/apidesign/graalvm/insight/JvmInsight") ?
                    arr: patch(arr);
            /* Enable and inspect with javap -c -private * /
            try (
                var os = new java.io.FileOutputStream(new java.io.File("/tmp/Clazz.class"))
            ) {
                os.write(newArr);
            }
            /* */
            return defineClass(name, newArr, 0, newArr.length);
        } catch (IOException ex) {
            throw new ClassNotFoundException(name, ex);
        }
    }

    private byte[] patch(byte[] arr) {
        var model = clazzFile.parse(arr);
        return clazzFile.transformClass(model, new JvmInsightTransform(model));
    }

}
