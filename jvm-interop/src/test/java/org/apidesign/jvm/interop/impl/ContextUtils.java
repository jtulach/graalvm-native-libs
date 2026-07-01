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
package org.apidesign.jvm.interop.impl;

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import org.apidesign.jvm.channel.Channel;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testing {@link Context} utilities.
 */
public final class ContextUtils {
    private Context ctx;

    public static final String DUMP_MESSAGE_PROPERTY = OtherJvmPool.DUMP_MESSAGE_PROPERTY;

    public static ContextUtils newBuilder(String... langs) {
        return new ContextUtils();
    }

    public ContextUtils build() {
        var host = HostAccess.ALL;
        this.ctx = Context.newBuilder().allowHostAccess(host).build();
        return this;
    }

    public Value asValue(Object obj) {
        return ctx.asValue(obj);
    }

    public Object unwrapValue(Value value) {
        var unwrapper = new Unwrapper();
        var unwrapperValue = asValue(unwrapper);
        unwrapperValue.execute(value);
        assert unwrapper.args != null;
        return unwrapper.args[0];
    }

    public Context context() {
        return ctx;
    }

    public void assertChannel(TruffleObject raw, Channel<OtherJvmPool> ch) {
        if (raw instanceof OtherJvmObject other) {
            assertTrue(other.assertChannel(ch));
        }
    }

    public boolean isOtherJvmObject(Object unwrap) {
        return unwrap instanceof OtherJvmObject;
    }

    public System.LoggerFinder newOtherJvmLogger(Channel<OtherJvmPool> ch) {
        return new OtherJvmLogger(ch);
    }

    @ExportLibrary(InteropLibrary.class)
    static final class Unwrapper implements TruffleObject {

        Object[] args;

        @ExportMessage
        Object execute(Object[] args) {
            this.args = args;
            return this;
        }

        @ExportMessage
        boolean isExecutable() {
            return true;
        }
    }
}
