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

import org.apidesign.jvm.interop.impl.ContextUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.oracle.truffle.api.TruffleLanguage;
import org.apidesign.jvm.channel.Channel;
import org.apidesign.jvm.interop.impl.OtherJvmMessage;
import org.apidesign.jvm.interop.impl.OtherJvmPool;
import org.apidesign.jvm.interop.impl.OtherJvmResult;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class OtherJvmJavaScriptTest {

    public static final ContextUtils ctx = ContextUtils.newBuilder("host").build();

    private static Channel<OtherJvmPool> CHANNEL;

    @BeforeAll
    public static void initializeChannel() {
        System.setProperty(ContextUtils.DUMP_MESSAGE_PROPERTY, "" + Integer.MAX_VALUE);
        CHANNEL = Channel.create(null, OtherJvmPool.class);
        CHANNEL
                .getConfig()
                .onEnterLeave(
                        CHANNEL,
                        FakeLanguage.class,
                        null,
                        (__) -> {
                            ctx.context().enter();
                            return null;
                        },
                        (__, ___) -> {
                            ctx.context().leave();
                        });
    }

    @Test
    public void wrapTruffleString() throws Exception {
        var testClassValue = loadOtherJvmClass(OtherJvmJavaScriptTest.class.getName());
        assertOtherJvmObject("Represents clazz from the other JVM", testClassValue);

        var result
                = new ResultCallbacks() {
            private Object value;

            @Override
            public void onMessage(Object o) {
                this.value = o;
            }
        };

        var returnedResult = testClassValue.invokeMember("multiString", "Hello", 3, result);
        assertTrue(returnedResult.isString(), "Represents a string");

        assertEquals("HelloHelloHello", returnedResult.asString());
        assertEquals("HelloHelloHello", result.value.toString());
    }

    public static String multiString(String txt, int count, ResultCallbacks onResult) {
        try (var jsCtx = Context.newBuilder("js").build()) {
            var fn
                    = jsCtx.eval(
                            "js",
                            """
              (function(txt, count, onResult) {
                  let sb = "";
                  for (let i = 0; i < count; i++) {
                      sb = sb + txt;
                  }
                  onResult.onMessage(sb);
                  return sb;
              })
              """);
            var res = fn.execute(txt, count, onResult).asString();
            return res;
        }
    }

    private static Value loadOtherJvmClass(String name) throws Exception {
        var msg = new OtherJvmMessage.LoadClass(name);
        var raw = CHANNEL.execute(OtherJvmResult.class, msg).value(null);
        ctx.assertChannel(raw, CHANNEL);
        var value = ctx.asValue(raw);
        return value;
    }

    private static void assertOtherJvmObject(String msg, Value value) {
        var unwrap = ctx.unwrapValue(value);
        if (ctx.isOtherJvmObject(unwrap)) {
            return;
        }
        fail(msg + " but got: " + unwrap);
    }

    public static interface ResultCallbacks {

        public void onMessage(Object o);
    }

    private abstract static class FakeLanguage extends TruffleLanguage<Object> {
    }
}
