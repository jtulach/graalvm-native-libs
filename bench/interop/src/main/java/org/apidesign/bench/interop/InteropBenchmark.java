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

import java.util.concurrent.TimeUnit;
import org.apidesign.jvm.interop.OtherJvmClassLoader;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Rebuilds {@code jvm-interop}'s own {@code OtherJvmObjectTest.checkString}-style comparison
 * (same target class, wrapped two ways, same operations run on both) as JMH benchmarks, using
 * only public API. Both sides run in this one HotSpot JVM: {@code OtherJvmClassLoader.create(null)}
 * is jvm-interop's in-process mock mode, so no native-image build is needed for this comparison.
 * {@code @Warmup}/{@code @Measurement}/{@code @Fork} are the defaults for a bare
 * {@code java -jar bench-interop.jar} - override with the usual JMH CLI flags when needed.
 */
@State(Scope.Thread)
@Warmup(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class InteropBenchmark {
    private Context localContext;
    private OtherJvmClassLoader loader;
    private Value localClass;
    private Value channelClass;

    @Setup(Level.Trial)
    public void setup() {
        localContext = Context.newBuilder().allowHostAccess(HostAccess.ALL).build();
        // getMember("static") is what exposes static methods as invokable members on the
        // wrapped Class value - mirrors OtherJvmObjectTest.checkString's own local1 setup.
        localClass = localContext.asValue(OtherJvmSample.class).getMember("static");

        loader = OtherJvmClassLoader.create(null);
        channelClass = loader.loadClass(OtherJvmSample.class.getName());
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        loader.close();
        localContext.close();
    }

    @Benchmark
    public String localInvokeString() {
        return localClass.invokeMember("greet", 41L).asString();
    }

    @Benchmark
    public String channelInvokeString() {
        return channelClass.invokeMember("greet", 41L).asString();
    }

    @Benchmark
    public int localArrayAccess() {
        return localClass.invokeMember("numbers", 8).getArrayElement(4).asInt();
    }

    @Benchmark
    public int channelArrayAccess() {
        return channelClass.invokeMember("numbers", 8).getArrayElement(4).asInt();
    }
}
