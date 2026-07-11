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

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

/**
 * Annotation for an automatic persistance of a class. Use to generate implementation and
 * registration of {@link Persistance} subclass to read and write simple records and case classes:
 * <br>
 *
 * {@snippet file="org/apidesign/jvm/persist/PersistanceTest.java" region="annotation"}
 */
@Target(ElementType.TYPE)
@Repeatable(Persistable.Group.class)
public @interface Persistable {

  /**
   * The class to generate {@link Persistance} for. If the value is omitted then the code is
   * generated for the class that is annotated by this annotation. Example of multiple
   * {@code @Persistable} annotations on a single element. <br>
   *
   * {@snippet file="org/apidesign/jvm/persist/PersistanceTest.java" region="annotation"}
   *
   * <br>
   * Example of self annotated class: <br>
   *
   * {@snippet file="org/apidesign/jvm/persist/PersistanceTest.java" region="self-annotation"}
   *
   * @return the class to generate read/write persistance code for
   */
  Class<?> clazz() default Object.class;

  /**
   * ID of the class. Each registered {@link Persistance} sub class must have a unique ID. This
   * attribute specifies it for the generated code. When serialization format changes (for example
   * by changing the number or type of constructor arguments), change also the ID.
   *
   * @return unique ID for the persisted {@link #clazz()}
   */
  int id();

  /**
   * Should the generated code use {@link Persistance.Output#writeInline} or not. By default all
   * {@code final} or <em>sealed</em> classes are inlined. Inlining is however not very helpful when
   * a single object is shared between multiple other objects.
   *
   * @return allow or disallow inlining
   */
  boolean allowInlining() default true;

  /** Multiple {@link Persistable} annotations. */
  @Target(ElementType.TYPE)
  public @interface Group {
    /**
     * The array of {@link Persistable} annotations.
     *
     * @return the array of annotations.
     */
    Persistable[] value();
  }
}
