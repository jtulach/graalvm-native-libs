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

import java.net.URL;
import java.util.Map;
import java.util.function.BiConsumer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JvmInsightTest {
    /** This is the {@link Factorial} class loaded by different classloader.
     * That classloader patches the bytecode of the loaded classes to be
     * {@link JvmInsight} capable. As the class is loaded by different classloader
     * that this testing class, we have to access it via reflection.
     */
    private static Class<?> FactorialHosted;

    @BeforeAll
    public static void initializeContext() throws Exception {
        var cp = Factorial.class.getProtectionDomain().getCodeSource().getLocation();
        var bothCp = new URL[] {
            JvmInsight.class.getProtectionDomain().getCodeSource().getLocation(),
            cp
        };
        var loader = new JvmInsightLoader(new AvoidClassLoader(Factorial.class), bothCp);
        FactorialHosted = loader.loadClass(Factorial.class.getName());
        assertNotEquals(Factorial.class, FactorialHosted, "Factorial shall be masked from this loader");
        assertNotNull(FactorialHosted, "Factorial class is loaded");
    }

    @Test
    public void testFactorialMethodInvocation() throws Exception {
        var methodFac = FactorialHosted.getMethod("fac", int.class);
        var sum = new int[1];
        var counter = (BiConsumer<String, Map<String, Object>>) (methodName, frame) -> {
            if (!methodName.contains("fac")) {
                return;
            }
            assertTrue(methodName.endsWith("fac(I)I"), "There is int fac(int): " + methodName);
            var n = (Number) frame.get("n");
            assertNotNull(n, "Local variable n is defined");
            sum[0] += n.intValue();
        };

        JvmInsight.apply((insight) -> {
            insight.on(FactorialHosted)
                .roots()
                .call(counter);
        });

        var res = (Number) methodFac.invoke(null, 5);
        assertEquals(120, res.intValue(), "Factorial is computed");
        assertEquals(15, sum[0], "Sum is being added to");
    }
}
