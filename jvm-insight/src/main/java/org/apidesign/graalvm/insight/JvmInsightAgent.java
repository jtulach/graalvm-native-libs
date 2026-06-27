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

import java.lang.classfile.ClassFile;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

/** Entry point for using JVM Insight as Java agent. Usage:
 * <pre>
 * java -javaagent:jvm-insight-*.jar Hello.java
 * </pre>
 * where {@code Hello} is a Java file that contains {@code main} method
 * and some other methods to track.
 *
 */
final class JvmInsightAgent implements ClassFileTransformer {
    /** Option to select what class to instrument.
     * Use {@code -javaagent=jvm-insight.jar=classes=.*Reg} to select
     * what classes to instrument.
     */
    private static final String OPT_CLASSES = "classes";

    /** Option to select what method to instrument.
     * Use {@code -javaagent=jvm-insight.jar=methods=.*Reg} to select
     * what classes to instrument.
     */
    private static final String OPT_METHODS = "methods";

    /** Option to specify a class to callback when an event happens.
     * The class must be reachable by a application classloader. The
     * class must be public and must have a default constructor. The
     * class should implement {@link BiConsumer}{@code <String, Map<String, Object>>}
     * and then it gets callback with a name of class+method+signature and
     * access to local variables.
     *
     * Use {@code -javaagent=jvm-insight.jar=handler=my.pkg.CallMe} to specify
     * what classes to instrument.
     */
    private static final String OPT_HANDLER = "handler";

    private final JvmInsight global;
    private final ClassFile clazzFile;
    private final Pattern pattern;
    private final Pattern methods;
    private final BiConsumer<String, Map<String, Object>> handler;

    public static void premain(String args, Instrumentation instr) throws Exception {
        registerAgent(args, instr);
    }

    public static void agentmain(String args, Instrumentation instr) throws Exception {
        registerAgent(args, instr);
    }

    private static void registerAgent(String args, Instrumentation instr) throws Exception {
        var insight = JvmInsight.enableInstrumentation(instr);
        Pattern classes = null;
        Pattern methods = null;
        BiConsumer<String, Map<String, Object>> handler = null;

        if (args != null) {
            var segments = args.split(",");
            for (var seg : segments) {
                var keyValue = seg.split("=");
                if (keyValue.length != 2) {
                    throw new IllegalStateException("Expecting key=value, but was: " + seg);
                }
                switch (keyValue[0]) {
                    case OPT_CLASSES -> {
                        classes = Pattern.compile(keyValue[1]);
                    }
                    case OPT_METHODS -> {
                        methods = Pattern.compile(keyValue[1]);
                    }
                    case OPT_HANDLER -> {
                        var clazz = Class.forName(keyValue[1], true, ClassLoader.getSystemClassLoader());
                        handler = (BiConsumer) clazz.getConstructor().newInstance();
                    }
                    default -> {
                        throw new IllegalStateException("Unknown option " + seg);
                    }
                }
            }
        }

        instr.addTransformer(new JvmInsightAgent(insight, classes, methods, handler));
    }

    private JvmInsightAgent(
        JvmInsight insight,
        Pattern pattern, Pattern methods,
        BiConsumer<String, Map<String, Object>> handler
    ) {
        if (pattern == null) {
            throw new IllegalArgumentException("Specify classses=.*MyClass.* filter");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Specify handler=my.pkg.MyHandler");
        }
        this.global = insight;
        this.clazzFile = ClassFile.of();
        this.pattern = pattern;
        this.methods = methods;
        this.handler = handler;
    }

    @Override
    public byte[] transform(
        Module module, ClassLoader loader,
        String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer
    ) throws IllegalClassFormatException {
        if (pattern.matcher(className).matches()) {
            try {
                log("Transforming " + className);
                var model = clazzFile.parse(classfileBuffer);
                var newByteCode = clazzFile.transformClass(model, new JvmInsightTransform(model));
                configure(className);
                return newByteCode;
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return null;
    }

    private static void log(String msg) {
        System.err.println("[JvmInsightAgent]: " + msg);
    }


    private void configure(String className) {
        var handle = global.configure((insight) -> {
                insight.apply(null).roots().call((methodName, localVars) -> {
                    if (methods == null || methods.matcher(methodName).matches()) {
                        handler.accept(methodName, localVars);
                    }
                });
        });
    }
}
