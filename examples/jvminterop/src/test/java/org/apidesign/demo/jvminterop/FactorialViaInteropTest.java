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
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FactorialViaInteropTest {
    private static File prog;

    @BeforeAll
    public static void findExecutable() throws Exception {
        var loc = FactorialViaInteropTest.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        var dir = new File(loc);
        while (dir != null) {
            var win = new File(dir, "demo-jvminterop.exe");
            if (win.canExecute() && !win.isDirectory()) {
                prog = win;
                break;
            }
            var unix = new File(dir, "demo-jvminterop");
            if (unix.canExecute() && !unix.isDirectory()) {
                prog = unix;
                break;
            }
            dir = dir.getParentFile();
        }

        assertTrue(prog != null && prog.canExecute(), "Cannot find demo-jvminterop executable searching from " + loc);
    }

    @Test
    public void factorialFive() throws Exception {
        var output = executeProg(prog.getAbsolutePath(), "5");
        var logOutputLines = output.lines().filter(l -> l.startsWith("{")).toList();
        assertEquals(6, logOutputLines.size(), "Six lines: " + logOutputLines);
        LogFrom.SVM.assertLog("Loaded org.apidesign.demo.jvminterop.FactorialImpl class from HotSpot JVM", logOutputLines.get(0));
        LogFrom.SVM.assertLog("Invoking org.apidesign.demo.jvminterop.FactorialImpl.fac(5) method in the HotSpot JVM", logOutputLines.get(1));
        LogFrom.HOTSPOT.assertLog("Computing fac(5)", logOutputLines.get(2));
        LogFrom.HOTSPOT.assertLog("Result of fac(5) is 120", logOutputLines.get(3));
        LogFrom.SVM.assertLog("Result is back in native code: 120", logOutputLines.get(4));
        LogFrom.SVM.assertLog("fac(5) = 120", logOutputLines.get(5));
    }

    private static String executeProg(String... args) throws IOException, InterruptedException {
        var pb = new ProcessBuilder(args);
        pb.environment().put("JAVA_HOME", System.getProperty("java.home"));
        var proc = pb.start();
        var output = new String(proc.getErrorStream().readAllBytes());
        assertEquals(0, proc.waitFor(), "Executing " + prog + " should succeed. Output:\n" + output);
        return output;
    }

    enum LogFrom {
        SVM, HOTSPOT;

        void assertLog(String exp, String line) {
            assertTrue(line.startsWith("{"), "Has VM identifier beginning");
            var end = line.indexOf("} ");
            assertNotEquals(-1, end, "Has VM identifier end");
            var vm = line.substring(1, end);
            var msg = line.substring(end + 2);

            if (this == SVM) {
                assertEquals("Substrate VM", vm);
            } else {
                var openJDK = vm.contains("OpenJDK"); // OpenJDK 64-Bit Server VM
                var hotSpot = vm.contains("HotSpot"); // Java HotSpot(TM) 64-Bit Server VM
                assertTrue(openJDK || hotSpot, "Expecting HotSpot VM, but was: " + line);
            }
            assertEquals(exp, msg);
        }
    }
}
