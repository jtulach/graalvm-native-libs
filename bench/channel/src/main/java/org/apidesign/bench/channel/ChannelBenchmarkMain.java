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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.LongUnaryOperator;
import org.apidesign.jvm.channel.Channel;
import org.apidesign.jvm.channel.JVM;
import org.graalvm.nativeimage.ImageInfo;

/**
 * The single class run in all three benchmark modes ({@code --mode=native|mock|hotspot}). Each
 * mode calls the exact same private {@link #timeLoop} method against a different backend for
 * {@link BenchContract}'s {@code ComputeRequest}, so the resulting ns/op numbers are directly
 * comparable and the ratios between them mean something.
 *
 * <p>No JMH here on purpose: {@code --mode=native} only exists as a compiled native-image
 * executable (see {@link Channel#create}), and JMH's own forking/discovery machinery is not
 * reliable inside one. Using one hand-rolled methodology across all three modes keeps them
 * comparable, rather than mixing JMH-measured numbers with hand-rolled ones.
 */
public final class ChannelBenchmarkMain {
    private ChannelBenchmarkMain() {
    }

    public static void main(String[] args) throws Exception {
        var opts = Options.parse(args);
        var result = switch (opts.mode()) {
            case "native" -> runNative(opts);
            case "mock" -> runMock(opts);
            case "hotspot" -> runHotspot(opts);
            default -> throw new IllegalArgumentException(
                    "Unknown --mode=" + opts.mode() + " (expected native, mock, or hotspot)");
        };
        result.report(opts);
    }

    /** Real cross-VM mechanism: launches a second HotSpot JVM, then measures steady-state
     * round-trip cost against that one already-launched JVM (launch cost is logged separately,
     * once, and never mixed into the per-call samples). */
    private static Result runNative(Options opts) throws Exception {
        if (!ImageInfo.inImageCode()) {
            throw new IllegalStateException("--mode=native only runs from a compiled native image");
        }
        var javaHome = System.getenv("JAVA_HOME");
        if (javaHome == null) {
            throw new IllegalStateException("--mode=native needs the JAVA_HOME environment variable");
        }
        var jvm = JVM.create(new File(javaHome), "-Djava.class.path=" + classpath());
        var launchStart = System.nanoTime();
        var channel = Channel.create(jvm, Conf.class);
        var launchNanos = System.nanoTime() - launchStart;
        var samples = timeLoop(opts, n -> channel.execute(Long.class, new BenchContract.ComputeRequest(n)));
        return new Result("native", samples, launchNanos);
    }

    /** In-process stand-in: same channel API, both sides in this one JVM. */
    private static Result runMock(Options opts) {
        var channel = Channel.create(null, Conf.class);
        var samples = timeLoop(opts, n -> channel.execute(Long.class, new BenchContract.ComputeRequest(n)));
        return new Result("mock", samples, -1);
    }

    /** No jvm-channel involvement at all: the reference computation, called directly. */
    private static Result runHotspot(Options opts) {
        var samples = timeLoop(opts, BenchContract::referenceCompute);
        return new Result("hotspot", samples, -1);
    }

    private static long[] timeLoop(Options opts, LongUnaryOperator op) {
        var expected = BenchContract.referenceCompute(opts.n());
        for (var i = 0; i < opts.warmup(); i++) {
            checkResult(expected, op.applyAsLong(opts.n()));
        }
        var samples = new long[opts.iterations()];
        for (var i = 0; i < opts.iterations(); i++) {
            var t0 = System.nanoTime();
            var r = op.applyAsLong(opts.n());
            samples[i] = System.nanoTime() - t0;
            checkResult(expected, r);
        }
        return samples;
    }

    private static void checkResult(long expected, long actual) {
        if (expected != actual) {
            throw new IllegalStateException("Wrong result: expected " + expected + " but got " + actual);
        }
    }

    private static String classpath() {
        var sb = new StringBuilder();
        for (var e : new String[] {"target/classes", "target/dependency"}) {
            if (sb.length() > 0) {
                sb.append(File.pathSeparator);
            }
            sb.append(e.replace('/', File.separatorChar));
        }
        return sb.toString();
    }

    /** Parsed {@code --mode=}/{@code --n=}/{@code --warmup=}/{@code --iterations=}/{@code --out=}
     * command line arguments. */
    private record Options(String mode, long n, int warmup, int iterations, String out) {
        static Options parse(String[] args) {
            var mode = "mock";
            var n = 10L;
            var warmup = 2000;
            var iterations = 5000;
            String out = null;
            for (var a : args) {
                if (a.startsWith("--mode=")) {
                    mode = a.substring("--mode=".length());
                } else if (a.startsWith("--n=")) {
                    n = Long.parseLong(a.substring("--n=".length()));
                } else if (a.startsWith("--warmup=")) {
                    warmup = Integer.parseInt(a.substring("--warmup=".length()));
                } else if (a.startsWith("--iterations=")) {
                    iterations = Integer.parseInt(a.substring("--iterations=".length()));
                } else if (a.startsWith("--out=")) {
                    out = a.substring("--out=".length());
                } else {
                    throw new IllegalArgumentException("Unknown argument: " + a);
                }
            }
            return new Options(mode, n, warmup, iterations, out);
        }
    }

    /** One JSON line (or file, via {@code --out}) with mean/median/stddev/min/max over the
     * measured samples, plus the one-shot launch cost when {@code --mode=native} measured one. */
    private record Result(String mode, long[] samples, long launchNanos) {
        void report(Options opts) throws IOException {
            var sorted = samples.clone();
            Arrays.sort(sorted);
            var sum = 0.0;
            for (var s : sorted) {
                sum += s;
            }
            var mean = sum / sorted.length;
            var variance = 0.0;
            for (var s : sorted) {
                variance += (s - mean) * (s - mean);
            }
            var stddev = Math.sqrt(variance / sorted.length);

            var json = new StringBuilder("{");
            json.append("\"mode\":\"").append(mode).append("\",");
            json.append("\"n\":").append(opts.n()).append(',');
            json.append("\"iterations\":").append(sorted.length).append(',');
            json.append("\"meanNanos\":").append(mean).append(',');
            json.append("\"medianNanos\":").append(sorted[sorted.length / 2]).append(',');
            json.append("\"stddevNanos\":").append(stddev).append(',');
            json.append("\"minNanos\":").append(sorted[0]).append(',');
            json.append("\"maxNanos\":").append(sorted[sorted.length - 1]);
            if (launchNanos >= 0) {
                json.append(",\"launchNanos\":").append(launchNanos);
            }
            json.append('}');

            if (opts.out() != null) {
                Files.writeString(Path.of(opts.out()), json.toString());
            } else {
                System.out.println(json);
            }
        }
    }
}
