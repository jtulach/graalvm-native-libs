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
package org.apidesign.bench.persist;

import org.apidesign.jvm.persist.Persistable;

/**
 * Payload shapes benchmarked in this package. Self-annotated with {@link Persistable}, which
 * makes {@code persist-dsl}'s annotation processor generate this package's own {@code
 * Persistables} pool ({@link PersistBenchmark} uses it directly, same-package, no import needed).
 */
public final class BenchPayloads {
    private BenchPayloads() {
    }

    @Persistable(id = 950001)
    public record Point(int x, int y) {
    }

    @Persistable(id = 950002)
    public record Line(Point from, Point to, String label) {
    }
}
