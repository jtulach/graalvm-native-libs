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
package e2e;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import org.apidesign.graalvm.insight.JvmInsight;

/* This is not a class to execute. Thus just: */
// $ exit

public final class PrintingCallback implements BiConsumer<String, Map<String, Object>> {
    /** A JVM Insight script entry point. Any {@code public static} method
     * named {@code insightmain} with two ({@code String} and
     * {@code JvmInsight}) arguments can be an entry point.
     * Such an entry point is invoked when the JVM Insight agent is initiated by
     *
     * <pre>
     * java -cp ${classes} -javaagent:${jvminsight}=e2e.PrintingCallback,additional arguments
     * </pre>
     *
     * The name of the class is given as the first segment before {@code ,}.
     * The arguments is the text after the {@code ,} or an empty string.
     *
     * @param args additional arguments passed
     * @param insight reference to JVM Insight to configure it
     */
    public static void insightmain(String args, JvmInsight insight) {
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

        var register = new PrintingCallback(insight, classes, methods);
    }

    private final Pattern methods;
    private PrintingCallback(JvmInsight insight, Pattern classes, Pattern methods) {
        this.methods = methods;
        insight.configure((n) -> {
            return classes.matcher(n).matches();
        }, (b) -> {
            b.apply(null).methodName(methods).roots().call(this);
        });
    }


    @Override
    public void accept(String methodName, Map<String, Object> localVars) {
        if (methods.matcher(methodName).matches()) {
            var msg = "[Callback]: Method " + methodName + " with local variables: " + localVars;
            System.err.println(msg);
        }
    }

    //
    // parsing support
    //

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

}
