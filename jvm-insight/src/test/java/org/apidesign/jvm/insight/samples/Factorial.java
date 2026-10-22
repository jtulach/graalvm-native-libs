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
package org.apidesign.jvm.insight.samples;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

public final class Factorial {
    public Factorial() {
    }

    public static int fac(int n) {
        if (n <= 1) {
            return 1;
        } else {
            int n1 = fac(n - 1);
            return n * n1;
        }
    }

    public static String simpleConcat(String a, String b) {
        var sb = new StringBuilder(); sb.append(""); /* empty append needed for Espresso */
        sb.append(a);
        sb.append(b);
        return sb.toString();
    }

    public static void countDown(int arg_0) {
        // name arg_0 selected because of Espresso doesn't use parameter name
        // info when returning from a function, JVM Insight handles any name
        if (arg_0 <= 0) {
            throw new IllegalArgumentException("Count down");
        } else {
            countDown(arg_0 - 1);
        }
    }

    public static int simpleReturn(int n) {
        return n;
    }

    public static int simpleAssign(int n) {
        var sum = n;
        return sum * n;
    }

    public static int simpleFac(int n) {
        var sum = 1;
        for (var i = 1; i <= n; i++) {
            sum = sum * i;
        }
        return sum;
    }

    public static short simpleShortFac(byte n) {
        short sum = 1;
        while (n > 0) {
            sum *= n;
            n--;
        }
        return sum;
    }

    public static void facEx(int n, int[] res) {
        if (n <= 1) {
            res[0] = 1;
            throw new IllegalStateException();
        } else {
            try {
                facEx(n - 1, res);
            } finally {
                res[0] = res[0] * n;
            }
        }
    }

    public int facInst(int n) {
        if (n <= 1) {
            return 1;
        } else {
            int n1 = facInst(n - 1);
            return n * n1;
        }
    }

    public static <E> void forEach(E[] arr, java.util.function.Consumer<? super E> action) {
        for (var e : arr) {
            action.accept(e);
        }
    }

    public static int allTypes(
        String prefix,
        boolean type_z, byte type_b, short type_s,
        int type_i, long type_l, char type_c,
        float type_f, double type_d
    ) {
        String txt = prefix + type_z + type_b + type_s + type_i + type_l + type_c + type_f + type_d;
        return txt.length();
    }

    public int allInstanceTypes(
        String prefix,
        boolean type_z, byte type_b, short type_s,
        int type_i, long type_l, char type_c,
        float type_f, double type_d
    ) {
        String txt = prefix + type_z + type_b + type_s + type_i + type_l + type_c + type_f + type_d;
        return txt.length();
    }

    public static int callsite() {
        return -1;
    }

    private static final java.lang.invoke.MutableCallSite meaningSite;
    static {
        var target = MethodHandles.constant(int.class, -1);
        meaningSite = new MutableCallSite(target);
    }
    public static int countMeaning;
    public static int meaning() {
        countMeaning++;
        return 42;
    }

    public static int mul(int a, int b) {
        return a * b;
    }

    private static java.lang.invoke.CallSite meaningBootstrap(
        MethodHandles.Lookup lkp, String name, MethodType t
    ) {
        return meaningSite;
    }

    public static void enableDynamicMeaning() throws Exception {
        var lkp = MethodHandles.lookup();
        var mt = MethodType.methodType(int.class);
        var handle = lkp.findStatic(Factorial.class, "meaning", mt);
        meaningSite.setTarget(handle);
    }

    public static java.util.function.Consumer<Object> noAction() {
        return (_) -> {};
    }

    public static Object[] fourElementArray() {
        return new Object[] { 2, 3, 5, 8 };
    }
}
