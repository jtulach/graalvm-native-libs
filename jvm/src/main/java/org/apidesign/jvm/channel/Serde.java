package org.apidesign.jvm.channel;

import java.io.IOException;
import java.nio.ByteBuffer;

interface Serde {
    public byte[] write(Object obj) throws IOException;
    public Object read(ByteBuffer buf) throws IOException;
}
