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
package org.apidesign.jvm.channel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import java.util.function.Function;
import org.graalvm.nativeimage.ImageInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class ChannelMockInSingleJvmTest {

    @Test
    public void exchangeMessageThatModifiesItself() {
        var ch = Channel.create(null, Conf.class);
        assertTrue(ch.isMaster(), "The created channel is a master");

        var msg = new Increment(10);

        var newMsg = ch.execute(Increment.class, msg);

        assertNotNull(newMsg, "Got a value");
        assertEquals(11, newMsg.valueToIncrement(), "10 + 1");
        assertEquals(10, msg.valueToIncrement(), "Original value remains");
    }

    @Test
    public void exchangeMessageThatModifiesPrivateData() {
        Conf.countInstances = 0;
        var ch = Channel.create(null, Conf.class);
        assertEquals(2, Conf.countInstances, "Two channels & data created");
        assertEquals(0, ch.getConfig().counter, "By default we are at zero");

        var msg = new AssignPrivateData(10);
        var newMsg = ch.execute(AssignPrivateData.class, msg);

        assertEquals(0, ch.getConfig().counter, "PrivateData.counter hasn't been changed");

        assertNotNull(newMsg, "Got a value");
        assertEquals(11, newMsg.valueToSet(), "10 + 1");
        assertEquals(10, msg.valueToSet(), "Original value remains");
    }

    @Test
    public void smallText() {
        var ch = Channel.create(null, Conf.class);

        var msg = new GenerateString(256);
        var newMsg = ch.execute(LongString.class, msg);

        assertEquals(256, newMsg.text().length(), newMsg.text());
    }

    @Test
    public void longText() {
        var ch = Channel.create(null, Conf.class);

        var msg = new GenerateString(32632);
        var newMsg = ch.execute(LongString.class, msg);

        assertEquals(32632, newMsg.text().length(), newMsg.text());
    }

    @Test
    public void exceptionIsThrows() {
        var ch = Channel.create(null, Conf.class);

        var msg = new GenerateString(-73);
        try {
            var newMsg = ch.execute(LongString.class, msg);
            fail("Not expecting a return value: " + newMsg);
        } catch (IllegalArgumentException ex) {
            assertEquals("Length must be positive. Was: -73", ex.getMessage());
            var stackTop = ex.getStackTrace()[0];
            assertEquals(GenerateString.class.getName(), stackTop.getClassName());
            assertEquals("handleGenerationOfStrings", stackTop.getMethodName());
        }
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
        var channel = Channel.create(null, Conf.class);
        try {
            channel.execute(Void.class, action);
            fail("Expecting an exception to be thrown for " + msg);
        } catch (IllegalStateException ex) {
            assertEquals(msg, ex.getMessage());
            var countDecrementAndSendMessage = 0;
            for (var elem : ex.getStackTrace()) {
                if ("decrementAndSendMessage".equals(elem.getMethodName())) {
                    assertEquals("ChannelMockInSingleJvmTest.java", elem.getFileName());
                    assertNotEquals(elem.getLineNumber(), -1);
                    assertEquals(action.getClass().getName(), elem.getClassName());
                    countDecrementAndSendMessage++;
                }
            }
            if (action.value() != countDecrementAndSendMessage) {
                ex.printStackTrace();
                assertEquals(action.value(),
                        countDecrementAndSendMessage, "There is exactly right amount of invocations");
            }
        }
    }

    @Test
    public void verifyStopMethodNameReferencesRealMethodName() throws Exception {
        if (ImageInfo.inImageCode()) {
            // only perform the reflection based check in HotSpot mode
            return;
        }
        var stopMethodField = Channel.class.getDeclaredField("STOP_METHOD_NAME");
        stopMethodField.setAccessible(true);
        var stopMethodValue = stopMethodField.get(null);
        for (var m : Channel.class.getDeclaredMethods()) {
            if (m.getName().equals(stopMethodValue)) {
                return;
            }
        }
        fail("STOP_METHOD_NAME field value should be consistent with method name");
    }

    static final class Increment implements Function<Channel<?>, Increment>, Serializable {

        int valueToIncrement;

        Increment(int valueToIncrement) {
            this.valueToIncrement = valueToIncrement;
        }

        int valueToIncrement() {
            return valueToIncrement;
        }

        @Override
        public Increment apply(Channel<?> channel) {
            valueToIncrement++;
            assertFalse(channel.isMaster(), "We are processed in the slave");
            return this;
        }
    }

    static record AssignPrivateData(int valueToSet)
            implements Function<Channel<Conf>, AssignPrivateData>, Serializable {

        @Override
        public AssignPrivateData apply(Channel<Conf> t) {
            t.getConfig().counter = valueToSet;
            return new AssignPrivateData(t.getConfig().counter + 1);
        }
    }

    static record GenerateString(int lengthToGenerate)
            implements Function<Channel<Conf>, LongString>, Serializable {

        @Override
        public LongString apply(Channel<Conf> t) {
            return handleGenerationOfStrings(lengthToGenerate);
        }

        private static LongString handleGenerationOfStrings(int len) {
            if (len < 0) {
                throw new IllegalArgumentException("Length must be positive. Was: " + len);
            }
            return new LongString(len);
        }
    }

    static record LongString(String text) implements Serializable {

        private LongString(int len) {
            this("Hello".repeat(len / 5) + "!!!!!".substring(5 - len % 5));
        }
    }

    record CountDownAndThrow(long value, long acc) implements Function<Channel<?>, Void>, Serializable {

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

    /** Example of a channel configuration that is using {@link ObjectInputStream}
     * and {@link ObjectOutputStream} to transfer messages over the {@link Channel}.
     */
    public static final class Conf extends Channel.Config {
        static int countInstances;
        int counter;

        public Conf() {
            countInstances++;
        }

        @Override
        public void write(Object obj, ByteBuffer buf) throws IOException {
            var bos = new ByteArrayOutputStream();
            try (var dos = new ObjectOutputStream(bos)) {
                dos.writeObject(obj);
            }
            var arr = bos.toByteArray();
            buf.put(arr);
        }

        @Override
        public Object read(ByteBuffer buf) throws IOException {
            var arr = new byte[buf.remaining()];
            buf.get(arr);
            try (var dis = new ObjectInputStream(new ByteArrayInputStream(arr))) {
                return dis.readObject();
            } catch (ClassNotFoundException ex) {
                throw new IOException(ex);
            }
        }
    }
}
