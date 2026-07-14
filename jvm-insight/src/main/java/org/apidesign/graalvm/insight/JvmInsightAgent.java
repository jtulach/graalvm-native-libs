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
 * java -javaagent:jvm-insight-*.jar=pkg.Clazz Hello.java
 * </pre>
 * where {@code Hello} is a Java file that contains {@code main} method
 * and some other methods to track.
 * <p>
 * The {@code pkg.Clazz} must be a public class with a {@code public static}
 * method called {@code insightmain} that will be called by this agent.
 * The method shall have two arguments. First {@link String} for arguments
 * passed when initializing the agent and then an {@link JvmInsight} for
 * configuring the insight.
 * The agent will provide them after setting up necessary infrastructure.
 * </p>
 */
final class JvmInsightAgent implements ClassFileTransformer {
    private final JvmInsight insight;
    private final Instrumentation instr;
    private final ClassFile clazzFile;

    public static void premain(String args, Instrumentation instr) throws Exception {
        registerAgent(args, instr);
    }

    public static void agentmain(String args, Instrumentation instr) throws Exception {
        registerAgent(args, instr);
    }

    private static void registerAgent(String args, Instrumentation instr) throws Exception {
        if (args == null) {
            throw new NullPointerException("Specify name of class with insightmain method as argument!");
        }
        JvmInsightInitializer.withInstrumentation(instr);

        var agent = new JvmInsightAgent(instr);

        var comma = args.indexOf(',');
        var className= comma == -1 ? args : args.substring(0, comma);
        var remainingArgs = comma == -1 ? "" : args.substring(comma + 1);

        var insightMainClass = Class.forName(className, true, ClassLoader.getSystemClassLoader());
        var insightMainMethod = insightMainClass.getMethod("insightmain", String.class, JvmInsight.class);
        insightMainMethod.invoke(null, remainingArgs, agent.insight);

        instr.addTransformer(agent);
    }

    private JvmInsightAgent(Instrumentation instr) {
        this.instr = instr;
        this.clazzFile = ClassFile.of();
        this.insight = JvmInsight.find(null);
    }

    @Override
    public byte[] transform(
        Module module, ClassLoader loader,
        String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer
    ) throws IllegalClassFormatException {
        var info = new JvmInsight.ClassInfo(className, module, loader);
        if (info.instrumentClass(insight)) {
            log("Transforming " + className);
            try {
                var model = clazzFile.parse(classfileBuffer);
                var newByteCode = clazzFile.transformClass(model, JvmInsightTransform.create(model));
                return newByteCode;
            } catch (Throwable t) {
                t.printStackTrace();
                throw t;
            }
        } else {
            return null;
        }
    }

    private static void log(String msg) {
        System.err.println("[JvmInsightAgent]: " + msg);
    }
}
