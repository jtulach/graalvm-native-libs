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
package e2e;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

// $ javac -cp ${jvminsight} NativeLauncher.java -g -d ${classes}
// $ native-image -cp ${classes} \
//    -H:+RuntimeClassLoading -H:+AllowJRTFileSystem \
//    -H:Preserve=package=java.lang \
//    -H:Preserve=package=jdk.internal.misc \
//    -H:Preserve=package=jdk.internal.access \
//    -H:Preserve=package=java.lang.invoke \
//    -o ${classes}/native-launcher e2e.NativeLauncher
// > *
// $ javac Fib.java -d ${classes}
// $ ${classes}/native-launcher ${classes} Fib
// > fib(5) = 8
// $ exit

public final class NativeLauncher {
    public static void main(String... args) throws Exception {
        var cp = new File(args[0]).toURI().toURL();
        var loader = new URLClassLoader(new URL[] { cp });
        var clazz = loader.loadClass(args[1]);
        var method = clazz.getMethod("main", String[].class);
        method.setAccessible(true);
        var remaining = Arrays.copyOfRange(args, 2, args.length);
        method.invoke(null, (Object) remaining);
    }
}
