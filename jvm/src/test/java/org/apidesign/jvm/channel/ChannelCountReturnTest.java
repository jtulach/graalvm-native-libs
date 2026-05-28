package org.apidesign.jvm.channel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChannelCountReturnTest {

    private Channel<Conf> channel;

    @BeforeEach
    public void initializeChannel() throws Exception {
        channel = Channel.create(TestUtils.jvm(), Conf.class);
    }

    @Test
    public void backAndForthFactorialOne() throws Exception {
        var fac = channel.execute(Long.class, new CountDownAndReturn(1, 1));
        assertEquals(1, fac.longValue());
    }


    @Test
    public void backAndForthFactorialTwo() throws Exception {
        var fac = channel.execute(Long.class, new CountDownAndReturn(2, 1));
        assertEquals(2, fac.longValue());
    }

    @Test
    public void backAndForthFactorialThree() throws Exception {
        var fac = channel.execute(Long.class, new CountDownAndReturn(3, 1));
        assertEquals(6, fac.longValue());
    }

    @Test
    public void backAndForthFactorialFour() throws Exception {
        var fac = channel.execute(Long.class, new CountDownAndReturn(4, 1));
        assertEquals(24, fac.longValue());
    }

    @Test
    public void factorialInSecondThread() throws Exception {
        var pool = Executors.newSingleThreadExecutor();
        var v
                = pool.submit(
                        () -> {
                            var fac = channel.execute(Long.class, new CountDownAndReturn(5, 1));
                            return fac;
                        });
        assertEquals(120, v.get().longValue());
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    public void factorialInManyThreads() throws Exception {
        var pool = Executors.newFixedThreadPool(30);
        executeInParallel(1000, pool);
    }

    private void executeInParallel(int count, ExecutorService pool)
            throws ExecutionException, InterruptedException {
        var futures = new ArrayList<Future<Long>>();
        for (int i = 0; i < count; i++) {
            var v
                    = pool.submit(
                            () -> {
                                var fac = channel.execute(Long.class, new CountDownAndReturn(5, 1));
                                return fac;
                            });
            futures.add(v);
        }
        for (var v : futures) {
            assertEquals(120, v.get().longValue());
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    public void backAndForthFactorialFive() throws Exception {
        var fac = channel.execute(Long.class, new CountDownAndReturn(5, 1));
        assertEquals(120, fac.longValue());
    }

    /**
     * Definition of a <em>message</em> to pass thru the {@link Channel}.
     */
    record CountDownAndReturn(long value, long acc) implements Function<Channel<?>, Long> {

        @Override
        public Long apply(Channel<?> otherVM) {
            if (value <= 1) {
                return acc;
            } else {
                return otherVM.execute(Long.class, new CountDownAndReturn(value - 1, acc * value));
            }
        }
    }

    /**
     * Configuration for the channel used in this test. Supports serialization
     * and deserialization of {@link Long} and {@link CountDownAndReturn}. The
     * serialization support is written manually by hand.
     */
    public static final class Conf extends Channel.Config implements Serde {
        @Override
        public Serde createPool(Channel<?> channel) {
            return this;
        }

        @Override
        public byte[] write(Object obj) throws IOException {
            var bos = new ByteArrayOutputStream();
            try (var dos = new DataOutputStream(bos)) {
                switch (obj) {
                    case Long v -> {
                        dos.writeByte(1);
                        dos.writeLong(v);
                    }
                    case CountDownAndReturn v -> {
                        dos.writeByte(11);
                        dos.writeLong(v.value());
                        dos.writeLong(v.acc());
                    }
                    case null -> throw new IOException("null");
                    default -> throw new IOException("" + obj + " type: " + obj.getClass());
                }
            }
            return bos.toByteArray();
        }

        @Override
        public Object read(ByteBuffer buf) throws IOException {
            var type = buf.get();
            return switch (type) {
                case 1 -> buf.getLong();
                case 11 -> {
                    var value = buf.getLong();
                    var acc = buf.getLong();
                    yield new CountDownAndReturn(value, acc);
                }
                default -> throw new IOException("Type: " + type);
            };
        }
    }
}
