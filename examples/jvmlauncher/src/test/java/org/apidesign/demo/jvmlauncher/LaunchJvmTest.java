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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LaunchJvmTest {
    private static File prog;

    @BeforeAll
    public static void findJvmLauncherExecutable() throws Exception {
        var loc = LaunchJvmTest.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        var dir = new File(loc);
        while (dir != null) {
            var win = new File(dir, "demo-jvmlauncher.exe");
            if (win.canExecute() && !win.isDirectory()) {
                prog = win;
                break;
            }
            var unix = new File(dir, "demo-jvmlauncher");
            if (unix.canExecute() && !unix.isDirectory()) {
                prog = unix;
                break;
            }
            dir = dir.getParentFile();
        }

        assertTrue(prog != null && prog.canExecute(), "Cannot find demo-jvmlauncher executable searching from " + loc);
    }

    @Test
    public void executeNativeExe() throws Exception {
        var output = executeProg(prog.getAbsolutePath());
        assertEquals("Running in: Substrate VM", output);
    }

    @Test
    public void executeNativeExeWithJvm() throws Exception {
        var output = executeProg(prog.getAbsolutePath(), "--jvm");
        var openJDK = output.contains("OpenJDK"); // OpenJDK 64-Bit Server VM
        var hotSpot = output.contains("HotSpot"); // Java HotSpot(TM) 64-Bit Server VM
        assertTrue(openJDK || hotSpot, "Expecting HotSpot VM, but was: " + output);
    }

    private static String executeProg(String... args) throws IOException, InterruptedException {
        var pb = new ProcessBuilder(args);
        pb.environment().put("JAVA_HOME", System.getProperty("java.home"));
        var proc = pb.start();
        assertEquals(0, proc.waitFor(), "Executing " + prog + " should succeed");
        var output = new BufferedReader(new InputStreamReader(proc.getErrorStream())).readLine();
        return output;
    }

}
