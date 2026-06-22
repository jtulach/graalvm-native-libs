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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.io.TempDir;

/** Compiles and executes prepared scripts in {@code test/resources/e2e} directory.
 * It looks for lines starting with {@code //} as instructions what to do.
 * Line starting {@code // $} signals an instruction.
 *
 */
public final class EndToEndIT {
    public static List<File> allScriptFiles() throws Exception {
        var u = EndToEndIT.class.getResource("/e2e/Hello.java");
        assertNotNull(u, "There must be Hello.java example");
        var f = new File(u.toURI());
        var d = f.getParentFile();
        var arr = new ArrayList<File>();
        var raw = d.listFiles();
        assertNotNull(raw, "There must be some files in " + d);
        for (var ch : raw) {
            if (ch.getName().endsWith(".java")) {
                arr.add(ch);
            }
        }
        return arr;
    }

    @TempDir
    File classes;

    @ParameterizedTest
    @MethodSource("allScriptFiles")
    public void runTheTests(File script) throws Exception {
        var jarUrl = JvmInsight.class.getProtectionDomain().getCodeSource().getLocation();
        var jarFile = new File(jarUrl.toURI());
        assertTrue(jarFile.isFile(), "Found java agent JAR file at " + jarFile);

        var jvmBin = new File(new File(System.getProperty("java.home")), "bin");

        List<CmdRun> commands = new ArrayList<>();
        try {
            var commandsAndOutput = Files.readAllLines(script.toPath()).stream()
                .filter(line -> line.startsWith("//"))
                .toList();

            CmdRun current = null;
            for (var line : commandsAndOutput) {
                if (line.startsWith("// $ ")) {
                    if (current != null) {
                        commands.add(current);
                    }
                    current = new CmdRun(line.substring(5));
                } else {
                    if (current == null) {
                        throw new IllegalStateException("Unexpected line: " + line);
                    }
                    if (line.startsWith("// >")) {
                        current.stdout().add(line.substring(5));
                    } else if (line.startsWith("// <")) {
                        current.stdin().add(line.substring(5));
                    } else if (line.startsWith("// 2>")) {
                        current.stderr().add(line.substring(6));
                    } else {
                        throw new IllegalStateException("Unexpected line: " + line);
                    }
                }
            }
            if (current != null) {
                commands.add(current);
            }
        } catch (Exception ex) {
            throw new IOException("Cannot read " + script, ex);
        }

        var log = new StringBuilder();
        for (var rawLine : commands) {
            var cmdLine = rawLine
                .execute()
                .trim();

            var rawArgs = cmdLine.split(" +");
            if (rawArgs.length == 1 && "exit".equals(rawArgs[0])) {
                // successfully executed
                return;
            }
            var cmdArgs = Stream.of(rawArgs)
                .map(line -> line
                    .replace("${jvminsight}", jarFile.getAbsolutePath())
                    .replace("${classes}", classes.getAbsolutePath())
                )
                .toArray(String[]::new);

            var exe = new File(cmdArgs[0]);
            if (!exe.isAbsolute() || !exe.canExecute()) {
                exe = new File(jvmBin, cmdArgs[0]);
            }
            assertTrue(exe.canExecute(), "The command can be executed: " + exe + "\n" + log);
            cmdArgs[0] = exe.getAbsolutePath();

            log.append("Executing: ").append(Arrays.toString(cmdArgs));

            var bldr = new ProcessBuilder(cmdArgs)
                .directory(script.getParentFile());

            var proc = bldr.start();
            for (var line : rawLine.stdout()) {
                proc.getOutputStream().write(line.getBytes());
                proc.getOutputStream().write('\n');
            }
            var exitCode = proc.waitFor();
            var stdOut = new String(proc.getInputStream().readAllBytes());
            var stdErr = new String(proc.getErrorStream().readAllBytes());

            log.append(stdOut).append("\n");
            log.append(stdErr).append("\n");

            assertEquals(0, exitCode, "Failure executing " + cmdLine + "\n" + log);
            var expOut = String.join("\n", rawLine.stdout()).trim();
            if (!expOut.equals("*")) {
                assertEquals(expOut, stdOut.trim(), "Unexpected stdout in\n" + log);
            }
            var expErr = String.join("\n", rawLine.stderr()).trim();
            if (!expErr.equals("*")) {
                assertEquals(expErr, stdErr.trim(), "Unexpected stderr in\n" + log);
            }
        }
        fail("Finish the sequence of commands by using 'exit'");
    }

    private static record CmdRun(
        String execute,
        List<String> stdin, List<String> stdout, List<String> stderr
    ) {
        private CmdRun (String execute) {
            this(execute.trim(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
    }
}
