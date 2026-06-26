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
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Tests compatibility between Espresso+GraalVM Insight and JVM Insight with
 * Graal.js.
 */
public final class JvmInsightEspressoTest {
    /** Context to execute JavaScript Insight scripts in. */
    private static Context ctx;
    /** Captured output from the {@link #ctx} context. */
    private static ByteArrayOutputStream out;
    /**
     * The {@link Factorial} class loaded by Espresso. As Espresso supports
     * GraalVM Insight out of the box, there is no special configuration to
     * do to make the class GraalVM Insight capable.
     */
    private static Value Factorial;
    /** This is the {@link Factorial} class loaded by different classloader.
     * That classloader patches the bytecode of the loaded classes to be
     * {@link JvmInsight} capable. As the class is loaded by different classloader
     * that this testing class, we have to access it via reflection.
     */
    private static Class<?> FactorialHosted;

    public JvmInsightEspressoTest() {
    }

    @BeforeAll
    public static void initializeContext() throws Exception {
        out = new ByteArrayOutputStream();
        var cp = Factorial.class.getProtectionDomain().getCodeSource().getLocation();
        var bothCp = new URL[] {
            JvmInsight.class.getProtectionDomain().getCodeSource().getLocation(),
            cp
        };
        var hostAccess = HostAccess.newBuilder()
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

        var loader = new JvmInsightLoader(new AvoidClassLoader(Factorial.class), bothCp);
        FactorialHosted = loader.loadClass(Factorial.class.getName());
        assertNotEquals(Factorial.class, FactorialHosted, "Factorial shall be masked from this loader");
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
                debugger;
            }, {
                roots : true,
                rootNameFilter : '.*fac.*'
            });
            """;
        var warmUp = jvm.invokeFactorialMethodLong("fac", 6);
        assertEquals(720, warmUp);
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
    public void onReturnHook(JvmType jvm) throws Exception {
        var insight = """
            insight.on('return', (ctx, frame) => {
                print(`Computed for ${frame.n}. Factorial is ${frame.sum}.`);
            }, {
                roots : true,
                rootNameFilter : '.*simpleFac.*'
            });
            """;
        var warmUp = jvm.invokeFactorialMethodLong("simpleFac", 6);
        assertEquals(720, warmUp);
        try (
            var _ = jvm.applyInsight(ctx, insight, "print-sum.js")
        ) {
            var res = jvm.invokeFactorialMethodLong("simpleFac", 5);
            assertEquals(120, res);
        }

        assertEquals("""
        Computed for 5. Factorial is 120.
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
        Line 25 with n=5
        Line 28 with n=5
        Line 25 with n=4
        Line 28 with n=4
        Line 25 with n=3
        Line 28 with n=3
        Line 25 with n=2
        Line 28 with n=2
        Line 25 with n=1
        Line 26 with n=1
        Line 29 with n=2
        Line 29 with n=3
        Line 29 with n=4
        Line 29 with n=5
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

    @ParameterizedTest
    @EnumSource(JvmType.class)
    public void testLocalVariableTypesStatic(JvmType jvm) throws Exception {
        var insight = """
            insight.on('enter', (ctx, frame) => {
                let sb = "";
                let sep = "";
                for (let p in frame) {
                    if (p.startsWith("type_")) {
                        sb = sb + sep + p + ":" + frame[p];
                        sep = ","
                    }
                }
                print(sb);
            }, {
                roots : true,
                rootNameFilter : '.*allTypes.*'
            });
            """;
        long len;
        try (
            var _ = jvm.applyInsight(ctx, insight, "all-types.js")
        ) {
            len = jvm.invokeFactorialMethodLong("allTypes", "", false, (byte) 0x04, (short)32, 48, 6354L, 'X', 0.5f, 2.7);
        }

        var exp = Set.of(
            "type_z:false", "type_b:4", "type_s:32", "type_i:48",
            "type_l:6354", "type_c:X", "type_f:0.5", "type_d:2.7"
        );
        var act = Set.of(out.toString().trim().split(","));
        assertEquals(exp, act, "Properly captured all arguments");
        var allLen = exp.stream().map(s -> {
            return s.split(":")[1].length();
        }).reduce(0, (a, b) -> a + b);
        assertEquals(allLen.intValue(), len, "Computed length is the same");
    }

    @ParameterizedTest
    @EnumSource(JvmType.class)
    public void testLocalVariableTypesInstance(JvmType jvm) throws Exception {
        var insight = """
            insight.on('enter', (ctx, frame) => {
                let sb = "";
                let sep = "";
                for (let p in frame) {
                    if (p.startsWith("type_")) {
                        sb = sb + sep + p + ":" + frame[p];
                        sep = ","
                    }
                }
                print(sb);
            }, {
                roots : true,
                rootNameFilter : '.*allInstanceTypes.*'
            });
            """;
        long len;
        try (
            var _ = jvm.applyInsight(ctx, insight, "all-types.js")
        ) {
            len = jvm.invokeFactorialInstanceMethodLong("allInstanceTypes", "", false, (byte) 0x04, (short)32, 48, 6354L, 'X', 0.5f, 2.7);
        }

        var exp = Set.of(
            "type_z:false", "type_b:4", "type_s:32", "type_i:48",
            "type_l:6354", "type_c:X", "type_f:0.5", "type_d:2.7"
        );
        var act = Set.of(out.toString().trim().split(","));
        assertEquals(exp, act, "Properly captured all arguments");
        var allLen = exp.stream().map(s -> {
            return s.split(":")[1].length();
        }).reduce(0, (a, b) -> a + b);
        assertEquals(allLen.intValue(), len, "Computed length is the same");
    }

    @ParameterizedTest
    @EnumSource(JvmType.class)
    public void testChangeLocalVariable(JvmType jvm) throws Exception {
        var insight = """
            insight.on('enter', (ctx, frame) => {
                frame.a = 6;
                frame.b = 7;
            }, {
                roots : true,
                rootNameFilter : '.*mul.*'
            });
            """;
        try (
            var _ = jvm.applyInsight(ctx, insight, "change-locals.js")
        ) {
            var sixSeven= jvm.invokeFactorialMethodLong("mul", 5, 3);
            assertEquals(42, sixSeven, "6 * 7 = 42");
        }
    }

    public enum JvmType {
        ESPRESSO, JVM;

        public static final class Insight {

            private AutoCloseable handle;
            private Insight() {
            }

            public static class Ctx {
                @HostAccess.Export
                public final String name;
                @HostAccess.Export
                public final int line;

                private Ctx(String name, int line) {
                    this.name = name;
                    this.line = line;
                }
            }

            @HostAccess.Export
            public void on(String type, BiFunction<Object, Object, Object> fn, Map<String,Object> cfg)
            throws Exception {
                var filter = (String) cfg.get("rootNameFilter");
                var rootNameFilter = filter == null ? null : Pattern.compile(filter);
                final BiConsumer<String, Map<String, Object>> handler = (String where, Map<String,Object> frame) -> {
                    var lineSep = where.indexOf(':');
                    var line = Integer.parseInt(where.substring(0, lineSep));
                    var methodName = where.substring(lineSep + 1);
                    if (rootNameFilter == null || rootNameFilter.matcher(methodName).matches()) {
                        var ctx = new Ctx(methodName, line);
                        fn.apply(ctx, frame);
                    }
                };
                handle = JvmInsight.apply((insight) -> {
                    var bldr = insight.on(FactorialHosted);
                    switch (type) {
                        case "enter" -> bldr.when(JvmInsight.Builder.When.ENTER);
                        case "return" -> bldr.when(JvmInsight.Builder.When.RETURN);
                        default -> throw new IllegalStateException(type);
                    }
                    if (Boolean.TRUE.equals(cfg.get("roots"))) {
                        bldr.roots();
                    }
                    if (Boolean.TRUE.equals(cfg.get("statements"))) {
                        bldr.statements();
                    }
                    bldr.call(handler);
                });
            }
        }

        final AutoCloseable applyInsight(Context ctx, String code, String name)
                throws Exception {
            if (this == JVM) {
                var init = Source.newBuilder("js", """
                (function(insight) {
                    return function(code) {
                        return eval(code);
                    }
                })
                """, "init.js").build();
                var initFn = ctx.eval(init);
                var jvmInsight = new Insight();
                var evalFn = initFn.execute(jvmInsight);
                evalFn.executeVoid(code);

                return jvmInsight.handle;
            } else {
                var engine = ctx.getEngine();

                var insight = engine.getInstruments().get("insight");
                assertNotNull(insight, "There must be the insight instrument");

                @SuppressWarnings("unchecked")
                var fn = (Function<Source, AutoCloseable>) insight.lookup(Function.class);
                assertNotNull(fn, "There must be an insight registraiton function");

                var insightScript = Source.newBuilder("js", code, name).build();
                return fn.apply(insightScript);
            }
        }

        final long invokeFactorialMethodLong(String name, Object... args) {
            return switch (this) {
                case ESPRESSO -> Factorial.invokeMember(name, args).asLong();
                case JVM -> {
                    try {
                        for (var m : FactorialHosted.getMethods()) {
                            if (!m.getName().equals(name)) {
                                continue;
                            }
                            var value = m.invoke(null, args);
                            yield ((Number) value).longValue();
                        }
                        throw new IllegalStateException("Cannot find " + name);
                    } catch (ReflectiveOperationException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            };
        }

        final long invokeFactorialInstanceMethodLong(String name, Object... args) {
            return switch (this) {
                case ESPRESSO -> {
                    var inst = Factorial.newInstance();
                    yield inst.invokeMember(name, args).asLong();
                }
                case JVM -> {
                    try {
                        var inst = FactorialHosted.getConstructor().newInstance();
                        for (var m : FactorialHosted.getMethods()) {
                            if (!m.getName().equals(name)) {
                                continue;
                            }
                            var value = m.invoke(inst, args);
                            yield ((Number) value).longValue();
                        }
                        throw new IllegalStateException("Cannot find " + name);
                    } catch (ReflectiveOperationException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            };
        }
    }

}
