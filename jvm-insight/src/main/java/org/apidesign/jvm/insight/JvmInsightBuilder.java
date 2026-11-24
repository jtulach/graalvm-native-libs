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
import java.util.Objects;
import java.util.function.BiConsumer;

/** Temporary builder to build an instrumentation. The builder is only
 * usable until its {@link #dispose()} method is called.
 */
final class JvmInsightBuilder extends JvmInsight.Builder {

    private final Class<?> clazz;
    private boolean statements;
    private boolean roots;
    private JvmInsight.When when = JvmInsight.When.ENTER;
    private final JvmInsight.MethodInfo method;
    /** @GuardedBy("this") */
    private List<JvmInsightClassData.Convertor> convertors = new ArrayList<>();

    JvmInsightBuilder(JvmInsight.MethodInfo method, Class<?> clazz) {
        this.method = method;
        this.clazz = clazz;
    }

    /** Specify when this callback should be triggered.
     *
     * @param type on enter or on return?
     * @return this builder
     */
    @Override
    public JvmInsight.Builder when(JvmInsight.When type) {
        Objects.requireNonNull(type);
        this.when = type;
        return this;
    }

    /** Specify whether this callback should be triggered on method enter/exit.
     *
     * @param roots specify {@code true} to enable tracking "roots"
     * @return this builder
     */
    @Override
    public JvmInsight.Builder roots(boolean roots) {
        this.roots = roots;
        return this;
    }

    /** Specify whether this callback should be triggered on each line/statement.
     *
     * @param statements specify {@code true} to enable tracking "statements"
     * @return this builder
     */
    @Override
    public JvmInsight.Builder statements(boolean statements) {
        this.statements = true;
        return this;
    }

    /** Finishes building a callback. After configuring the builder
     * options, call this mehtod to register the callback accordingly.
     *
     * @param handler a handler to be invoke when an event happens
     * @return an internal handle representing this callback,
     *    {@link AutoCloseable#close()} it
     *    to disassociate call registered by this method
     */
    @Override
    public void call(BiConsumer<? super JvmInsight.At, Map<String, Object>> handler) {
        var data = JvmInsightClassData.find(clazz);
        var conv = data.register(roots, statements, when, method, handler);
        synchronized (this) {
            convertors.add(conv);
        }
    }

    /**
     * Disposes this builder as no longer usable. Sets the {@code convertors}
     * field to {@code null} to avoid any further reasonable usage.
     *
     * @return list of all registered convertors
     */
    final synchronized List<JvmInsightClassData.Convertor> dispose() {
        Objects.requireNonNull(convertors);
        var prev = convertors;
        convertors = null;
        return prev;
    }

}
