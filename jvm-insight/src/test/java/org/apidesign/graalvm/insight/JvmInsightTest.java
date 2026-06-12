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

import java.io.File;
import org.graalvm.polyglot.Context;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JvmInsightTest {
    public JvmInsightTest() {
    }

    @Test
    public void testMain() throws Exception {
        var cp = Factorial.class.getProtectionDomain().getCodeSource().getLocation();
        var b = Context.newBuilder("js", "java")
                .option("java.Classpath", new File(cp.toURI()).getAbsolutePath())
                .allowNativeAccess(true);
        try (var ctx = b.build()) {
            var Factorial = ctx.getBindings("java").getMember("org.apidesign.graalvm.insight.Factorial");
            assertNotNull(Factorial, "Class is found");
            var res = Factorial.invokeMember("fac", 5);
            assertEquals(120, res.asLong());
        }
    }

}
