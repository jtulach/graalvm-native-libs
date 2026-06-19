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

// $ javac Hello.java -g -d ${classes}
// $ java -cp ${classes} -javaagent:${jvminsight} Hello
// $ exit

class Hello {
    private static int fac(int n) {
        if (n <= 1) {
            return 1;
        } else {
            var n1 = fac(n - 1);
            return n1 * n;
        }
    }

    private static int fib(int n) {
        if (n <= 1) {
            return 1;
        } else {
            var n1 = fib(n - 1);
            var n2 = fib(n - 2);
            return n1 + n2;
        }
    }

    public static void main(String... args) {
        System.err.println("enter Hello.main");
        System.out.println("fac(5) = " + fac(5));
        System.out.println("fib(5) = " + fib(5));
        System.err.println("exit Hello.main");
    }
}
