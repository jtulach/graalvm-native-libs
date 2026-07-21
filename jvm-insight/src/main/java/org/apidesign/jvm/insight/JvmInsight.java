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
package org.apidesign.jvm.insight;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/** {@link JvmInsight} allows advanced instrumentation to be applied to
 * classes running inside of the JVM.
 * <ul>
 *   <li>Use {@link JvmInsight#find} method to obtain instance of the JVM Insight</li>
 *   <li>Then use its {@link JvmInsight#configure} method to setup up an
 *      Insight hook</li>
 * </ul>
 *
 */
public final class JvmInsight  {
    /** Default JVM Insight to be used for the whole JVM.
     */
    private static final JvmInsight DEFAULT = new JvmInsight(
        JvmInsightInitializer.getInstrumentation()
    );
    private final Object instr;
    private final List<Registry> registrations = new CopyOnWriteArrayList<>();

    JvmInsight(Object instr) {
        this.instr = instr;
    }

    /**
     * Finds a JVM Insight hook for given loader.
     * @param loader the classloader to find associated insight for
     * @return an instance of JVM Insight associated with the loader
     *    or a dummy instance, if the loader supports no JVM Insight
     */
    public static JvmInsight find(ClassLoader loader) {
        return switch (loader) {
            case JvmInsightLoader insightLoader -> insightLoader.getJvmInsight();
            case null, default -> DEFAULT;
        };
    }

    /**
     * Applies new Insights to the running JVM.
     *
     * @param classFilter identifies which classes to instrument with the
     *   JVM Insight capabilities. The argument for the function is the
     *   fully qualified JVM name of the class -
     *   e.g. {@code java/lang/String}, etc.
     *
     * @param block block that receives an instance of {@link JvmInsight.Builder}
     *   factory and can use it to configure its JVM Insights. The block
     *   is invoked once, at the first moment an eligible class is found
     *
     * @return a handle that can be {@link AutoCloseable#close()} when
     *   these insights are to be disabled
     */
    public AutoCloseable configure(
        Predicate<? super ClassInfo> classFilter,
        Consumer<? super Builder> block
    ) {
        var registrar = new Registry(classFilter, block);
        registrations.add(registrar);
        return registrar;
    }

    /**
     * Creates a {@link JvmInsight}-ready classloader. Classes loaded by
     * this {@link ClassLoader} are patched to be ready for {@link #configure}-ing
     * JVM Insights.
     *
     * @param parent the parent classloader to use or {@code null}
     * @param cp set of classpath elements to load classes from
     * @return the JVM Insights ready classloader
     */
    public static ClassLoader createLoader(ClassLoader parent, URL... cp) {
        var loader = new JvmInsightLoader(parent, cp);
        return loader;
    }

    /** Info about class to be loaded. In addition to providing various
     * info about the class to be loaded, it also implements a {@link CharSequence}
     * representing the same content of {@link #name()}, so filters
     * can work with generic type when just a name is enough to filter.
     */
    public final static class ClassInfo implements CharSequence {
        private final String name;
        private final Module module;
        private final ClassLoader loader;

        ClassInfo(String name, Module module, ClassLoader loader) {
            this.name = name.replace('.', '/');
            this.module = module;
            this.loader = loader;
        }

        /** Fully qualified name with dots. E.g. {@code java.lang.String}.
         *
         * @return name in the {@link Class#getName()} format
         */
        public final String name() {
            return name.replace('/', '.');
        }

        /** Fully qualified name with slashes. E.g. {@code java/lang/String}.
         *
         * @return name in the JVM ready format
         */
        public final String jvmName() {
            return name;
        }

        /** The classloader loading this class.
         *
         * @return the classloader
         */
        public ClassLoader loader() {
            return loader;
        }

        /** Same string as {@link #jvmName()}.
         *
         * @return name of the class.
         */
        @Override
        public String toString() {
            return name;
        }

        /** @return {@code jvmName().length()} */
        @Override
        public int length() {
            return name.length();
        }

        /** @return {@code jvmName().charAt(index)} */
        @Override
        public char charAt(int index) {
            return name.charAt(index);
        }

        /** @return {@code jvmName().subSequence(start, end)} */
        @Override
        public CharSequence subSequence(int start, int end) {
            return name.subSequence(start, end);
        }

