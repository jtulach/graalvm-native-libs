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
package org.apidesign.bench.interop;

/**
 * The target of both interop paths in {@link InteropBenchmark}: loaded directly via {@code
 * Context.asValue} on one side, and via {@code OtherJvmClassLoader} (jvm-interop's own mechanism,
 * mock mode) on the other. Kept minimal, matching only what the paired benchmarks exercise.
 */
public final class OtherJvmSample {
    private OtherJvmSample() {
    }

    public static String greet(long n) {
        return "value-" + n;
    }

    public static int[] numbers(int count) {
        var arr = new int[count];
        for (var i = 0; i < count; i++) {
            arr[i] = i * i;
        }
        return arr;
    }
}
