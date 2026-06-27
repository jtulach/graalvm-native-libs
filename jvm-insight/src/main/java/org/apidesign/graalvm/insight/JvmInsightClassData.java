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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

final class JvmInsightClassData {
    private static final Provider PROVIDER = new Provider();
    private static final JvmInsightClassData GLOBAL = new JvmInsightClassData(null);
    private final Map<JvmInsight.Builder.When, List<Convertor>> roots = new EnumMap<>(JvmInsight.Builder.When.class);
    private final Map<JvmInsight.Builder.When, List<Convertor>> statements = new EnumMap<>(JvmInsight.Builder.When.class);

    private JvmInsightClassData(Class<?> type) {
    }

    static final JvmInsightClassData find(Class<?> type) {
        if (type == null) {
            return GLOBAL;
        } else {
            return PROVIDER.get(type);
        }
    }

    final Consumer<Map<String, Object>> roots(JvmInsight.Builder.When when, String fqn) {
        return dispatcher(roots.get(when), GLOBAL.roots.get(when), fqn);
    }

    final Consumer<Map<String, Object>> statements(JvmInsight.Builder.When when, String fqn) {
        return dispatcher(statements.get(when), GLOBAL.statements.get(when), fqn);
    }

    private static Consumer<Map<String, Object>> dispatcher(List<Convertor> list1, List<Convertor> list2, String fqn) {
        if (list1 == null && list2 == null) {
            return null;
        } else {
            return (value) -> {
                if (list1 != null) for (var c : list1) {
                    c.accept(fqn, value);
                }
                if (list2 != null) for (var c : list2) {
                    c.accept(fqn, value);
                }
            };
        }
    }

    synchronized Convertor register(
        boolean roots, boolean statements,
        JvmInsight.Builder.When when, Pattern methodFilter,
        BiConsumer<String, Map<String, Object>> handler
    ) {
        var c = new Convertor(roots, statements, when, handler);
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

    static class Convertor implements BiConsumer<String, Map<String, Object>> {
        private final boolean roots;
        private final boolean statements;
        private final JvmInsight.Builder.When when;
        private final BiConsumer<String, Map<String, Object>> handler;

        private Convertor(boolean roots, boolean statements, JvmInsight.Builder.When when, BiConsumer<String, Map<String, Object>> handler) {
            this.handler = handler;
            this.roots = roots;
            this.when = when;
            this.statements = statements;
        }

        @Override
        public void accept(String t, Map<String, Object> data) {
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
