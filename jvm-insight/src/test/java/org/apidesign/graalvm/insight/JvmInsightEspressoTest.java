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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
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
    /** The {@link Factorial} class must be loaded by different classloader.
     * That classloader patches the bytecode of the loaded classes to be
     * {@link JvmInsight} capable.
     */
    private static ClassLoader loader;

    public JvmInsightEspressoTest() {
    }

    @BeforeAll
    public static void initializeContext() throws Exception {
        out = new ByteArrayOutputStream();
        var cp = Factorial.class.getProtectionDomain().getCodeSource().getLocation();
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
    }

    @BeforeEach
    public void initializeLoader() throws Exception {
        var cp = Factorial.class.getProtectionDomain().getCodeSource().getLocation();
        var bothCp = new URL[] {
            JvmInsight.class.getProtectionDomain().getCodeSource().getLocation(),
            cp
        };
        loader = JvmInsight.createLoader(new AvoidClassLoader(Factorial.class), bothCp);
    }

    /** This is the {@link Factorial} class loaded by different classloader.
     * That classloader patches the bytecode of the loaded classes to be
     * {@link JvmInsight} capable. As the class is loaded by different classloader
     * that this testing class, we have to access it via reflection.
     */
    private static Class<?> FactorialHosted() throws ClassNotFoundException {
        var FactorialHosted = loader.loadClass(Factorial.class.getName());
        assertNotEquals(Factorial.class, FactorialHosted, "Factorial shall be masked from this loader");
        assertNotNull(FactorialHosted, "Factorial class is loaded");
        return FactorialHosted;
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

    @ParameterizedTest
    @EnumSource(JvmType.class)
    public void testConcatStepByStep(JvmType jvm) throws Exception {
        var insight = """
            function printDump(msg, frame) {
                let sb = msg;
                let sep = "";
                for (let p in frame) {
                    sb = sb + sep + p + ":" + frame[p];
                    sep = ","
                }
                print(sb);
            }

            insight.on('enter', (ctx, frame) => {
                printDump(`Method entered ${ctx.name}`, frame);
            }, {
                roots : true,
                rootNameFilter : '.*simpleConcat.*'
            });
            insight.on('return', (ctx, frame) => {
                printDump(`Method exited ${ctx.name}`, {}); // should be frame ...
                      // ... but Espresso is not using proper argument names here
            }, {
                roots : true,
                rootNameFilter : '.*simpleConcat.*'
            });

            insight.on('return', (ctx, frame) => {
                printDump(`Step over finished at ${ctx.line}: `, frame);
            }, {
                statements : true,
                rootNameFilter : '.*simpleConcat.*'
            });
            """;
        try (
            var _ = jvm.applyInsight(ctx, insight, "step-over.js")
        ) {
            var hi= jvm.invokeFactorialMethodString("simpleConcat", "Hi", "There!");
            assertEquals("HiThere!", hi);
        }
        assertEquals("""
        Method entered Lorg/apidesign/graalvm/insight/Factorial;.simpleConcat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;a:Hi,b:There!
        Step over finished at 34: a:Hi,b:There!,sb:
        Step over finished at 35: a:Hi,b:There!,sb:Hi
        Step over finished at 36: a:Hi,b:There!,sb:HiThere!
        Step over finished at 37: a:Hi,b:There!,sb:HiThere!
        Method exited Lorg/apidesign/graalvm/insight/Factorial;.simpleConcat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        """, out.toString(), "Properly captured output while stepping thru the function");
    }

    @ParameterizedTest
    @EnumSource(JvmType.class)
    public void testCountDownThrow(JvmType jvm) throws Exception {
        var insight = """
            insight.on('return', (ctx, frame) => {
                let sb = `${ctx.name.match(/;\\.([\\w]*)/)[1]}:`;
                let sep = "";
                for (let p in frame) {
                    sb = sb + sep + p + "=" + frame[p];
                    sep = ","
                }
                print(sb);
            }, {
                roots : true,
                rootNameFilter : '.*countDown.*'
            });
            """;
        try (
            var _ = jvm.applyInsight(ctx, insight, "countDown.js")
        ) {
            jvm.invokeFactorialMethodLong("countDown", 10);
        } catch (IllegalArgumentException ex) {
            assertEquals("Count down", ex.getMessage());
        } catch (PolyglotException ex) {
            assertEquals(IllegalArgumentException.class.getName(), ex.getGuestObject().getMetaObject().getMetaQualifiedName());
            assertEquals("Count down", ex.getMessage());
        }
        assertEquals("""
        countDown:arg_0=0
        countDown:arg_0=1
        countDown:arg_0=2
        countDown:arg_0=3
        countDown:arg_0=4
        countDown:arg_0=5
        countDown:arg_0=6
        countDown:arg_0=7
        countDown:arg_0=8
        countDown:arg_0=9
        countDown:arg_0=10
        """, out.toString(), "Properly captured output while throwing an exception");

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
                final BiConsumer<JvmInsight.At, Map<String, Object>> handler = (var at, var frame) -> {
                    var where = at.toString();
                    var lineSep = where.indexOf(':');
                    var line = Integer.parseInt(where.substring(0, lineSep));
                    var methodName = where.substring(lineSep + 1);
                    if (rootNameFilter == null || rootNameFilter.matcher(methodName).matches()) {
                        var ctx = new Ctx(methodName, line);
                        var txtFrame = new LinkedHashMap<String, Object>();
                        var needsTxtFrame = false;
                        for (var en : frame.entrySet()) {
                            if (en.getValue() instanceof StringBuilder sb) {
                                needsTxtFrame = true;
                                txtFrame.put(en.getKey(), sb.toString());
                            } else {
                                txtFrame.put(en.getKey(), en.getValue());
                            }
                        }
                        fn.apply(ctx, needsTxtFrame ? txtFrame : frame);
                    }
                };
                var jvmInsight = JvmInsight.find(loader);
                handle = jvmInsight.configure((_) -> true, (bldr) -> {
                    switch (type) {
                        case "enter" -> bldr.when(JvmInsight.When.ENTER);
                        case "return" -> bldr.when(JvmInsight.When.RETURN);
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

        final long invokeFactorialMethodLong(String name, Object... args) throws Exception {
            return switch (this) {
                case ESPRESSO -> Factorial.invokeMember(name, args).asLong();
                case JVM -> {
                    try {
                        for (var m : FactorialHosted().getMethods()) {
                            if (!m.getName().equals(name)) {
                                continue;
                            }
                            var value = m.invoke(null, args);
                            yield ((Number) value).longValue();
                        }
                        throw new IllegalStateException("Cannot find " + name);
                    } catch (InvocationTargetException ex) {
                        if (ex.getTargetException() instanceof Exception target) {
                            throw target;
                        } else {
                            throw ex;
                        }
                    }
                }
            };
        }

        final String invokeFactorialMethodString(String name, Object... args) {
            return switch (this) {
                case ESPRESSO -> Factorial.invokeMember(name, args).asString();
                case JVM -> {
                    try {
                        for (var m : FactorialHosted().getMethods()) {
                            if (!m.getName().equals(name)) {
                                continue;
                            }
                            var value = m.invoke(null, args);
                            yield (String) value;
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
                        var inst = FactorialHosted().getConstructor().newInstance();
                        for (var m : FactorialHosted().getMethods()) {
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
