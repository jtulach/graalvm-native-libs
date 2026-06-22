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

// $ javac Fib.java -g -d ${classes}
// $ java -cp ${classes} -javaagent:${jvminsight} Fib
// > fib(5) = 8
// 2> [JvmInsightAgent]: premain args: null
// 2> [JvmInsightAgent]: transforming Hello redefine: null
// 2> [JvmInsightAgent]: Callback for -1:LHello;.main([Ljava/lang/String;)V with local variables: {args=[Ljava.lang.String;@5d099f62}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=5}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=4}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=3}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=2}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=1}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=0}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=1}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=2}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=1}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=0}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=3}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=2}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=1}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=0}
// 2> [JvmInsightAgent]: Callback for -1:LHello;.fib(I)I with local variables: {n=1}
// 2> exit Hello.main
// $ exit

class Fib {
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
        System.out.println("fib(5) = " + fib(5));
    }
}
