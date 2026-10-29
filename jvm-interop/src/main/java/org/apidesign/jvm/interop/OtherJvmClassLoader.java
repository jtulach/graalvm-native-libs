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
package org.apidesign.jvm.interop;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.apidesign.jvm.channel.Channel;
import org.apidesign.jvm.channel.JVM;
import org.apidesign.jvm.interop.impl.OtherJvmException;
import org.apidesign.jvm.interop.impl.OtherJvmMessage;
import org.apidesign.jvm.interop.impl.OtherJvmPool;
import org.apidesign.jvm.interop.impl.OtherJvmResult;
import org.apidesign.jvm.interop.impl.OtherJvmUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

/**
 * Responsible for loading Java classes from <em>other JVM</em> connected via a
 * {@link Channel}. Provides basic methods for direct configuration, but also
 * exposes its functionality via {@link Value} based polyglot API.
 * During initialization with {@link #create} method obtain new instance
 * of the loader. Then use {@link #loadClass} to get references to individual
 * {@link Value} representing each loaded class. Operate with the value as
 * with host objects wrapped by {@link Value#asValue(java.lang.Object)}.
 */
public final class OtherJvmClassLoader implements AutoCloseable {
    private final Channel<OtherJvmPool> channel;
    private Context ctx;

    private OtherJvmClassLoader(Channel<OtherJvmPool> ch) {
        this.channel = ch;
    }

    /**
     * Creates instance of the class loader.
     *
     * @param jvm the "other" JVM to load classes from (can be {@code null} to
     * mock the system inside of the existing JVM)
     * @return new instance of the class loader from the provided JVM
     */
    public static OtherJvmClassLoader create(JVM jvm) {
        var ch = Channel.create(jvm, OtherJvmPool.class);
        var pool = ch.getConfig();
        pool.onEnterLeave(ch, null, null, null, null);
        return new OtherJvmClassLoader(ch);
    }

    /**
     * Adds provided directory to the classpath.
     *
     * @param dir directory to add to classpath
     */
    public final void addPath(File dir) {
        channel.execute(Void.class, new OtherJvmMessage.AddToClassPath(dir.getAbsolutePath()));
    }

    /**
     * Loads a class as a value.
     *
     * @param fqn fully qualified name of class to load
     * @return
     */
    public final Value loadClass(String fqn) {
        try {
            org.apidesign.jvm.interop.impl.OtherJvmResult<com.oracle.truffle.api.interop.TruffleObject, java.lang.ClassNotFoundException> result = channel.execute(OtherJvmResult.class, new OtherJvmMessage.LoadClass(fqn));
            var rawClass = result.value(null);
            if (ctx == null) {
                ctx
                        = Context.newBuilder("hosted")
                                .allowHostAccess(HostAccess.ALL)
                                .allowExperimentalOptions(true)
                                .build();
                Function<Object, Object> enter
                        = (_) -> {
                            ctx.enter();
                            return null;
                        };
                BiConsumer<Object, Object> leave
                        = (_, _) -> {
                            ctx.leave();
                        };
                var pool = channel.getConfig();
                pool.onEnterLeave(channel, null, null, enter, leave);
            }
            return ctx.asValue(rawClass);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Closes the loader. Closes associated channel and/or context.
     */
    @Override
    public final void close() {
        try {
            try {
                channel.getConfig().close(channel);
            } finally {
                if (ctx != null) {
                    ctx.close();
                }
            }
        } catch (Exception ex) {
            if (OtherJvmUtils.isTruffleException(ex)) {
                throw (RuntimeException) ex;
            } else {
                throw new OtherJvmException(ex);
            }
        }
    }
}
