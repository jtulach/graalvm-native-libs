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
package org.apidesign.demo.jvmchannel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.function.Function;
import org.apidesign.jvm.channel.Channel;
import org.apidesign.jvm.channel.JVM;

public final class FactorialViaChannel {
    private FactorialViaChannel() {}

    public static void main(String[] args) throws Exception {
        assumeOrExit(9, "Provide one numeric argument!", args.length == 1);
        var javaHome = System.getenv("JAVA_HOME");
        assumeOrExit(1, "The environment variable JAVA_HOME must be defined", javaHome != null);
        var javaDir = new File(javaHome);
        assumeOrExit(2, "JAVA_HOME variable must point to a JDK directory, but was " + javaDir, javaDir.isDirectory());

        var jvm = JVM.create(javaDir, "-Djava.class.path=target/classes");
        var ch = Channel.create(jvm, SerdeConf.class);
        ch.execute(Void.class, new RequestFactorial(args[0]));
    }

    record RequestFactorial(String number) implements Function<Channel<?>, Void> {
        @Override
        public Void apply(Channel<?> channel) {
            log("Parsing %n as long number\n", number);
            var n = Long.parseLong(number);
            var acc = BigInteger.ONE;
            for (var i = 0l; i < n; i++) {
                acc = acc.multiply(BigInteger.valueOf(i));
            }
            log("Computed the result to %n sending to the other JVM\n", acc);
            channel.execute(Void.class, new ReportResult(n, acc));
            return null;
        }
    }

    record ReportResult(long number, BigInteger result) implements Function<Channel, Void> {
        @Override
        public Void apply(Channel otherVM) {
            log("Obtained result fac(%d) is %d\n", number, result);
            return null;
        }
    }

    public static final class SerdeConf extends Channel.Config {
        private static final byte NULL = 0x00;
        private static final byte REQUEST = 0x01;
        private static final byte RESULT = 0x02;

        public SerdeConf() {
        }

        @Override
        public void write(Object obj, ByteBuffer buf) throws IOException, BufferOverflowException {
            switch (obj) {
                case null -> {
                    buf.put(NULL);
                }
                case RequestFactorial m -> {
                    buf.put(REQUEST);
                    buf.putInt(m.number().length());
                    buf.asCharBuffer().put(m.number());
                }
                case ReportResult m -> {
                    buf.put(RESULT);
                    buf.putLong(m.number());
                    var arr = m.result.toByteArray();
                    buf.putInt(arr.length);
                    buf.put(arr);
                }
                default -> throw new IOException();
            }
        }

        @Override
        public Object read(ByteBuffer buf) throws IOException {
            var type = buf.get();
            return switch (type) {
                case NULL -> null;
                case REQUEST -> {
                    var len = buf.getInt();
                    var seq = buf.asCharBuffer().subSequence(0, len);
                    yield new RequestFactorial(seq.toString());
                }
                case RESULT -> {
                    var n = buf.getLong();
                    var len = buf.getInt();
                    var arr = new byte[len];
                    buf.get(arr);
                    var big = new BigInteger(arr);
                    yield new ReportResult(n, big);
                }
                default -> throw new IOException();
            };
        }
    }

    private static void log(String fmt, Object... args) {
        var vm = System.getProperty("java.vm.name");
        System.err.printf("[" + vm + "]" + fmt, args);
    }

    private static void assumeOrExit(int exitCode, String msg, boolean check) {
        if (!check) {
            System.err.println(msg);
            System.exit(exitCode);
        }
    }
}
