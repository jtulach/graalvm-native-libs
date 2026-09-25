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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/** Same payload shape as {@link PersistBenchmark}'s {@code Point}, benchmarked via plain {@code
 * java.io.ObjectOutputStream}/{@code ObjectInputStream} as a reference point next to the
 * {@code persist} numbers. */
@State(Scope.Thread)
public class ObjectStreamComparisonBenchmark {
    private SerializablePoint point;
    private byte[] pointBytes;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        point = new SerializablePoint(3, 4);
        pointBytes = serialize(point);
    }

    @Benchmark
    public byte[] serializePoint() throws IOException {
        return serialize(point);
    }

    @Benchmark
    public SerializablePoint deserializePoint() throws IOException, ClassNotFoundException {
        return deserialize(pointBytes);
    }

    private static byte[] serialize(Object obj) throws IOException {
        var bytes = new ByteArrayOutputStream();
        try (var out = new ObjectOutputStream(bytes)) {
            out.writeObject(obj);
        }
        return bytes.toByteArray();
    }

    private static SerializablePoint deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (SerializablePoint) in.readObject();
        }
    }

    public record SerializablePoint(int x, int y) implements Serializable {
    }
}
