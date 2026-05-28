package org.apidesign.jvm.channel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChannelCountThrowTest {

    private Channel<Conf> channel;

    @BeforeEach
    public void initializeChannel() throws Exception {
        channel = Channel.create(TestUtils.jvm(), Conf.class);
    }

    @Test
    public void throwFactorialOne() throws Exception {
        assertException("1", new CountDownAndThrow(1, 1));
    }

    @Test
    public void throwFactorialTwo() throws Exception {
        assertException("2", new CountDownAndThrow(2, 1));
    }

    @Test
    public void throwFactorialThree() throws Exception {
        assertException("6", new CountDownAndThrow(3, 1));
    }

    @Test
    public void throwFactorialFour() throws Exception {
        assertException("24", new CountDownAndThrow(4, 1));
    }

    @Test
    public void throwFactorialFive() throws Exception {
        assertException("120", new CountDownAndThrow(5, 1));
    }

    private void assertException(String msg, CountDownAndThrow action) {
        try {
            channel.execute(Void.class, action);
            fail("Expecting an exception to be thrown for " + msg);
        } catch (IllegalStateException ex) {
            assertEquals(msg, ex.getMessage());
            var countDecrementAndSendMessage = 0;
            for (var elem : ex.getStackTrace()) {
                if ("decrementAndSendMessage".equals(elem.getMethodName())) {
                    assertEquals("ChannelCountThrowTest.java", elem.getFileName());
                    assertNotEquals(-1, elem.getLineNumber());
                    assertEquals(action.getClass().getName(), elem.getClassName());
                    countDecrementAndSendMessage++;
                }
            }
            if (action.value() != countDecrementAndSendMessage) {
                ex.printStackTrace();
                assertEquals(action.value(), countDecrementAndSendMessage, "There is exactly right amount of invocations");
            }
        }
    }

    record CountDownAndThrow(long value, long acc) implements Function<Channel<?>, Void> {

        @Override
        public Void apply(Channel<?> otherVM) {
            decrementAndSendMessage(value, acc, otherVM);
            return null;
        }

        private static void decrementAndSendMessage(long n, long sum, Channel<?> otherVM) {
            if (n <= 1) {
                throw new IllegalStateException("" + sum);
            } else {
                otherVM.execute(Void.class, new CountDownAndThrow(n - 1, sum * n));
            }
        }
    }

    /**
     * Configuration for the channel used in this test. Supports serialization
     * and deserialization of {@link Long} and {@link CountDownAndReturn}. The
     * serialization support is written manually by hand.
     */
    public static final class Conf extends Channel.Config {
        @Override
        public byte[] write(Object obj) throws IOException {
            var bos = new ByteArrayOutputStream();
            try (var dos = new DataOutputStream(bos)) {
                switch (obj) {
                    case Long v -> {
                        dos.writeByte(7);
                        dos.writeLong(v);
                    }
                    case CountDownAndThrow v -> {
                        dos.writeByte(33);
                        dos.writeLong(v.value());
                        dos.writeLong(v.acc());
                    }
                    case null ->
                        throw new IOException("null");
                    default ->
                        throw new IOException("" + obj + " type: " + obj.getClass());
                }
            }
            return bos.toByteArray();
        }

        @Override
        public Object read(ByteBuffer buf) throws IOException {
            var type = buf.get();
            return switch (type) {
                case 7 ->
                    buf.getLong();
                case 33 -> {
                    var value = buf.getLong();
                    var acc = buf.getLong();
                    yield new CountDownAndThrow(value, acc);
                }
                default ->
                    throw new IOException("Type: " + type);
            };
        }
    }
}
