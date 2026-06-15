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
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class JvmInsightTest {
    private static ByteArrayOutputStream out;
    private static Context ctx;
    private static Value Factorial;
    private static Class<?> FactorialHosted;

    public JvmInsightTest() {
    }

    @BeforeAll
    public static void initializeContext() throws Exception {
        out = new ByteArrayOutputStream();
        var cp = Factorial.class.getProtectionDomain().getCodeSource().getLocation();
        final HostAccess hostAccess = HostAccess.newBuilder()
                .allowAccessAnnotatedBy(HostAccess.Export.class)
                .allowImplementationsAnnotatedBy(FunctionalInterface.class)
                .allowMapAccess(true)
                .build();
        var b = Context.newBuilder("js", "java")
                .option("java.Classpath", new File(cp.toURI()).getAbsolutePath())
                .out(out)
                .allowHostAccess(hostAccess)
                .allowNativeAccess(true);
        ctx = b.build();
        ctx.enter();
        Factorial = ctx.getBindings("java").getMember("org.apidesign.graalvm.insight.Factorial");
        assertNotNull(Factorial, "Class is found");

        var loader = new JvmInsight(Factorial.class.getClassLoader().getParent(), cp);
        FactorialHosted = loader.loadClass(Factorial.class.getName());
        assertNotNull(FactorialHosted, "Factorial class is loaded");
    }

    @AfterAll
    public static void disposeContext() {
        ctx.close();
    }

    @BeforeEach
    public void clearThePreviousOutput() {
        out.reset();
    }

    @ParameterizedTest
    @EnumSource(JvmType.class)
    public void invokeFactorialWithInsights(JvmType jvm) throws Exception {
        var insight = """
            insight.on('enter', (ctx, frame) => {
                print(`Invoked ${ctx.name} with n=${frame.n}`);
            }, {
                roots : true,
                rootNameFilter : '.*fac.*'
            });
            """;
        try (
            var _ = jvm.applyInsight(ctx, insight, "print-n.js")
        ) {
            var res = jvm.invokeFactorialMethodLong("fac", 5);
            assertEquals(120, res);
        }

        assertEquals("""
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=5
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=4
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=3
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=2
        Invoked Lorg/apidesign/graalvm/insight/Factorial;.fac(I)I with n=1
        """, out.toString(), "Properly captured five invocation of fac(n)");
    }

    @ParameterizedTest
    @EnumSource(JvmType.class)
    public void trackStatementsEgLines(JvmType jvm) throws Exception {
        var insight = """
            insight.on('enter', (ctx, frame) => {
                print(`Line ${ctx.line} with n=${frame.n}`);
            }, {
                statements : true,
                rootNameFilter : '.*fac.*'
            });
            """;
        try (
            var _ = jvm.applyInsight(ctx, insight, "print-lines.js")
        ) {
            var res = jvm.invokeFactorialMethodLong("fac", 5);
            assertEquals(120, res);
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
        """, out.toString(), "Properly captured stepping thru the fac function");
    }

    @ParameterizedTest
    @EnumSource(JvmType.class)
    public void noExpressionsInEspresso(JvmType jvm) throws Exception {
        var insight = """
            insight.on('enter', (ctx, frame) => {
                print(`Line ${ctx.line} with n=${frame.n}`);
            }, {
                expressions : true,
                rootNameFilter : '.*fac.*'
            });
            """;
        try (
            var _ = jvm.applyInsight(ctx, insight, "print-lines.js")
        ) {
            var res = jvm.invokeFactorialMethodLong("fac", 5);
            assertEquals(120, res);
        }

        assertEquals("", out.toString(), "Expressions aren't supported by Espresso");
    }

    public enum JvmType {
        ESPRESSO, JVM;

        public static final class Insight {
            private Insight() {
            }

            public static class Ctx {
                @HostAccess.Export
                public final String name;

                private Ctx(String name) {
                    this.name = name;
                }
            }

            @HostAccess.Export
            public void on(String type, BiFunction<Object, Object, Object> fn, Map<String,Object> cfg)
            throws Exception {
                var filter = (String) cfg.get("rootNameFilter");
                var rootNameFilter = filter == null ? null : Pattern.compile(filter);
                System.err.println("type: " + type + " fn: " + fn + " cfg: " + cfg);
                var f = FactorialHosted.getField("TRACE");
                f.set(null, (BiConsumer<?, ?>) (String methodName, Map<String,Object> frame) -> {
                    if (rootNameFilter == null || rootNameFilter.matcher(methodName).matches()) {
                        var ctx = new Ctx(methodName);
                        fn.apply(ctx, frame);
                    }
                    System.err.println("tracing method enter: " + methodName + " with " + frame);
                });
            }
        }

        final AutoCloseable applyInsight(Context ctx, String code, String name)
                throws Exception {
            if (this == JVM) {
                var init = """
                (function(insight) {
                    return function(code) {
                        return eval(code);
                    }
                })
                """;
                var initFn = ctx.eval("js", init);
                var insight = new Insight();
                var evalFn = initFn.execute(insight);
                evalFn.executeVoid(code);

                return () -> {
                    var f = FactorialHosted.getField("TRACE");
                    f.set(null, null);
                };
            }
            var engine = ctx.getEngine();

            var insight = engine.getInstruments().get("insight");
            assertNotNull(insight, "There must be an insight instrument");

            @SuppressWarnings("unchecked")
            var fn = (Function<Source, AutoCloseable>) insight.lookup(Function.class);
            assertNotNull(fn, "There must be an insight registraiton function");

            var insightScript = Source.newBuilder("js", code, name).build();
            return fn.apply(insightScript);
        }

        final long invokeFactorialMethodLong(String name, Object... args) {
            return switch (this) {
                case ESPRESSO -> Factorial.invokeMember(name, args).asLong();
                case JVM -> {
                    try {
                        var value = FactorialHosted.getMethod(name, int.class).invoke(null, args);
                        yield ((Number) value).longValue();
                    } catch (ReflectiveOperationException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            };
        }
    }
}
