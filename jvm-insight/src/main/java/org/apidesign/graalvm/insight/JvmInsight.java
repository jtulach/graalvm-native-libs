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
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public final class JvmInsight  {
    private static final Map<Builder.When, BiConsumer<String, Map<String, Object>>> ROOTS = new EnumMap<>(Builder.When.class);
    private static final Map<Builder.When, BiConsumer<String, Map<String, Object>>> STATEMENTS = new EnumMap<>(Builder.When.class);

    private JvmInsight() {
    }

    /**
     * Applies new Insights to the running JVM.
     *
     * @param block block that receives an instance of {@link JvmInsight}
     *   and can use it to configure its JVM Insights
     * @return a handle that can be {@link AutoCloseable#close()} when
     *   these insights are to be disabled
     */
    public static AutoCloseable apply(Consumer<JvmInsight> block) {
        block.accept(new JvmInsight());
        return () -> {
            ROOTS.clear();
            STATEMENTS.clear();
        };
    }

    /**
     * Creates a {@link JvmInsight}-ready classloader. Classes loaded by
     * this {@link ClassLoader} are patched to be ready for {@link #apply}-ing
     * JVM Insights.
     *
     * @param parent the parent classloader to use or {@code null}
     * @param cp set of classpath elements to load classes from
     * @return the JVM Insights ready classloader
     */
    public static ClassLoader createLoader(ClassLoader parent, URL... cp) {
        return new JvmInsightLoader(parent, cp);
    }

    /** Registers an Insight handler on given type. This method selects
     * a class to operate on and returns a builder to configure the <em>Insight</em>.
     *
     * @param clazz the clazz to operate on
     * @return builder to configure and finish with {@link Builder#call} to register
     *    the callback
     */
    public Builder on(Class<?> clazz) {
        return new Builder(clazz);
    }

    /** Configuration for an Insight callback.
     * Use methods of this class to configure a callback and then register
     * it by calling {@link Builder#call}.
     */
    public static final class Builder {
        private final Class<?> clazz;
        private boolean statements;
        private boolean roots;
        private Pattern methodFilter;
        private When when = When.ENTER;

        private Builder(Class<?> clazz) {
            this.clazz = clazz;
        }

        public enum When {
            ENTER, RETURN;
        }

        public Builder when(When type) {
            Objects.requireNonNull(type);
            this.when = type;
            return this;
        }

        public Builder roots() {
            this.roots = true;
            return this;
        }

        public Builder statements() {
            this.statements = true;
            return this;
        }

        public Builder methodName(Pattern regExp) {
            this.methodFilter = regExp;
            return this;
        }

        public void call(BiConsumer<String, Map<String, Object>> handler) {
            class Convertor implements BiConsumer<String, Map<String, Object>> {
                @Override
                public void accept(String t, Map<String, Object> data) {
                    var names = (String[])data.get("names");
                    var values = (Object[])data.get("values");
                    var frame = new HashMap<String, Object>();
                    for (var i = 0; i < names.length; i++) {
                        if (names[i] != null) {
                            frame.put(names[i], values[i]);
                        }
                    }
                    handler.accept(t, frame);
                    for (var i = 0; i < names.length; i++) {
                        if (names[i] != null) {
                            values[i] = frame.get(names[i]);
                        }
                    }
                }
            }
            if (roots) {
                ROOTS.put(when, new Convertor());
            }
            if (statements) {
                STATEMENTS.put(when, new Convertor());
            }
        }
    }


    /** Creates a dynamically configurable site for JVM Insight. Used by
     * bytecode manipulation transformers that patch methods to be ready for
     * {@link JvmInsight}.
     *
     * @param lkp lookup of the class that is being bytecode patched
     * @param name name of the configuration to fetch
     *    - either {@code "ROOTS"} or {@code "STATEMENTS"}.
     * @param type requested method type
     * @param when which kind of event this call site shall trigger
     *    - {@code "enter"} or {@code "return"}
     * @return the callsite
     * @throws IllegalArgumentException if the {@code name} isn't recognized
     */
    public static CallSite metafactory(
        MethodHandles.Lookup lkp, String name, MethodType type, String when
    ) {
        try {
            var myLkp = MethodHandles.lookup();
            var rawHandle = myLkp.findStatic(JvmInsight.class, name, MethodType.methodType(BiConsumer.class, Builder.When.class));
            var handle = switch (when) {
                case "enter" -> rawHandle.bindTo(Builder.When.ENTER);
                case "return" -> rawHandle.bindTo(Builder.When.RETURN);
                default -> throw new IllegalArgumentException(when);
            };
            return new ConstantCallSite(handle);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static BiConsumer<String, Map<String, Object>> roots(Builder.When when) {
        return ROOTS.get(when);
    }

    private static BiConsumer<String, Map<String, Object>> statements(Builder.When when) {
        return STATEMENTS.get(when);
    }
}
