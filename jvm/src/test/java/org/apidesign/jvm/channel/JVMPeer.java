package org.apidesign.jvm.channel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class JVMPeer extends Channel.Config implements Serde {

    public JVMPeer() {
    }

    @Override
    public Serde createPool(Channel<?> ignore) {
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
                case TestMain.CountDownAndReturn v -> {
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
                yield new TestMain.CountDownAndReturn(value, acc);
            }
            default -> throw new IOException("Type: " + type);
        };
    }

    @Persistable(id = 432002)
    static final class PersistList extends Persistance<List> {

        PersistList() {
            super(List.class, true, 432002);
        }

        @Override
        protected void writeObject(List obj, Persistance.Output out) throws IOException {
            out.writeInt(obj.size());
            for (Object o : obj) {
                out.writeObject(o);
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected List readObject(Persistance.Input in) throws IOException, ClassNotFoundException {
            int size = in.readInt();
            var lst = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                var obj = in.readObject();
                lst.add(obj);
            }
            return lst;
        }
    }

    @Persistable(id = 4437)
    public static final class PersistString extends Persistance<String> {

        public PersistString() {
            super(String.class, true, 4437);
        }

        @Override
        protected void writeObject(String obj, Persistance.Output out) throws IOException {
            out.writeUTF(obj);
        }

        @Override
        protected String readObject(Persistance.Input in) throws IOException, ClassNotFoundException {
            var obj = in.readUTF();
            return obj;
        }
    }

    @Persistable(id = 4438)
    static final class PersistBigInt extends Persistance<BigInteger> {

        PersistBigInt() {
            super(BigInteger.class, true, 4438);
        }

        @Override
        protected void writeObject(BigInteger obj, Persistance.Output out) throws IOException {
            var arr = obj.toByteArray();
            out.writeInt(arr.length);
            out.write(arr);
        }

        @Override
        protected BigInteger readObject(Persistance.Input in)
                throws IOException, ClassNotFoundException {
            var size = in.readInt();
            var arr = new byte[size];
            in.readFully(arr);
            return new BigInteger(arr);
        }
    }

    @Persistable(id = 4439)
    static final class PersistLong extends Persistance<Long> {

        PersistLong() {
            super(Long.class, true, 4439);
        }

        @Override
        protected void writeObject(Long obj, Persistance.Output out) throws IOException {
            out.writeLong(obj);
        }

        @Override
        protected Long readObject(Persistance.Input in) throws IOException {
            return in.readLong();
        }
    }
}
