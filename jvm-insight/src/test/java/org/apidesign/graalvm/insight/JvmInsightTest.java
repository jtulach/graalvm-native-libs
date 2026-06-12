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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class JvmInsightTest {
    private static ByteArrayOutputStream out;
    private static Context ctx;
    private static Value Factorial;

    public JvmInsightTest() {
    }

    @BeforeAll
    public static void initializeContext() throws Exception {
        out = new ByteArrayOutputStream();
        var cp = Factorial.class.getProtectionDomain().getCodeSource().getLocation();
        var b = Context.newBuilder("js", "java")
                .option("java.Classpath", new File(cp.toURI()).getAbsolutePath())
                .out(out)
                .allowNativeAccess(true);
        ctx = b.build();
        ctx.enter();
        Factorial = ctx.getBindings("java").getMember("org.apidesign.graalvm.insight.Factorial");
        assertNotNull(Factorial, "Class is found");
    }

    @AfterAll
    public static void disposeContext() {
        ctx.close();
    }

    @BeforeEach
    public void clearThePreviousOutput() {
        out.reset();
    }

    @Test
    public void invokeFactorialWithInsights() throws Exception {
        var insight = """
            insight.on('enter', (ctx, frame) => {
                print(`Invoked ${ctx.name} with n=${frame.n}`);
            }, {
                roots : true,
                rootNameFilter : '.*fac.*'
            });
            """;
        try (
            var _ = applyInsight(ctx, insight, "print-n.js")
        ) {
            var res = Factorial.invokeMember("fac", 5);
            assertEquals(120, res.asLong());
        }

        assertEquals("""
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=5
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=4
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=3
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=2
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=1
        """, out.toString(), "Properly captured five invocation of fac(n)");
    }

    @Test
    public void trackStatementsEgLines() throws Exception {
        var insight = """
            insight.on('enter', (ctx, frame) => {
                print(`Line ${ctx.line} with n=${frame.n}`);
            }, {
                statements : true,
                rootNameFilter : '.*fac.*'
            });
            """;
        try (
            var _ = applyInsight(ctx, insight, "print-lines.js")
        ) {
            var res = Factorial.invokeMember("fac", 5);
            assertEquals(120, res.asLong());
        }

        assertEquals("""
        Line 21 with n=5
        Line 24 with n=5
        Line 21 with n=4
        Line 24 with n=4
        Line 21 with n=3
        Line 24 with n=3
        Line 21 with n=2
        Line 24 with n=2
        Line 21 with n=1
        Line 22 with n=1
        Line 25 with n=2
        Line 25 with n=3
        Line 25 with n=4
        Line 25 with n=5
        """, out.toString(), "Properly captured five invocation of fac(n)");
    }

    private static AutoCloseable applyInsight(Context ctx, String code, String name)
    throws IOException {
        var engine = ctx.getEngine();

        var insight = engine.getInstruments().get("insight");
        assertNotNull(insight, "There must be an insight instrument");

        @SuppressWarnings("unchecked")
        var fn = (Function<Source, AutoCloseable>) insight.lookup(Function.class);
        assertNotNull(fn, "There must be an insight registraiton function");

        var insightScript = Source.newBuilder("js", code, name).build();
        return fn.apply(insightScript);
    }
}
