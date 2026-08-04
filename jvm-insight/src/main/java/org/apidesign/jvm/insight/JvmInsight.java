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

import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** {@link JvmInsight} allows advanced instrumentation to be applied to
 * classes running inside of the JVM.
 * <ul>
 *   <li>Use {@link JvmInsight#find} method to obtain instance of the JVM Insight</li>
 *   <li>Then use its {@link JvmInsight#onMethod} method to setup up an
 *      Insight hook</li>
 *   <li>All methods of newly loaded classes are going to be sent into
 *      the hook and it configure their instrumentation</li>
 * </ul>
 * There is also {@link JvmInsight#onClass} method that can be useful to
 * observe classes being loaded into the JVM or registering hooks for all
 * methods of such classes.
 */
public final class JvmInsight  {
    /** Default JVM Insight to be used for the whole JVM.
     */
    private static final JvmInsight DEFAULT = new JvmInsight(
        JvmInsightInitializer.getInstrumentation()
    );
    private final Object instr;
    /** All active hooks created by this JvmInsight instance */
    private final List<JvmInsightHook> hooks = new CopyOnWriteArrayList<>();

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
     * Applies new Insights to the running JVM on a per method basis.
     *
     * @param block block that receives an instance of a {@link ClassInfo}) and
     *   a {@link JvmInsight.Builder}
     *   factory. The block can use the builder to configure its JVM Insights
     *   to be applied to all methods of the given class. The block
     *   may be invoked multiple times (even for the same class). It
     *   is expected the block behaves the same for the invocation
     *   with the same argument.
     *
     * @return a handle that can be {@link AutoCloseable#close()} when
     *   these insights are to be disabled
     */
    public AutoCloseable onClass(BiConsumer<ClassInfo, Builder> block) {
        var listener = new JvmInsightHook(this, null, block);
        hooks.add(listener);
        return listener;
    }

    /**
     * Applies new Insights to the running JVM on a per method basis.
     *
     * @param block block that receives an instance of a {@link MethodInfo}
     *   (that belongs to {@link ClassInfo}) and a {@link JvmInsight.Builder}
     *   factory. The block can use the builder to configure its JVM Insights
     *   to be applied to the given method. The block
     *   may be invoked multiple times (even for the same method). It
     *   is expected the block behaves the same for the invocation
     *   with the same argument.
     *
     * @return a handle that can be {@link AutoCloseable#close()} when
     *   these insights are to be disabled
     */
    public AutoCloseable onMethod(BiConsumer<MethodInfo, Builder> block) {
        var listener = new JvmInsightHook(this, block, null);
        hooks.add(listener);
        return listener;
    }

    /**
     * Creates a {@link JvmInsight}-ready classloader. Classes loaded by
     * this {@link ClassLoader} are patched to be ready for {@link #onMethod}-ing
     * JVM Insights, if there is a hook registered for their methods.
     *
     * @param parent the parent classloader to use or {@code null}
     * @param cp set of classpath elements to load classes from
     * @return the JVM Insights ready classloader
     */
    public static ClassLoader createLoader(ClassLoader parent, URL... cp) {
        var loader = new JvmInsightLoader(parent, cp);
        return loader;
    }

    /** Info about class (to be) loaded. In addition to providing various
     * info about the class to be loaded, it also implements a {@link CharSequence}
     * representing the same content of {@link #name()}, so filters
     * can work with generic type when just a name is enough to filter.
     */
    public final static class ClassInfo implements CharSequence, Iterable<MethodInfo> {
        private final String name;
        private final Module module;
        private final ClassLoader loader;
        private final byte[] code;
        /** @GuardedBy("this") */
        private ClassModel model;
        /** @GuardedBy("this") */
        private List<MethodInfo> methods;

        ClassInfo(String name, Module module, ClassLoader loader, byte[] code) {
            Objects.requireNonNull(code);
            this.name = name.replace('.', '/');
            this.module = module;
            this.loader = loader;
            this.code = code;
        }

        ClassInfo(Class<?> clazz) {
            this(clazz.getName(), clazz.getModule(), clazz.getClassLoader(), new byte[0]);
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

        /**
         * Iterator over all {@link MethodInfo} elements provided by this class.
         *
         * @return the parsed model of the class
         */
        @Override
        public synchronized Iterator<MethodInfo> iterator() {
            if (this.methods == null) {
                var models = classModel().elementStream().mapMulti((ClassElement t, Consumer<MethodModel> sink) -> {
                    if (t instanceof MethodModel m) {
                        sink.accept(m);
                    }
                });
                var infos = models.map(e -> {
                    var methodName = e.methodName().stringValue();
                    var descriptor = e.methodType().stringValue();
                    return new MethodInfo(this, methodName, descriptor);
                });
                this.methods = infos.toList();
            }
            return this.methods.iterator();
        }

        synchronized final ClassModel classModel() {
            if (model == null) {
                var file = ClassFile.of();
                model = file.parse(code);
            }
            return model;
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
            for (var r : insight.hooks) {
                if (r.instrumentClass(this)) {
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

    /** Info about a method (being) defined. In addition to providing various
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

        /** Fully qualified name of the method.
         * Includes the {@link ClassInfo#jvmName()} of the class,
         * the method {@link MethodInfo#name()} and the {@link MethodInfo#descriptor()}.
         * For example method {@link String#length()}
         * would be represented as {@code "Ljava/lang/String;.length()I"} string.
         *
         * @return fully qualified name identifying the method
         */
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
        /** Deliver the event before the given location is executed. */
        ENTER,
        /** Deliver the event after the given location is executed. */
        RETURN;
    }

    /** Identifies a location of a JVM Insight event. It carries individual
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

    /** Configuration of a JVM Insight callback.
     * Use methods of this class to configure a callback and then register
     * it by calling {@link Builder#call}.
     */
    public static abstract class Builder {
        /** not available to public */
        Builder() {
        }

        /** Specify when this callback should be triggered.
         *
         * @param type on enter or on return?
         * @return this builder
         */
        public abstract Builder when(When type);

        /** Specify whether this callback should be triggered on method enter/exit.
         *
         * @param roots specify {@code true} to enable tracking "roots"
         * @return this builder
         */
        public abstract Builder roots(boolean roots);

        /** Specify whether this callback should be triggered on each line/statement.
         *
         * @param statements specify {@code true} to enable tracking "statements"
         * @return this builder
         */
        public abstract Builder statements(boolean statements);

        /** Finishes building a callback. After configuring the builder
         * options, call this mehtod to register the callback accordingly.
         *
         * @param handler a handler to be invoked when an event happens
         */
        public abstract void call(BiConsumer<? super At, Map<String, Object>> handler);
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
            var method = JvmInsightClassData.find(clazz).method(methodName, methodDescriptor);
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
        var clazz = at.where();
        var insight = find(clazz.getClassLoader());
        var classInfo = JvmInsightClassData.find(clazz).info();
        for (var registry : insight.hooks) {
            registry.register(classInfo, clazz);
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

    final void removeHook(JvmInsightHook hook) {
        this.hooks.remove(hook);
    }
}
