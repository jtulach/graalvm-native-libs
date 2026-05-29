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
import java.util.List;
import org.graalvm.nativeimage.ImageInfo;
import org.apidesign.jvm.channel.JVM;

public final class LaunchJvm {
    private static final boolean AOT = ImageInfo.inImageRuntimeCode();

    public static void main(String[] args) {
        if (AOT && List.of(args).contains("--jvm")) {
            var javaHome = new File(System.getenv("JAVA_HOME"));
            var jvm = JVM.create(javaHome, "-Djava.class.path=target/classes");
            jvm.executeMain("org/apidesign/demo/jvmlauncher/LaunchJvm", args);
            return;
        }
        System.err.println("Running in: " + System.getProperty("java.vm.name"));
    }
}
