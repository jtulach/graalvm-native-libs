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

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.function.BiConsumer;

public final class JvmInsight  {
    private JvmInsight() {
    }

    public static BiConsumer<String, Map<String, Object>> ROOTS;
    public static BiConsumer<String, Map<String, Object>> STATEMENTS;

    /** Creates a dynamically configurable site for JVM Insight. Used by
     * bytecode manipulation methods that patch methods to be ready for
     * {@link JvmInsight}.
     *
     * @param lkp lookup to use
     * @param name name of method
     * @param type requested method type
     * @return the callsite
     * @throw IllegalArgumentException if the {@code name} isn't recognized
     */
    public static CallSite metafactory(
        MethodHandles.Lookup lkp, String name, MethodType type
    ) {
        try {
            var myLkp = MethodHandles.lookup();
            var handle = switch (name) {
                case "ROOTS" -> myLkp.findStaticGetter(JvmInsight.class, "ROOTS", BiConsumer.class);
                case "STATEMENTS" -> myLkp.findStaticGetter(JvmInsight.class, "STATEMENTS", BiConsumer.class);
                default -> throw new NoSuchFieldException(name);
            };
            return new ConstantCallSite(handle);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

}
