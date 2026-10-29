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
package org.apidesign.jvm.persist;

import java.util.Objects;

sealed class PerMemoryReference<T> extends Persistance.Reference<T>
    permits PerMemoryReference.Deferred {
  static final Persistance.Reference<?> NULL = new PerMemoryReference<>(null);
  private final T value;

  PerMemoryReference(T obj) {
    this.value = obj;
  }

  final T value() {
    return value;
  }

  @Override
  boolean isDeferredWrite() {
    return Deferred.class == getClass();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof PerMemoryReference<?> that) {
      return Objects.equals(value, that.value);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return Objects.toString(value);
  }

  static final class Deferred<T> extends PerMemoryReference<T> {
    Deferred(T obj) {
      super(obj);
    }
  }
}
