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
package org.apidesign.demo.jvminterop;

import java.math.BigInteger;
import org.graalvm.nativeimage.ImageInfo;

public final class FactorialImpl {

    private FactorialImpl() {
    }

    static {
        if (ImageInfo.inImageCode()) {
            throw new IllegalStateException("Never load this class in AOT mode!");
        }
    }

    public static BigInteger fac(long n) {
        log("Computing fac(%d)\n", n);
        var acc = BigInteger.ONE;
        for (var i = 1l; i <= n; i++) {
            acc = acc.multiply(BigInteger.valueOf(i));
        }
        log("Result of fac(%d) is %d\n", n, acc);
        return acc;
    }

    private static void log(String fmt, Object... args) {
        var vm = System.getProperty("java.vm.name");
        System.err.printf("{" + vm + "} " + fmt, args);
    }

}
