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

/** Classloader to allow loading patched {@link Factorial} class.
 * Our class as well as patched {@link Factorial} class need to have a
 * reference to the same {@link JvmInsight} class. When running unit tests
 * all three classes are loaded by the same classloader. Creating a child
 * classloader to load {@link Factorial} requires us to mask out that
 * class. That's what the {@link AvoidingLoader} does.
 */
final class AvoidClassLoader extends ClassLoader {

    private final Class<?> avoid;

    AvoidClassLoader(Class<?> avoid) {
        super(avoid.getClassLoader());
        this.avoid = avoid;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (avoid.getName().equals(name)) {
            throw new ClassNotFoundException(name);
        }
        return super.loadClass(name, resolve);
    }
    
}
