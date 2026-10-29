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

// Single-source-file program (run via `java BenchAggregate.java <repoRoot> <outFile>`, no build
// step). Runs bench/channel's three modes a handful of times each (process-level repeats, since
// there is no JMH fork machinery here to catch run-to-run variance), reads bench/interop's JMH
// JSON output, and records native-image binary sizes - emitting one customSmallerIsBetter JSON
// array that github-action-benchmark can track.

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class BenchAggregate {

    public static void main(String[] args) throws Exception {
        var repoRoot = Path.of(args.length > 0 ? args[0] : ".").toAbsolutePath();
        var outFile = Path.of(args.length > 1 ? args[1] : "bench-aggregate.json");
        // PR job never builds the native-image executable (that's the slow part the fast/PR
        // cadence deliberately excludes) - pass --no-native there to skip it and the ratios that
        // need it, rather than failing on a missing executable.
        var includeNative = args.length <= 2 || !args[2].equals("--no-native");

        var entries = new ArrayList<Entry>();
        addChannelEntries(entries, repoRoot, includeNative);
        addInteropRatios(entries, repoRoot);
        addImageSizes(entries, repoRoot);

        Files.writeString(outFile, toJson(entries));
        System.out.println("Wrote " + entries.size() + " entries to " + outFile);
    }

    // --- bench/channel: native, mock, hotspot, and the ratios between them ---

    private static void addChannelEntries(List<Entry> entries, Path repoRoot, boolean includeNative)
            throws IOException, InterruptedException {
        var channelDir = repoRoot.resolve("bench/channel");
        var jar = channelDir.resolve("target/bench-channel.jar");

        var mockMedian = medianOf(runChannelMode(channelDir, jar, "mock", 5, 1000, 3000));
        var hotspotMedian = medianOf(runChannelMode(channelDir, jar, "hotspot", 5, 1000, 3000));
        entries.add(new Entry("jvm-channel: mock round trip", "ns", mockMedian));
        entries.add(new Entry("jvm-channel: hotspot baseline", "ns", hotspotMedian));
        entries.add(new Entry("jvm-channel: mock/hotspot ratio", "x", mockMedian / hotspotMedian));

        if (includeNative) {
            var nativeExe = channelDir.resolve("target/bench-channel");
            var nativeMedian = medianOf(runChannelMode(channelDir, nativeExe, "native", 5, 100, 300));
            entries.add(new Entry("jvm-channel: native (NI+HotSpot) round trip", "ns", nativeMedian));
            entries.add(new Entry("jvm-channel: native/mock ratio", "x", nativeMedian / mockMedian));
            entries.add(new Entry("jvm-channel: native/hotspot ratio", "x", nativeMedian / hotspotMedian));
        }
    }

    private static List<Long> runChannelMode(
            Path channelDir, Path executable, String mode, int repeats, int warmup, int iterations)
            throws IOException, InterruptedException {
        if (!Files.exists(executable)) {
            throw new IllegalStateException("Missing " + executable + " - build it first");
        }
        var isJar = executable.toString().endsWith(".jar");
        var medians = new ArrayList<Long>();
        for (var i = 0; i < repeats; i++) {
            var cmd = new ArrayList<String>();
            if (isJar) {
                cmd.add("java");
                cmd.add("-jar");
            }
            cmd.add(executable.toString());
            cmd.add("--mode=" + mode);
            cmd.add("--warmup=" + warmup);
            cmd.add("--iterations=" + iterations);
            var process = new ProcessBuilder(cmd).directory(channelDir.toFile()).start();
            var stdout = new String(process.getInputStream().readAllBytes());
            var stderr = new String(process.getErrorStream().readAllBytes());
            var exit = process.waitFor();
            if (exit != 0) {
                throw new IllegalStateException("--mode=" + mode + " failed (exit " + exit + "): " + stderr);
            }
            medians.add(extractLong(stdout, "medianNanos"));
        }
        return medians;
    }

    // --- bench/interop: ratios read straight out of its own JMH JSON output ---

    private static void addInteropRatios(List<Entry> entries, Path repoRoot) throws IOException {
        var resultFile = repoRoot.resolve("bench/interop/target/jmh-result.json");
        if (!Files.exists(resultFile)) {
            System.out.println("Skipping jvm-interop ratios: " + resultFile + " not found");
            return;
        }
        var scores = parseJmhScores(Files.readString(resultFile));
        addInteropRatio(entries, scores, "channelInvokeString", "localInvokeString", "invokeString");
        addInteropRatio(entries, scores, "channelArrayAccess", "localArrayAccess", "arrayAccess");
    }

    private static void addInteropRatio(
            List<Entry> entries, Map<String, Double> scores, String channelSuffix, String localSuffix, String label) {
        var channel = findScore(scores, channelSuffix);
        var local = findScore(scores, localSuffix);
        if (channel != null && local != null) {
            // both scores are throughput (ops/s): the slower path has the smaller score, so
            // local/channel gives the same ">1 means overhead" sense as the channel harness' ratios
            entries.add(new Entry("jvm-interop: channel/local ratio (" + label + ")", "x", local / channel));
        }
    }

    private static Double findScore(Map<String, Double> scores, String benchmarkSuffix) {
        for (var e : scores.entrySet()) {
            if (e.getKey().endsWith(benchmarkSuffix)) {
                return e.getValue();
            }
        }
        return null;
    }

    private static Map<String, Double> parseJmhScores(String json) {
        var result = new LinkedHashMap<String, Double>();
        var benchPattern = Pattern.compile("\"benchmark\"\\s*:\\s*\"([^\"]+)\"");
        var scorePattern = Pattern.compile("\"score\"\\s*:\\s*([-0-9.eE+]+)");
        var benchMatcher = benchPattern.matcher(json);
        var names = new ArrayList<String>();
        var starts = new ArrayList<Integer>();
        while (benchMatcher.find()) {
            names.add(benchMatcher.group(1));
            starts.add(benchMatcher.start());
        }
        for (var i = 0; i < names.size(); i++) {
            var regionEnd = i + 1 < starts.size() ? starts.get(i + 1) : json.length();
            var region = json.substring(starts.get(i), regionEnd);
            var scoreMatcher = scorePattern.matcher(region);
            if (scoreMatcher.find()) {
                result.put(names.get(i), Double.parseDouble(scoreMatcher.group(1)));
            }
        }
        return result;
    }

    // --- native-image binary sizes ---

    private static void addImageSizes(List<Entry> entries, Path repoRoot) throws IOException {
        addImageSize(entries, repoRoot.resolve("bench/channel/target/bench-channel"), "image size: bench-channel");
        addImageSize(entries, repoRoot.resolve("examples/jvmlauncher/target/demo-jvmlauncher"), "image size: demo-jvmlauncher");
        addImageSize(entries, repoRoot.resolve("examples/jvmchannel/target/demo-jvmchannel"), "image size: demo-jvmchannel");
        addImageSize(entries, repoRoot.resolve("examples/jvminterop/target/demo-jvminterop"), "image size: demo-jvminterop");
    }

    private static void addImageSize(List<Entry> entries, Path exe, String label) throws IOException {
        if (Files.exists(exe)) {
            entries.add(new Entry(label, "bytes", (double) Files.size(exe)));
        } else {
            System.out.println("Skipping " + label + ": " + exe + " not found");
        }
    }

    // --- shared helpers ---

    private static long medianOf(List<Long> values) {
        var sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        return sorted.get(sorted.size() / 2);
    }

    private static long extractLong(String json, String field) {
        var matcher = Pattern.compile("\"" + field + "\":([0-9]+)").matcher(json);
        if (!matcher.find()) {
            throw new IllegalStateException("Missing \"" + field + "\" in: " + json);
        }
        return Long.parseLong(matcher.group(1));
    }

    private record Entry(String name, String unit, double value) {
    }

    private static String toJson(List<Entry> entries) {
        var sb = new StringBuilder("[\n");
        for (var i = 0; i < entries.size(); i++) {
            var e = entries.get(i);
            sb.append("  {\"name\":\"").append(e.name().replace("\"", "\\\"")).append("\",");
            sb.append("\"unit\":\"").append(e.unit()).append("\",");
            sb.append("\"value\":").append(e.value()).append('}');
            if (i + 1 < entries.size()) {
                sb.append(',');
            }
            sb.append('\n');
        }
        return sb.append(']').toString();
    }
}
