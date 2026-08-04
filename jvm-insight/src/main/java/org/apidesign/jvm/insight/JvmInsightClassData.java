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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.apidesign.jvm.insight.JvmInsight.At;

final class JvmInsightClassData {
    private static final Map<JvmInsight.ClassInfo, JvmInsight.ClassInfo> CACHE = new ConcurrentHashMap<>();
    private static final Provider PROVIDER = new Provider();
    private final JvmInsight.ClassInfo info;
    private final Map<JvmInsight.When, List<Convertor>> roots = new EnumMap<>(JvmInsight.When.class);
    private final Map<JvmInsight.When, List<Convertor>> statements = new EnumMap<>(JvmInsight.When.class);

    private JvmInsightClassData(Class<?> type, JvmInsight.ClassInfo info) {
        this.info = info;
    }

    static final JvmInsightClassData find(Class<?> type) {
        Objects.requireNonNull(type);
        return PROVIDER.get(type);
    }

    static final void keep(JvmInsight.ClassInfo info) {
        CACHE.put(info, info);
    }

    final JvmInsight.ClassInfo info() {
        return info;
    }

    final JvmInsight.MethodInfo method(String name, String descriptor) {
        for (var m : info) {
            if (m.name().equals(name) && m.descriptor().equals(descriptor)) {
                return m;
            }
        }
        return null;
    }

    final Consumer<Map<String, Object>> roots(JvmInsight.At at) {
        var local = roots.get(at.when());
        return dispatcher(local, at);
    }

    final Consumer<Map<String, Object>> statements(JvmInsight.At at) {
        var local = statements.get(at.when());
        return dispatcher(local, at);
    }

    private static Consumer<Map<String, Object>> dispatcher(List<Convertor> list1, JvmInsight.At fqn) {
        if (list1 == null) {
            return null;
        } else {
            return (value) -> {
                for (var c : list1) {
                    c.accept(fqn, value);
                }
            };
        }
    }

    synchronized Convertor register(
        boolean roots, boolean statements,
        JvmInsight.When when, JvmInsight.MethodInfo methodOrNull,
        BiConsumer<? super At, Map<String, Object>> handler
    ) {
        var c = new Convertor(this, roots, statements, when, methodOrNull, handler);
        if (roots) {
            var prev = this.roots.get(c.when);
            if (prev == null) {
                prev = new CopyOnWriteArrayList<>();
                this.roots.put(c.when, prev);
            }
            prev.add(c);
        }
        if (statements) {
            var prev = this.statements.get(c.when);
            if (prev == null) {
                prev = new CopyOnWriteArrayList<>();
                this.statements.put(c.when, prev);
            }
            prev.add(c);
        }
        return c;
    }

    synchronized void unregister(Convertor c) {
        if (c.roots) {
            var prev = roots.get(c.when);
            prev.remove(c);
        }
        if (c.statements) {
            var prev = statements.get(c.when);
            prev.remove(c);
        }
    }

    private static final class Provider extends ClassValue<JvmInsightClassData> {
        @Override
        protected JvmInsightClassData computeValue(Class<?> type) {
            var template = new JvmInsight.ClassInfo(type);
            var real = CACHE.remove(template);
            Objects.requireNonNull(real, "Cannot find " + template + " in\n" + CACHE);
            return new JvmInsightClassData(type, real);
        }
    }

    static class Convertor implements AutoCloseable {
        private final JvmInsightClassData data;
        private final boolean roots;
        private final boolean statements;
        private final JvmInsight.When when;
        private final JvmInsight.MethodInfo methodOrNull;
        private final BiConsumer<? super At, Map<String, Object>> handler;

        private Convertor(
            JvmInsightClassData data,
            boolean roots, boolean statements,
            JvmInsight.When when,
            JvmInsight.MethodInfo methodFilter,
            BiConsumer<? super At, Map<String, Object>> handler
        ) {
            this.data = data;
            this.handler = handler;
            this.roots = roots;
            this.when = when;
            this.methodOrNull = methodFilter;
            this.statements = statements;
        }

        public void accept(At t, Map<String, Object> data) {
            if (methodOrNull != null && methodOrNull != t.method()) {
                return;
            }
            var names = (String[]) data.get("names");
            var values = (Object[]) data.get("values");
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

        @Override
        public void close() throws Exception {
            data.unregister(this);
        }
    }
}
