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

// $ javac Fac.java -g -d ${classes}
// $ java -cp ${classes} -javaagent:${jvminsight}=classes=.*Fac,methods=.*fac.* Fac
// > fac(5) = 120
// 2> [JvmInsightAgent]: Transforming Fac
// 2> enter Fac.main
// 2> [JvmInsightAgent]: Callback for -1:LFac;.fac(I)I with local variables: {n=5}
// 2> [JvmInsightAgent]: Callback for -1:LFac;.fac(I)I with local variables: {n=4}
// 2> [JvmInsightAgent]: Callback for -1:LFac;.fac(I)I with local variables: {n=3}
// 2> [JvmInsightAgent]: Callback for -1:LFac;.fac(I)I with local variables: {n=2}
// 2> [JvmInsightAgent]: Callback for -1:LFac;.fac(I)I with local variables: {n=1}
// 2> exit Fac.main
// $ exit

class Fac {
    private static int fac(int n) {
        if (n <= 1) {
            return 1;
        } else {
            var n1 = fac(n - 1);
            return n1 * n;
        }
    }

    static void main(String... args) {
        System.err.println("enter Fac.main");
        System.out.println("fac(5) = " + fac(5));
        System.err.println("exit Fac.main");
    }
}
