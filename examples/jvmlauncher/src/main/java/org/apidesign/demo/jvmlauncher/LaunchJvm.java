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
package org.apidesign.demo.jvmlauncher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import org.apidesign.jvm.channel.JVM;

public final class LaunchJvm {
    public static void main(String[] args) {
        var argList = new ArrayList<>(Arrays.asList(args));
        var argJvmAt = argList.indexOf("--jvm");
        if (argJvmAt >= 0) {
            argList.remove(argJvmAt);
            var filteredArgs = argList.toArray(new String[0]);

            var javaHome = System.getenv("JAVA_HOME");
            assumeOrExit(1, "The environment variable JAVA_HOME must be defined", javaHome != null);
            var javaDir = new File(javaHome);
            assumeOrExit(2, "JAVA_HOME variable must point to a JDK directory, but was " + javaDir, javaDir.isDirectory());

            var jvm = JVM.create(javaDir, "-Djava.class.path=target/classes");
            jvm.executeMain("org/apidesign/demo/jvmlauncher/LaunchJvm", filteredArgs);
            return;
        }
        System.err.println("Running in: " + System.getProperty("java.vm.name"));
    }

    private static void assumeOrExit(int exitCode, String msg, boolean check) {
        if (!check) {
            System.err.println(msg);
            System.exit(exitCode);
        }
    }
}
