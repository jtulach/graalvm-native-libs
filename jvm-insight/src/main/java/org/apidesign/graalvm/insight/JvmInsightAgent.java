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

    private final ClassFile clazzFile;
    private final Pattern pattern;
    private final Pattern methods;

    public static void premain(String args, Instrumentation instr) throws Exception {
        registerAgent(args, instr);
    }

    public static void agentmain(String args, Instrumentation instr) throws Exception {
        registerAgent(args, instr);
    }

    private static void registerAgent(String args, Instrumentation instr) throws Exception {
        Pattern classes = null;
        Pattern methods = null;

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
                    default -> {
                        throw new IllegalStateException("Unknown option " + seg);
                    }
                }
            }
        }

        instr.addTransformer(new JvmInsightAgent(classes, methods));
    }

    private JvmInsightAgent(Pattern pattern, Pattern methods) {
        if (pattern == null) {
            throw new IllegalArgumentException("Specify pattern=.*MyClass.*method.*");
        }
        this.clazzFile = ClassFile.of();
        this.pattern = pattern;
        this.methods = methods;
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
                var handle = JvmInsight.apply((insight) -> {
                    insight.on(null).roots().call((methodName, localVars) -> {
                        if (methods == null || methods.matcher(methodName).matches()) {
                            log("Callback for " + methodName + " with local variables: " + localVars);
                        }
                    });
                });
                var model = clazzFile.parse(classfileBuffer);
                var newByteCode = clazzFile.transformClass(model, new JvmInsightTransform(model));
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
}
