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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * JVM Insight hook created when {@link JvmInsight#onClass}
 * or {@link JvmInsight#onMethod} is used to register an insight.
 */
final class JvmInsightHook implements AutoCloseable {
    private final JvmInsight insight;
    private final BiConsumer<JvmInsight.MethodInfo, JvmInsight.Builder> onMethod;
    private final BiConsumer<JvmInsight.ClassInfo, JvmInsight.Builder> onClass;
    private final List<JvmInsightClassData.Convertor> convertors = new ArrayList<>();

    JvmInsightHook(
        JvmInsight insight,
        BiConsumer<JvmInsight.MethodInfo, JvmInsight.Builder> method,
        BiConsumer<JvmInsight.ClassInfo, JvmInsight.Builder> clazz
    ) {
        this.insight = insight;
        this.onMethod = method;
        this.onClass = clazz;
    }

    public Boolean instrumentClass(JvmInsight.ClassInfo info) {
        var isAppliedBuilder = new JvmInsight.Builder() {
            private boolean activated;

            @Override
            public JvmInsight.Builder when(JvmInsight.When type) {
                return this;
            }

            @Override
            public JvmInsight.Builder roots(boolean roots) {
                return this;
            }

            @Override
            public JvmInsight.Builder statements(boolean statements) {
                return this;
            }

            @Override
            public void call(BiConsumer<? super JvmInsight.At, Map<String, Object>> handler) {
                activated = true;
            }
        };
        if (onClass != null) {
            onClass.accept(info, isAppliedBuilder);
            if (isAppliedBuilder.activated) {
                return true;
            }
        }
        if (onMethod != null) {
            for (org.apidesign.jvm.insight.JvmInsight.MethodInfo methodInfo : info) {
                onMethod.accept(methodInfo, isAppliedBuilder);
                if (isAppliedBuilder.activated) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        insight.removeHook(this);
        for (var c : convertors) {
            c.close();
        }
    }

    final void register(JvmInsight.ClassInfo classInfo, Class<?> clazz) {
        if (onMethod != null) {
            for (var t : classInfo) {
                var bldr = new JvmInsightBuilder(t, clazz);
                onMethod.accept(t, bldr);
                convertors.addAll(bldr.dispose());
            }
        }
        if (onClass != null) {
            var bldr = new JvmInsightBuilder(null, clazz);
            onClass.accept(classInfo, bldr);
            convertors.addAll(bldr.dispose());
        }
    }
}
