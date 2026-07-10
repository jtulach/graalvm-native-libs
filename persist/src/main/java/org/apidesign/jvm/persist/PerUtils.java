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

import java.lang.System.Logger;

final class PerUtils {
  private PerUtils() {}

  static final Logger LOG = System.getLogger(Persistance.class.getPackageName());

  @SuppressWarnings("unchecked")
  static <E extends Throwable> E raise(Class<E> clazz, Throwable t) throws E {
    throw (E) t;
  }
}
