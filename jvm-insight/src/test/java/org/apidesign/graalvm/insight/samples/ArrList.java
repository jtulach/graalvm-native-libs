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
package org.apidesign.graalvm.insight.samples;

import java.util.AbstractList;
import java.util.Objects;
import java.util.function.Consumer;

public class ArrList<E> extends AbstractList<E> {
    private final E[] arr;

    public ArrList(E[] arr) {
        this.arr = arr;
    }

    @Override
    public E get(int index) {
        return arr[index];
    }

    @Override
    public int size() {
        return arr.length;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        for (var e : arr) {
            action.accept(e);
        }
    }
}
