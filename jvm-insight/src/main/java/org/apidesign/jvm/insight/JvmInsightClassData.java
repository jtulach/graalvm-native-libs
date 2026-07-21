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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.apidesign.jvm.insight.JvmInsight.At;

final class JvmInsightClassData {
    private static final Provider PROVIDER = new Provider();
    private final Map<JvmInsight.When, List<Convertor>> roots = new EnumMap<>(JvmInsight.When.class);
    private final Map<JvmInsight.When, List<Convertor>> statements = new EnumMap<>(JvmInsight.When.class);

    private JvmInsightClassData(Class<?> type) {
    }

    static final JvmInsightClassData find(Class<?> type) {
        Objects.requireNonNull(type);
        return PROVIDER.get(type);
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
        JvmInsight.When when, Predicate<JvmInsight.MethodInfo> methodFilter,
        BiConsumer<? super At, Map<String, Object>> handler
    ) {
        var c = new Convertor(roots, statements, when, methodFilter, handler);
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
            return new JvmInsightClassData(type);
        }
    }

    static class Convertor implements BiConsumer<At, Map<String, Object>> {
        private final boolean roots;
        private final boolean statements;
        private final JvmInsight.When when;
        private final Predicate<JvmInsight.MethodInfo> methodFilter;
        private final BiConsumer<? super At, Map<String, Object>> handler;

        private Convertor(
            boolean roots, boolean statements,
            JvmInsight.When when,
            Predicate<JvmInsight.MethodInfo> methodFilter,
            BiConsumer<? super At, Map<String, Object>> handler
        ) {
            this.handler = handler;
            this.roots = roots;
            this.when = when;
            this.methodFilter = methodFilter;
            this.statements = statements;
        }

        @Override
        public void accept(At t, Map<String, Object> data) {
            if (methodFilter != null && !methodFilter.test(t.method())) {
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
    }
}
