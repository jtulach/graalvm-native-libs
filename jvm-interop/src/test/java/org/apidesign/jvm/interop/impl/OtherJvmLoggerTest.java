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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apidesign.jvm.channel.Channel;
import org.apidesign.jvm.interop.impl.OtherJvmPool;
import org.graalvm.polyglot.Value;

public class OtherJvmLoggerTest {

    public static final ContextUtils ctx
            = ContextUtils.newBuilder("host") // no dynamic languages needed
                    .build();

    private static Channel<OtherJvmPool> CHANNEL;

    @BeforeAll
    public static void initializeChannel() {
        System.setProperty(OtherJvmPool.DUMP_MESSAGE_PROPERTY, "" + Integer.MAX_VALUE);
        CHANNEL = Channel.create(null, OtherJvmPool.class);
        CHANNEL
                .getConfig()
                .onEnterLeave(
                        CHANNEL,
                        null,
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
    public void registerLoggerObtainALog() throws Exception {
        var otherTest = loadOtherJvmClass(OtherJvmLoggerTest.class.getName());

        class CapturingHandler extends Handler {

            String loggerName;
            Level loggedLevel;
            String loggedMsg;

            @Override
            public void publish(LogRecord lr) {
                assertNull("No log record yet", loggerName);
                loggerName = lr.getLoggerName();
                assertNotNull("Logger name set", loggerName);
                loggedLevel = lr.getLevel();
                loggedMsg = lr.getMessage();
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        }
        var capture = new CapturingHandler();
        withLogHandler(
                Logger.getLogger(""),
                capture,
                () -> {
                    otherTest.invokeMember("logMessage", "test.log.error", "I got logged!");
                });

        assertEquals("Logger created", "test.log.error", capture.loggerName);
        assertEquals(Level.SEVERE, capture.loggedLevel, "Logging at error level maps to severe");
        assertEquals("The right message", "I got logged!", capture.loggedMsg);
    }

    @Test
    public void logWithArguments() throws Exception {
        var otherTest = loadOtherJvmClass(OtherJvmLoggerTest.class.getName());

        class CapturingHandler extends Handler {

            String loggerName;
            Level loggedLevel;
            String loggedMsg;
            Object[] loggedArgs;

            @Override
            public void publish(LogRecord lr) {
                assertNull("No log record yet", loggerName);
                loggerName = lr.getLoggerName();
                assertNotNull("Logger name set", loggerName);
                loggedLevel = lr.getLevel();
                loggedMsg = lr.getMessage();
                loggedArgs = lr.getParameters();
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        }
        var capture = new CapturingHandler();
        withLogHandler(
                Logger.getLogger(""),
                capture,
                () -> {
                    otherTest.invokeMember(
                            "logWithArguments", "test.log.error", "One {0}, two {1}, when {2}");
                });

        assertEquals("test.log.error", capture.loggerName, "Logger created");
        assertEquals(Level.SEVERE, capture.loggedLevel, "Logging at error level maps to severe");
        assertEquals("The right message", "One {0}, two {1}, when {2}", capture.loggedMsg);
        assertEquals(3, capture.loggedArgs.length, "Three args");
        assertEquals(1, capture.loggedArgs[0]);
        assertEquals(2.0, capture.loggedArgs[1]);
        assertEquals(new java.util.Date(43021432432423L).toString(), capture.loggedArgs[2]);
    }

    @Test
    public void registerLoggerObtainALogOnException() throws Exception {
        var otherTest = loadOtherJvmClass(OtherJvmLoggerTest.class.getName());

        class CapturingHandler extends Handler {

            String loggerName;
            Level loggedLevel;
            String loggedMsg;
            Throwable loggedThrown;

            @Override
            public void publish(LogRecord lr) {
                assertNull("No log record yet", loggerName);
                loggerName = lr.getLoggerName();
                assertNotNull("Logger name set", loggerName);
                loggedLevel = lr.getLevel();
                loggedMsg = lr.getMessage();
                loggedThrown = lr.getThrown();
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        }
        var capture = new CapturingHandler();
        withLogHandler(
                Logger.getLogger(""),
                capture,
                () -> {
                    otherTest.invokeMember("logException", "test.log.error", "Throwing", "I got thrown!");
                });

        assertEquals("test.log.error", capture.loggerName, "Logger created");
        assertEquals(Level.SEVERE, capture.loggedLevel, "Logging at error level maps to severe");
        assertEquals("Throwing", capture.loggedMsg, "The right message");
        assertNotNull(capture.loggedThrown, "Exception is transfered");
        assertEquals(
            "I got thrown!", capture.loggedThrown.getMessage(), "Exception has the right message"
        );

        FOUND_STACK_ELEMENT:
        {
            for (var elem : capture.loggedThrown.getStackTrace()) {
                if (elem.getClassName().equals(OtherJvmLoggerTest.class.getName())) {
                    if (elem.getMethodName().equals("logException")) {
                        break FOUND_STACK_ELEMENT;
                    }
                }
                if (elem.getClassName().equals("java.lang.System$Logger")) {
                    break;
                }
            }
            capture.loggedThrown.printStackTrace();
            fail("Expecting `logException` in the stack but without any reference to System.Logger!");
        }
    }

    @Test
    public void registerLoggerObtainALogOnExceptionWithNoMessage() throws Exception {
        var otherTest = loadOtherJvmClass(OtherJvmLoggerTest.class.getName());

        class CapturingHandler extends Handler {

            String loggerName;
            Level loggedLevel;
            String loggedMsg;
            Throwable loggedThrown;

            @Override
            public void publish(LogRecord lr) {
                assertNull("No log record yet", loggerName);
                loggerName = lr.getLoggerName();
                assertNotNull("Logger name set", loggerName);
                loggedLevel = lr.getLevel();
                loggedMsg = lr.getMessage();
                loggedThrown = lr.getThrown();
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        }
        var capture = new CapturingHandler();
        withLogHandler(
                Logger.getLogger(""),
                capture,
                () -> {
                    otherTest.invokeMember("logException", "test.log.error", null, "I got thrown!");
                });

        assertEquals("Logger created", "test.log.error", capture.loggerName);
        assertEquals(Level.SEVERE, capture.loggedLevel, "Logging at error level maps to severe");
        assertNull("The right message", capture.loggedMsg);
        assertNotNull(capture.loggedThrown, "Exception is transfered");
        assertEquals(
                "Exception has the right message", "I got thrown!", capture.loggedThrown.getMessage());
    }

    private static Value loadOtherJvmClass(String name) throws Exception {
        var msg = new OtherJvmMessage.LoadClass(name);
        var raw = CHANNEL.execute(OtherJvmResult.class, msg).value(null);
        if (raw instanceof OtherJvmObject other) {
            assertTrue(other.assertChannel(CHANNEL));
        }
        var value = ctx.asValue(raw);
        return value;
    }

    private static void withLogHandler(Logger l, Handler h, Runnable r) {
        var previous = l.getHandlers();
        try {
            for (var p : previous) {
                l.removeHandler(p);
            }
            l.addHandler(h);
            r.run();
        } finally {
            l.removeHandler(h);
            for (var p : previous) {
                l.addHandler(p);
            }
        }
    }

    public static void logMessage(String logName, String msg) {
        var factory = new OtherJvmLogger(CHANNEL);
        var log = factory.getLogger(logName, null);
        log.log(System.Logger.Level.ERROR, msg);
    }

    public static void logWithArguments(String logName, String format) {
        var factory = new OtherJvmLogger(CHANNEL);
        var log = factory.getLogger(logName, null);
        log.log(System.Logger.Level.ERROR, format, 1, 2.0, new java.util.Date(43021432432423L));
    }

    public static void logException(String logName, String msg, String ex) {
        var factory = new OtherJvmLogger(CHANNEL);
        var log = factory.getLogger(logName, null);
        log.log(System.Logger.Level.ERROR, msg, new IllegalStateException(ex));
    }
}
