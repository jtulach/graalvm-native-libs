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
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Simple bytecode patching tests. Sometimes it is not easy to debug
 * {@link JvmInsightEspressoTest} as the bytecode being processed there
 * is <em>too complicated</em>. In such case it is recommended to create
 * a {@code simpleXyz} test in {@link Factorial} class demonstrating the
 * problem and test it from this class. E.g. without involving Espresso
 * at all.
 */
public class JvmInsightTest {
    private ClassLoader loader;

    /** This is the {@link Factorial} class loaded by different classloader.
     * That classloader patches the bytecode of the loaded classes to be
     * {@link JvmInsight} capable. As the class is loaded by different classloader
     * that this testing class, we have to access it via reflection.
     */
    private Class<?> loadFactorialClass() throws ClassNotFoundException {
        var clazz = loader.loadClass(Factorial.class.getName());
        assertNotEquals(Factorial.class, clazz, "Factorial shall be masked from this loader");
        assertNotNull(clazz, "Factorial class is loaded");
        return clazz;
    }

    @BeforeEach
    public void initializeContext() throws Exception {
        var cp = Factorial.class.getProtectionDomain().getCodeSource().getLocation();
        var bothCp = new URL[] {
            JvmInsight.class.getProtectionDomain().getCodeSource().getLocation(),
            cp
        };
        loader = JvmInsight.createLoader(new AvoidClassLoader(Factorial.class), bothCp);
    }

    @Test
    public void testFactorialMethodInvocation() throws Exception {
        var sum = new int[1];
        var counter = (BiConsumer<CharSequence, Map<String, Object>>) (at, frame) -> {
            var methodName = at.toString();
            if (!methodName.contains("fac")) {
                return;
            }
            assertTrue(methodName.endsWith("fac(I)I"), "There is int fac(int): " + methodName);
            var n = (Number) frame.get("n");
            assertNotNull(n, "Local variable n is defined");
            sum[0] += n.intValue();
        };

        var jvmInsight = JvmInsight.find(loader);
        jvmInsight.configure((_) -> true, (insight) -> {
            insight
                .roots()
                .call(counter);
        });

        var methodFac = loadFactorialClass().getMethod("fac", int.class);
        var res = (Number) methodFac.invoke(null, 5);
        assertEquals(120, res.intValue(), "Factorial is computed");
        assertEquals(15, sum[0], "Sum is being added to");
    }

    @Test
    public void testFactorialInstanceMethodInvocation() throws Exception {
        var sum = new int[1];
        var counter = (BiConsumer<CharSequence, Map<String, Object>>) (at, frame) -> {
            var methodName = at.toString();
            if (!methodName.contains("facInst")) {
                return;
            }
            assertTrue(methodName.endsWith("facInst(I)I"), "There is int facInst(int): " + methodName);


            assertTrue(frame.containsKey("n"), "key n is present");
            var n = (Number) frame.get("n");
            assertNotNull(n, "Local variable n is defined");

            assertTrue(frame.containsKey("this"), "key this is present");
            var thiz = frame.get("this");
            assertNotNull(thiz, "Local variable this is defined");
            sum[0] += n.intValue();
        };

        var jvmInsight = JvmInsight.find(loader);
        jvmInsight.configure((_) -> true, (insight) -> {
            insight
                .roots()
                .call(counter);
        });

        var methodFac = loadFactorialClass().getMethod("facInst", int.class);
        var inst = loadFactorialClass().getConstructor().newInstance();
        var res = (Number) methodFac.invoke(inst, 5);
        assertEquals(120, res.intValue(), "Factorial is computed");
        assertEquals(15, sum[0], "Sum is being added to");
    }

    @Test
    public void testFacEx() throws Exception {
        var method = loadFactorialClass().getMethod("facEx", int.class, int[].class);
        var res = new int[1];
        try {

            method.invoke(null, 6, res);
            fail("facEx method shall always yield an exception");
        } catch (ReflectiveOperationException ex) {
            assertEquals(720, res[0], "Yields correct result");
        }
    }

    @Test
    public void testSimpleLocalValueReturn() throws Exception {
        var method = loadFactorialClass().getMethod("simpleReturn", int.class);
        var res = method.invoke(null, 42);
        assertEquals(42, res);
    }

    @Test
    public void testSimpleLocalValueAssign() throws Exception {
        var method = loadFactorialClass().getMethod("simpleAssign", int.class);
        var res = method.invoke(null, 6);
        assertEquals(36, res);
    }

    @Test
    public void testSimpleLoopFac() throws Exception {
        var method = loadFactorialClass().getMethod("simpleFac", int.class);
        var res = method.invoke(null, 6);
        assertEquals(720, res);
    }

    @Test
    public void testSimpleShortFac() throws Exception {
        var method = loadFactorialClass().getMethod("simpleShortFac", byte.class);
        var res = method.invoke(null, (byte)4);
        assertEquals((short)24, res);
    }

    @Test
    public void testSimpleConcat() throws Exception {
        var method = loadFactorialClass().getMethod("simpleConcat", String.class, String.class);
        var res = method.invoke(null, "Hello ", "World!");
        assertEquals("Hello World!", res);
    }
}