        /** Check whether a class to be loaded shall be patched.
         *
         * @return true if this class should be instrumented at least by
         *   one of registered insights
         */
        final boolean instrumentClass(JvmInsight insight) {
            if (name.startsWith("org.apidesign.jvm.insight.JvmInsight")) {
                // avoid self recursion
                return false;
            }
            var instrument = false;
            for (var r : insight.registrations) {
                if (r.filter.test(this)) {
                    instrument = true;
                }
            }
            return instrument;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + Objects.hashCode(this.name);
            hash = 37 * hash + Objects.hashCode(this.module);
            hash = 37 * hash + Objects.hashCode(this.loader);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ClassInfo other = (ClassInfo) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            if (!Objects.equals(this.module, other.module)) {
                return false;
            }
            return Objects.equals(this.loader, other.loader);
        }


    }

    /** Info about a method being defined. In addition to providing various
     * info about the methodto be loaded, it also implements a {@link CharSequence}
     * giving access to
     */
    public static final class MethodInfo implements CharSequence {
        private final ClassInfo clazz;
        private final String name;
        private final String descriptor;

        private MethodInfo(ClassInfo clazz, String methodName, String methodDescriptor) {
            this.clazz = clazz;
            this.name = methodName;
            this.descriptor = methodDescriptor;
        }

        /** Info about class owning this method. Such an info may be needed
         * before the actual class is loaded into the JVM, hence it is provided
         * as {@link ClassInfo}.
         *
         * @return the info about class owning this method
         * @see ClassInfo
         */
        public ClassInfo clazz() {
            return clazz;
        }

        /** Method name.
         *
         * @return non-null name of the method
         */
        public String name() {
            return name;
        }

        /** JVM descriptor of the method type. Include types of arguments
         * as well as return type.
         *
         * @return descriptor the method type
         */
        public String descriptor() {
            return descriptor;
        }

        @Override
        public String toString() {
            return "L" + clazz().jvmName() + ";." + name() + descriptor();
        }

        @Override
        public int length() {
            return toString().length();
        }

        @Override
        public char charAt(int index) {
            return toString().charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return toString().subSequence(start, end);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 37 * hash + Objects.hashCode(this.clazz);
            hash = 37 * hash + Objects.hashCode(this.name);
            hash = 37 * hash + Objects.hashCode(this.descriptor);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MethodInfo other = (MethodInfo) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            if (!Objects.equals(this.descriptor, other.descriptor)) {
                return false;
            }
            return Objects.equals(this.clazz, other.clazz);
        }


    }

    /** Type of JVM Insight event. */
    public enum When {
        ENTER, RETURN;
    }

    /** Identifies a location of JVM Insight event. It carries individual
     * informations about {@link #line()}, {@link #when()}, {@link #where()}
     * as well as implements {@link CharSequence} that represents fully
     * qualified identification of the location - equivalent
     * of {@link #toString()}.
     */
    public static final class At implements CharSequence {
        private final When when;
        private final Class<?> clazz;
        private final MethodInfo method;
        private final int line;
        private final String fqn;

        private At(
            When when, Class<?> clazz, MethodInfo method, int line
        ) {
            this.when = when;
            this.clazz = clazz;
            this.method = method;
            this.line = line;
            this.fqn = line + ":" + method;
        }

        /** Describes at what moment the event is triggered.
         *
         * @return when this event was triggered
         */
        public When when() {
            return when;
        }

        /** Identifies the real JVM class where this event is triggered.
         *
         * @return real JVM class
         */
        public Class<?> where() {
            return clazz;
        }

        /** Identifies the method where this event is triggered.
         *
         * @return method info with a reference to {@link ClassInfo}
         * @see ClassInfo
         */
        public MethodInfo method() {
            return method;
        }

        /** Identifies a line where this event is triggered.
         *
         * @return line number or {@code -1} if not known
         */
        public int line() {
            return line;
        }

        /**
         * Returns so called <em>fully qualified name</em> of the {@code At}
         * location. The format is {@code line:type.method} where:
         * <ul>
         *   <li>line is a number returned by {@link #line()}</li>
         *   <li>type is a JVM name of the {@link #where()} type - something like {@code Ljava/lang/String;}</li>
         *   <li>method is {@link MethodInfo#name()} followed by the {@link MethodInfo#descriptor()}</li>
         * </ul>
         * This class implements {@link CharSequence}. The value of such a
         * sequence is identical to the value of the string returned by this
         * method.
         *
         * @return fully qualified identification of this location
         */
        @Override
        public String toString() {
            return fqn;
        }

        @Override
        public int length() {
            return fqn.length();
        }

