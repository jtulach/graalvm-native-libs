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
package org.apidesign.demo.jvminterop;

import java.io.File;
import org.apidesign.jvm.channel.JVM;
import org.apidesign.jvm.interop.OtherJvmClassLoader;

public final class FactorialViaInterop {
    private FactorialViaInterop() {}

    public static void main(String[] args) throws Exception {
        assumeOrExit(9, "Provide one numeric argument!", args.length == 1);
        var javaHome = System.getenv("JAVA_HOME");
        assumeOrExit(1, "The environment variable JAVA_HOME must be defined", javaHome != null);
        var javaDir = new File(javaHome);
        assumeOrExit(2, "JAVA_HOME variable must point to a JDK directory, but was " + javaDir, javaDir.isDirectory());

        var jvm = JVM.create(javaDir,
            "-Djava.class.path=" + cp("target/classes", "target/dependency"),
            "-Dpolyglotimpl.DisableMultiReleaseCheck=true" // Truffle classes are unpacked
        );
        var loader = OtherJvmClassLoader.create(jvm);
        var FactorialImpl = loader.loadClass("org.apidesign.demo.jvminterop.FactorialImpl");
        log("Loaded %s class from HotSpot JVM\n", FactorialImpl);
        var n = Long.parseLong(args[0]);
        log("Invoking %s.fac(%d) method in the HotSpot JVM\n", FactorialImpl, n);
        var res = FactorialImpl.invokeMember("fac", n);
        log("Result is back in native code: %s\n", res);
        log("fac(%d) = %d\n", n, res.asBigInteger());
    }

    private static String cp(String... elems) {
        var sb = new StringBuilder();
        for (var e : elems) {
            var withCorrectSlash = e.replace('/', File.separatorChar);
            if (sb.length() > 0) {
                sb.append(File.pathSeparator);
            }
            sb.append(withCorrectSlash);
        }
        return sb.toString();
    }

    private static void log(String fmt, Object... args) {
        var vm = System.getProperty("java.vm.name");
        System.err.printf("{" + vm + "} " + fmt, args);
    }

    private static void assumeOrExit(int exitCode, String msg, boolean check) {
        if (!check) {
            System.err.println(msg);
            System.exit(exitCode);
        }
    }
}
