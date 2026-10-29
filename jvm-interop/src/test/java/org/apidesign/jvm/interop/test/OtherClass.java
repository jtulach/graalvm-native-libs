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
package org.apidesign.jvm.interop.test;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.function.Consumer;

public class OtherClass {

    private static final Object IDENTICAL = new MockObject();

    private OtherClass() {
    }

    public static Object nullInstance() {
        return null;
    }


    public static short otherJvmValueOf(String txt) {
        return Short.parseShort(txt);
    }

    public static BigDecimal newBigDecimal(String txt) {
        return new BigDecimal(txt);
    }

    public static Object wrap(String txt) {
        return new MockString(txt);
    }

    public static void callback(Consumer<Object> cb, Object value) {
        cb.accept(value);
    }

    private static final class MockObject {
    }

    public static Object[] otherJvmArrayWithPrimitives() {
        var bigReal
                = new Object[]{
                    "Ahoj", 't', (byte) 1, (short) 2, (int) 3, (long) 4, (float) 5, (double) 6, true
                };
        return bigReal;
    }

    public static Object otherJvmInstances(int kind) {
        return switch (kind) {
            case 0 ->
                IDENTICAL;
            case 1 ->
                new MockObject();
            case 2 ->
                Duration.ofSeconds(42);
            case 3 ->
                new int[20];
            case 4 ->
                "Hello";
            case 5 ->
                "Hello".repeat(100000);
            case 6 ->
                wrap("Hello");
            case 7 ->
                wrap("Hello".repeat(100000));
            default ->
                null;
        };
    }

    public static WithABuffer withBuffer(int type, int size) {
        var buf
                = switch (type) {
            case 0 ->
                ByteBuffer.allocateDirect(size);
            default ->
                throw new IllegalArgumentException();
        };
        buf.put("Hello".getBytes());
        buf.flip();
        return new WithABuffer(buf);
    }

    public static final class WithABuffer {

        public final ByteBuffer buf;

        WithABuffer(ByteBuffer buf) {
            this.buf = buf;
        }

        public String toText() {
            var arr = new byte[buf.limit()];
            buf.get(arr);
            return new String(arr);
        }
    }

    @ExportLibrary(InteropLibrary.class)
    static class MockString implements TruffleObject {

        private final String txt;

        private MockString(String txt) {
            this.txt = txt;
        }

        @ExportMessage
        boolean isString() {
            return true;
        }

        @ExportMessage
        String asString() {
            return txt;
        }
    }

}