        @Override
        public char charAt(int index) {
            return fqn.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return fqn.subSequence(start, end);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.when);
            hash = 59 * hash + Objects.hashCode(this.method);
            hash = 59 * hash + this.line;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final At other = (At) obj;
            if (this.line != other.line) {
                return false;
            }
            if (this.when != other.when) {
                return false;
            }
            return Objects.equals(this.method, other.method);
        }


    }

    /** Configuration for an Insight callback.
     * Use methods of this class to configure a callback and then register
     * it by calling {@link Builder#call}.
     */
    public final class Builder {
        private final Registry registry;
        private final Class<?> clazz;
        private boolean statements;
        private boolean roots;
        private Predicate<MethodInfo> methodFilter;
        private When when = When.ENTER;

        private Builder(Registry registry, Class<?> clazz) {
            this.registry = registry;
            this.clazz = clazz;
        }

        /** Specify when this callback should be triggered.
         *
         * @param type on enter or on return?
         * @return this builder
         */
        public Builder when(When type) {
            Objects.requireNonNull(type);
            this.when = type;
            return this;
        }

        /** Specify whether this callback should be triggered on method enter/exit.
         *
         * @param roots specify {@code true} to enable tracking "roots"
         * @return this builder
         */
        public Builder roots(boolean roots) {
            this.roots = roots;
            return this;
        }

        /** Specify whether this callback should be triggered on each line/statement.
         *
         * @param statements specify {@code true} to enable tracking "statements"
         * @return this builder
         */
        public Builder statements(boolean statements) {
            this.statements = true;
            return this;
        }

        /** Filter the methods where this callback shall be invoked.
         *
         * @param filter a predicate to decide if a method triggers the callback or not
         * @return this builder
         * @see MethodInfo
         */
        public Builder methods(Predicate<MethodInfo> filter) {
            this.methodFilter = filter;
            return this;
        }

        /** Finishes building a callback. After configuring the builder
         * options, call this mehtod to register the callback accordingly.
         *
         * @param handler a handler to be invoke when an event happens
         */
        public void call(BiConsumer<? super At, Map<String, Object>> handler) {
            registry.register(this, handler);
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
     * @param clazz class that contains the instrumented method
     * @param methodName name of the instrumented method
     * @param methodDescriptor descriptor of the method
     * @param line line number in the method or {@code -1} if it is not specified
     * @return the callsite
     * @throws IllegalArgumentException if the {@code name} isn't recognized
     */
    public static CallSite metafactory(
        MethodHandles.Lookup lkp, String name, MethodType type,
        String when, Class<?> clazz, String methodName, String methodDescriptor,
        int line
    ) {
        try {
            var myLkp = MethodHandles.lookup();
            var rawHandle = myLkp.findStatic(
                JvmInsight.class, name,
                MethodType.methodType(
                    Consumer.class,
                    At.class
                )
            );
            var info = new ClassInfo(
                clazz.getName(), clazz.getModule(), clazz.getClassLoader()
            );
            var method = new MethodInfo(info, methodName, methodDescriptor);
            var at = new At(
                When.valueOf(when.toUpperCase()),
                clazz, method, line
            );
            var consumer = rawHandle
                .bindTo(at);
            return new ConstantCallSite(consumer);
        } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static Consumer<Map<String, Object>> init(At at) {
        var insight = find(at.where().getClassLoader());
        for (var registry : insight.registrations) {
            registry.init.accept(insight.new Builder(registry, at.where()));
        }
        return null;
    }

    private static Consumer<Map<String, Object>> roots(At at) {
        var data = JvmInsightClassData.find(at.where());
        return data.roots(at);
    }

    private static Consumer<Map<String, Object>> statements(At at) {
        var data = JvmInsightClassData.find(at.where());
        return data.statements(at);
    }

    private class Registry implements AutoCloseable {
        private final Predicate<? super ClassInfo> filter;
        private final Map<Class<?>, List<JvmInsightClassData.Convertor>> entries = new LinkedHashMap<>();
        private final Consumer<? super Builder> init;

        private Registry(Predicate<? super ClassInfo> classFilter, Consumer<? super Builder> block) {
            this.filter = classFilter;
            this.init = block;
        }

        @Override
        public synchronized void close() throws Exception {
            for (var entry : entries.entrySet()) {
                var data = JvmInsightClassData.find(entry.getKey());
                for (var reg : entry.getValue()) {
                    data.unregister(reg);
                }
            }
            entries.clear();
        }

        private synchronized void register(Builder bldr, BiConsumer<? super At, Map<String, Object>> handler) {
            var data = JvmInsightClassData.find(bldr.clazz);
            var reg = data.register(bldr.roots, bldr.statements, bldr.when, bldr.methodFilter, handler);
            var list = entries.get(bldr.clazz);
            if (list == null) {
                list = new ArrayList<>();
                entries.put(bldr.clazz, list);
            }
            list.add(reg);
        }
    }
}
