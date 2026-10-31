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

import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.Supplier;
import org.graalvm.nativeimage.UnmanagedMemory;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.word.PointerBase;
import org.graalvm.word.WordFactory;

/**
 * Encapsulation of various buffers used to exchange array of bytes among
 * the HotSpot JVM and SVM.
 */
sealed abstract class ExchangeBuffer implements AutoCloseable
permits ExchangeBuffer.ViaMalloc, ExchangeBuffer.ViaBufferSegment, ExchangeBuffer.WithCache {

    /** Creates appropriate implementation of exchange buffer. Such an
     * instance can only be <em>used locally</em>. E.g. from a single thread.
     *
     * @param useMalloc should an SVM implementation be created or
     *   HotSpot one?
     * @return
     */
    static ExchangeBuffer create(boolean useMalloc) {
        return WithCache.find(useMalloc ? ViaMalloc::new : ViaBufferSegment::new);
    }

    abstract ByteBuffer realloc(int size);

    abstract long address();

    abstract ByteBuffer wrap(long addr, int size);

    @Override
    public abstract void close();

    private static final class ViaBufferSegment extends ExchangeBuffer {
        private ByteBuffer currentBuffer;
        private MemorySegment currentAddress;

        @Override
        public ByteBuffer realloc(int size) {
            if (currentBuffer == null || currentBuffer.capacity() != size) {
                currentBuffer = ByteBuffer.allocateDirect(size);
                currentAddress = MemorySegment.ofBuffer(currentBuffer);
            } else {
                currentBuffer.clear();
            }
            return currentBuffer;
        }

        @Override
        public long address() {
            return currentAddress.address();
        }

        @Override
        public final ByteBuffer wrap(long addr, int len) {
            var seg = MemorySegment.ofAddress(addr).reinterpret(len);
            return seg.asByteBuffer();
        }

        @Override
        public void close() {
            // rely on GC
        }
    }


    /**
     * Per thread cache of exchange buffers. Eliminates allocation of memory block
     * on each call among JVM/SVM.
     */
    private static final class WithCache extends ExchangeBuffer {
        private static final ThreadLocal<ExchangeBuffer[]> CACHE = new ThreadLocal<>() {
            @Override
            protected ExchangeBuffer[] initialValue() {
                return new ExchangeBuffer[8];
            }
        };
        private final ExchangeBuffer delegate;

        private WithCache(ExchangeBuffer delegate) {
            this.delegate = delegate;
        }

        /**
         * Finds cached buffer associated with this thread.
         *
         * @param factory is used to create new instance, when none is cached
         * @return ready to used implementation of exchange buffer
         */
        static ExchangeBuffer find(Supplier<ExchangeBuffer> factory) {
            var arr = CACHE.get();
            for (var i = 0; i < arr.length; i++) {
                var delegate = arr[i];
                if (delegate != null) {
                    arr[i] = null;
                    return new WithCache(delegate);
                }
            }
            return new WithCache(factory.get());
        }

        @Override
        public ByteBuffer realloc(int size) {
            return delegate.realloc(size);
        }

        /** Returns the {@link #delegate} back to the {@link #CACHE cached pool}.
         * The delegate isn't closed, it is left open for subsequent usage.
         * If the {@link #CACHE cached pool} is <strong>full</strong>, then
         * the delegate is closed and discarded.
         *
         * @throws Exception
         */
        @Override
        public void close() {
            var arr = CACHE.get();
            for (var i = 0; i < arr.length; i++) {
                if (arr[i] == null) {
                    arr[i] = delegate;
                    return;
                }
            }
            // no empty space in the pool
            delegate.close();
        }

        @Override
        public long address() {
            return delegate.address();
        }

        @Override
        public ByteBuffer wrap(long addr, int size) {
            return delegate.wrap(addr, size);
        }


    }

    private static final class ViaMalloc extends ExchangeBuffer {
        private ByteBuffer currentBuffer;
        private int currentSize;
        private PointerBase currentPointer = WordFactory.nullPointer();

        @Override
        public ByteBuffer realloc(int size) {
            if (currentBuffer != null) {
                if (currentSize != size) {
                    currentPointer = UnmanagedMemory.realloc(currentPointer, WordFactory.unsigned(size));
                    currentBuffer = asNativeByteBuffer(currentPointer, size);
                    currentSize = size;
                } else {
                    currentBuffer.clear();
                }
            } else {
                currentPointer = UnmanagedMemory.malloc(size);
                currentBuffer = asNativeByteBuffer(currentPointer, size);
                currentSize = size;
            }
            return currentBuffer;
        }

        @Override
        public long address() {
            return currentPointer.rawValue();
        }

        @Override
        public ByteBuffer wrap(long addr, int len) {
            var ptr = WordFactory.pointer(addr);
            var buf = CTypeConversion.asByteBuffer(ptr, len);
            return buf.order(ByteOrder.BIG_ENDIAN);
        }

        @Override
        public void close() {
            if (currentBuffer != null) {
                UnmanagedMemory.free(currentPointer);
            }
        }

        private static ByteBuffer asNativeByteBuffer(PointerBase memory, long size) {
            var bufferSize = Math.toIntExact(size);
            return CTypeConversion.asByteBuffer(memory, bufferSize).order(ByteOrder.BIG_ENDIAN);
        }
    }
}
