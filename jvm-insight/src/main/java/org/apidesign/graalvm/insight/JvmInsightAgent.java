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

/** Entry point for using JVM Insight as Java agent. Usage:
 * <pre>
 * java -javaagent:jvm-insight-*.jar Hello.java
 * </pre>
 * where {@code Hello} is a Java file that contains {@code main} method
 * and some other methods to track.
 *
 */
final class JvmInsightAgent implements ClassFileTransformer {
    private final ClassFile clazzFile;

    public static void premain(String args, Instrumentation instr) throws Exception {
        log("premain args: " + args);
        registerAgent(args, instr);
    }

    public static void agentmain(String args, Instrumentation instr) throws Exception {
        log("agentmain args: " + args);
        registerAgent(args, instr);
    }

    private static void registerAgent(String args, Instrumentation instr) throws Exception {
        instr.addTransformer(new JvmInsightAgent());
    }

    private JvmInsightAgent() {
        this.clazzFile = ClassFile.of();
    }

    @Override
    public byte[] transform(
        Module module, ClassLoader loader,
        String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer
    ) throws IllegalClassFormatException {
        if (className.contains("Hello")) {
            try {
                log("transforming " + className + " redefine: " + classBeingRedefined);
                var handle = JvmInsight.apply((insight) -> {
                    insight.on(null).roots().call((methodName, localVars) -> {
                        log("Callback for " + methodName + " with local variables: " + localVars);
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
