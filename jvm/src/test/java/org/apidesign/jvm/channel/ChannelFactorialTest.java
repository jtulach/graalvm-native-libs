package org.apidesign.jvm.channel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class ChannelFactorialTest {

    private static final int MIN = 300;
    private static final int MAX = 3000;
    private static final Map<Long, String> CORRECT_RESULTS = new HashMap<>();

    private Channel<Conf> channel;

    @BeforeEach
    public void initializeChannel() throws Exception {
        channel = Channel.create(TestUtils.jvm(), Conf.class);
    }

    @Test
    public void computeFactorialViaMessages() throws Exception {
        ChannelFactorialTest.CORRECT_RESULTS.clear();
        assertEquals(0, ChannelFactorialTest.CORRECT_RESULTS.size(), "Results are empty");
        var gen = new Random();
        var n = 0L;
        for (var i = 0; i < 5; i++) {
            n += gen.nextLong(MIN, MAX);
            channel.execute(Void.class, new RequestFactorial(n));
        }
        assertEquals(5, ChannelFactorialTest.CORRECT_RESULTS.size(), "Five results found: " + ChannelFactorialTest.CORRECT_RESULTS);
        for (var e : ChannelFactorialTest.CORRECT_RESULTS.entrySet()) {
            var expecting = ChannelFactorialTest.factorial(e.getKey());
            assertEquals(expecting.toString(), e.getValue(), "fac(" + e.getKey() + ") should be");
        }
    }

    @Test
    public void computeFactorialViaSingleMessage() throws Exception {
        var gen = new Random();
        var n = 0L;
        for (var i = 0; i < 5; i++) {
            n += gen.nextLong(MIN, MAX);
            var res = channel.execute(BigInteger.class, new ComputeFactorial(n));
            var expecting = ChannelFactorialTest.factorial(n);
            assertEquals(expecting, res, "fac(" + n + ") should be");
        }
    }

    public static void main(String... args) throws Exception {
        var out = new File(args[0]);
        var n = Integer.parseInt(args[1]);
        try (java.io.FileWriter os = new FileWriter(out)) {
            os.write(factorial(n).toString());
        }
    }

    static BigInteger factorial(long n) {
        var acc = BigInteger.valueOf(1);
        for (;;) {
            acc = acc.multiply(BigInteger.valueOf(n));
            if (--n == 0) {
                break;
            }
        }
        return acc;
    }

    record RequestFactorial(long n) implements Function<Channel<?>, Void> {

        @Override
        public Void apply(Channel<?> channel) {
            assertFalse(channel.isMaster(), "Requesting factorial is handled in the slave only");
            var res = factorial(n).toString();
            channel.execute(Void.class, new ReportResult(n, res));
            return null;
        }
    }

    record ComputeFactorial(long n) implements Function<Object, BigInteger> {

        @Override
        public BigInteger apply(Object ignore) {
            var res = factorial(n);
            return res;
        }
    }

    record ReportResult(long key, String value) implements Function<Channel, Void> {

        @Override
        public Void apply(Channel otherVM) {
            var vm = System.getProperty("java.vm.name");
            if (otherVM.isDualJvmMode()) {
                assertEquals("Substrate VM", vm, "Running in SVM again!");
            } else {
                // running in OpenJDK mock mode
            }
            CORRECT_RESULTS.put(key, value);
            return null;
        }
    }

    /**
     * Manually written serde configuration to transport factorial related
     * messages {@link RequestFactorial}, {@link ComputeFactorial}, and
     * {@link ReportResult}.
     */
    public static final class Conf extends Channel.Config {
        @Override
        public byte[] write(Object obj) throws IOException {
            var bos = new ByteArrayOutputStream();
            try (var dos = new DataOutputStream(bos)) {
                switch (obj) {
                    case RequestFactorial v -> {
                        dos.writeByte(1);
                        dos.writeLong(v.n());
                    }
                    case ComputeFactorial v -> {
                        dos.writeByte(2);
                        dos.writeLong(v.n());
                    }
                    case ReportResult v -> {
                        dos.writeByte(3);
                        dos.writeLong(v.key());
                        dos.writeChars(v.value());
                    }
                    case BigInteger v -> {
                        dos.writeByte(11);
                        var bytes = v.toByteArray();
                        dos.writeInt(bytes.length);
                        dos.write(bytes);
                    }
                    case null -> {
                        dos.writeByte(0);
                    }
                    default ->
                        throw new IOException("Unknown: " + obj);
                }
            }
            return bos.toByteArray();
        }

        @Override
        public Object read(ByteBuffer buf) throws IOException {
            var type = buf.get();
            return switch (type) {
                case 0 -> null;
                case 1 ->
                    new RequestFactorial(buf.getLong());
                case 2 ->
                    new ComputeFactorial(buf.getLong());
                case 3 -> {
                    var key = buf.getLong();
                    var str = buf.asCharBuffer().toString();
                    yield new ReportResult(key, str);
                }
                case 11 -> {
                    var len = buf.getInt();
                    var arr = new byte[len];
                    buf.get(arr);
                    yield new BigInteger(arr);
                }
                default ->
                    throw new IOException("Unknown type: " + type);
            };
        }

    }
}
