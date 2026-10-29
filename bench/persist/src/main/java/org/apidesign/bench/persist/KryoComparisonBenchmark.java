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

import java.util.concurrent.TimeUnit;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/** Kryo comparison using the same record as {@link ObjectStreamComparisonBenchmark}. */
@State(Scope.Thread)
@Warmup(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class KryoComparisonBenchmark {
    private Kryo kryo;
    private ObjectStreamComparisonBenchmark.SerializablePoint point;
    private Output output;
    private Input input;
    private byte[] pointBytes;

    @Setup(Level.Trial)
    public void setup() {
        kryo = new Kryo();
        kryo.setRegistrationRequired(true);
        kryo.register(ObjectStreamComparisonBenchmark.SerializablePoint.class);
        point = new ObjectStreamComparisonBenchmark.SerializablePoint(3, 4);
        output = new Output(32, -1);
        kryo.writeObject(output, point);
        pointBytes = output.toBytes();
        var decoded = kryo.readObject(new Input(pointBytes), ObjectStreamComparisonBenchmark.SerializablePoint.class);
        if (!point.equals(decoded)) {
            throw new IllegalStateException("Kryo round trip changed the point");
        }
        input = new Input(pointBytes);
    }

    @Benchmark
    public byte[] serializePoint() {
        output.reset();
        kryo.writeObject(output, point);
        return output.toBytes();
    }

    @Benchmark
    public ObjectStreamComparisonBenchmark.SerializablePoint deserializePoint() {
        input.setBuffer(pointBytes);
        return kryo.readObject(input, ObjectStreamComparisonBenchmark.SerializablePoint.class);
    }
}