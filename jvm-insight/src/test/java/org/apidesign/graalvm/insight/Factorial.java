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
package org.apidesign.graalvm.insight;

public class Factorial {
    private Factorial() {
    }

    public static int fac(int n) {
        if (n <= 1) {
            return 1;
        } else {
            var n1 = fac(n - 1);
            return n * n1;
        }
    }

    public static int allTypes(
        String prefix,
        boolean type_z, byte type_b, short type_s,
        int type_i, long type_l, char type_c,
        float type_f, double type_d
    ) {
        var txt = prefix + type_z + type_b + type_s + type_i + type_l + type_c + type_f + type_d;
        return txt.length();
    }

    public static int callsite() {
        return -1;
    }

    private static final java.lang.invoke.MutableCallSite meaningSite;
    static {
        var target = java.lang.invoke.MethodHandles.constant(int.class, -1);
        meaningSite = new java.lang.invoke.MutableCallSite(target);
    }
    public static int countMeaning;
    public static int meaning() {
        countMeaning++;
        return 42;
    }

    private static java.lang.invoke.CallSite meaningBootstrap(
        java.lang.invoke.MethodHandles.Lookup lkp,
        String name,
        java.lang.invoke.MethodType t
    ) {
        return meaningSite;
    }

    public static void enableDynamicMeaning() throws Exception {
        var lkp = java.lang.invoke.MethodHandles.lookup();
        var mt = java.lang.invoke.MethodType.methodType(int.class);
        var handle = lkp.findStatic(Factorial.class, "meaning", mt);
        meaningSite.setTarget(handle);
    }
}
