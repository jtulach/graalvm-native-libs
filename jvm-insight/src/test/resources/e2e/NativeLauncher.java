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
import java.util.Arrays;

import org.apidesign.graalvm.insight.JvmInsight;

// $ javac -cp ${jvminsight} NativeLauncher.java -g -d ${classes}
// $ native-image -cp ${jvminsight}:${classes} \
//    -H:+RuntimeClassLoading -H:+AllowJRTFileSystem \
//    -H:Preserve=package=java.lang \
//    -H:Preserve=package=jdk.internal.misc \
//    -H:Preserve=package=jdk.internal.access \
//    -H:Preserve=package=java.lang.invoke \
//    -H:Preserve=package=org.apidesign.graalvm.insight \
//    -H:Preserve=package=java.util \
//    -o ${classes}/native-launcher e2e.NativeLauncher
// > *
// $ javac Fib.java -g -d ${classes}
// $ ${classes}/native-launcher ${classes} Fib
// > fib(5) = 8
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=5}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=4}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=3}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=2}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=1}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=0}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=1}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=2}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=1}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=0}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=3}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=2}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=1}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=0}
// 2> [Crema+JvmInsight]: method -1:LFib;.fib(I)I with: {n=1}
// $ exit

public final class NativeLauncher {
    public static void main(String... args) throws Exception {
        var cp = new File(args[0]).toURI().toURL();
        var loader = JvmInsight.createLoader(JvmInsight.class.getClassLoader(), cp);
        var clazz = loader.loadClass(args[1]);
        var method = clazz.getMethod("main", String[].class);
        method.setAccessible(true);
        var remaining = Arrays.copyOfRange(args, 2, args.length);
        try (var handle = JvmInsight.apply((insight) -> {
            insight.on(clazz).roots().call((name, localVars) -> {
                if (localVars.containsKey("n")) {
                    System.err.println("[Crema+JvmInsight]: method " + name + " with: " + localVars);
                }
            });
        })) {
            method.invoke(null, (Object) remaining);
        }
    }
}
