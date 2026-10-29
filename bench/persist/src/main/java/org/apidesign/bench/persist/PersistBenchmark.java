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

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apidesign.bench.persist.BenchPayloads.Line;
import org.apidesign.bench.persist.BenchPayloads.Point;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/** Conventional JMH benchmarks over {@code persist}'s serialize/deserialize round trip.
 * {@code @Warmup}/{@code @Measurement}/{@code @Fork} here are the defaults for a bare
 * {@code java -jar bench-persist.jar} - CI and anyone else can still override them with the
 * usual {@code -wi}/{@code -i}/{@code -f}/{@code -w}/{@code -r} flags. */
@State(Scope.Thread)
@Warmup(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class PersistBenchmark {
    private Point point;
    private Line line;
    private byte[] pointBytes;
    private byte[] lineBytes;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        point = new Point(3, 4);
        line = new Line(new Point(0, 0), new Point(10, 10), "diagonal");
        pointBytes = Persistables.POOL.write(point);
        lineBytes = Persistables.POOL.write(line);
    }

    @Benchmark
    public byte[] serializePoint() throws IOException {
        return Persistables.POOL.write(point);
    }

    @Benchmark
    public Point deserializePoint() throws IOException {
        return Persistables.POOL.read(pointBytes).get(Point.class);
    }

    @Benchmark
    public byte[] serializeLine() throws IOException {
        return Persistables.POOL.write(line);
    }

    @Benchmark
    public Line deserializeLine() throws IOException {
        return Persistables.POOL.read(lineBytes).get(Line.class);
    }
}
