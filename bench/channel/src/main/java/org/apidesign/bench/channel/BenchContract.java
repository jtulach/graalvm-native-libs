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
package org.apidesign.bench.channel;

import java.util.function.Function;
import org.apidesign.jvm.channel.Channel;

/**
 * The single operation measured in all three of {@code --mode=native|mock|hotspot}: sum of
 * squares from {@code 1} to {@code n}. Deliberately cheap to compute, so a round trip's cost is
 * dominated by the transport (JNI/cross-isolate, in-process mock, or nothing at all), not by the
 * arithmetic itself.
 */
public final class BenchContract {
    private BenchContract() {}

    /** The reference implementation: what {@code --mode=hotspot} calls directly, and what the
     * other two modes' answers are checked against.
     */
    public static long referenceCompute(long n) {
        var acc = 0L;
        for (var i = 1L; i <= n; i++) {
            acc += i * i;
        }
        return acc;
    }

    /** The message sent over the channel in {@code --mode=native} and {@code --mode=mock}. */
    public record ComputeRequest(long n) implements Function<Channel<?>, Long> {
        @Override
        public Long apply(Channel<?> channel) {
            return referenceCompute(n);
        }
    }
}
