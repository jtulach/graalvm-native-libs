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

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import org.apidesign.jvm.channel.Channel;

/**
 * Hand written serde for {@link BenchContract}'s two message shapes, the same style as
 * {@code examples/jvmchannel}'s own config class: writing the serializer by hand is simple and
 * least magical for a wire format this small.
 */
public final class Conf extends Channel.Config {
    private static final byte REQUEST = 0x01;
    private static final byte RESULT = 0x02;

    public Conf() {
    }

    @Override
    public void write(Object obj, ByteBuffer buf) throws IOException, BufferOverflowException {
        switch (obj) {
            case BenchContract.ComputeRequest r -> {
                buf.put(REQUEST);
                buf.putLong(r.n());
            }
            case Long l -> {
                buf.put(RESULT);
                buf.putLong(l);
            }
            default -> throw new IOException("Unknown message: " + obj);
        }
    }

    @Override
    public Object read(ByteBuffer buf) throws IOException {
        var type = buf.get();
        return switch (type) {
            case REQUEST -> new BenchContract.ComputeRequest(buf.getLong());
            case RESULT -> buf.getLong();
            default -> throw new IOException("Unknown message type: " + type);
        };
    }
}
