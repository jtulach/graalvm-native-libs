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

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.oracle.truffle.api.interop.TruffleObject;
import java.time.LocalDateTime;
import java.util.Date;

class OtherInteropTypeTest {
    public static final ContextUtils ctx
            = ContextUtils.newBuilder("host") // no dynamic languages needed
                    .build();

    static int findType(Object obj) {
        var v = ctx.asValue(obj);
        var raw = ctx.unwrapValue(v);
        if (raw instanceof TruffleObject truffle) {
            return OtherInteropType.findType(truffle);
        } else {
            return 0;
        }
    }

    @Test
    public void checkDate() {
        assertTrue(OtherInteropType.isDate(findType(new Date())));
    }

    @Test
    public void checkDateAndType() {
        var v = findType(LocalDateTime.now());
        assertTrue(OtherInteropType.isDate(v));
        assertTrue(OtherInteropType.isTime(v));
    }

    @Test
    public void checkArray() {
        var v = findType(new String[]{"Hi", "there!"});
        assertTrue(OtherInteropType.hasArrayElements(v));
    }
}
