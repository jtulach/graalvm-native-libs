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

// $ javac -cp ${jvminsight} AddToArray.java Insight.java -g -d ${classes}
// $ java -cp ${classes} -javaagent:${jvminsight}=Insight,prefix=java/util/concurrent/ AddToArray A B X
// > [A, B, X]
// 2> [Insight] args: prefix=java/util/concurrent/
// 2> [Insight] patch: java/util/concurrent/CopyOnWriteArraySet => true
// 2> [JvmInsightAgent]: Transforming java/util/concurrent/CopyOnWriteArraySet
// 2> [Insight] at: -1:Ljava/util/concurrent/CopyOnWriteArraySet;.<init>()V frame: {this=null}
// 2> [Insight] at: -1:Ljava/util/concurrent/CopyOnWriteArraySet;.add(Ljava/lang/Object;)Z frame: {e=A, this=[]}
// 2> [Insight] at: -1:Ljava/util/concurrent/CopyOnWriteArraySet;.add(Ljava/lang/Object;)Z frame: {e=B, this=[A]}
// 2> [Insight] at: -1:Ljava/util/concurrent/CopyOnWriteArraySet;.add(Ljava/lang/Object;)Z frame: {e=X, this=[A, B]}
// 2> [Insight] at: -1:Ljava/util/concurrent/CopyOnWriteArraySet;.iterator()Ljava/util/Iterator; frame: {this=[A, B, X]}
// $ exit

class AddToArray {
    public static void main(String... args) {
        try {
            main0(args);
        } catch (Throwable t) {
            System.err.println("thr: " + t.getClass());
            System.err.println("msg: " + t.getMessage());
            for (var e : t.getStackTrace()) {
                System.err.println(" e : " + e);
            }
        }
    }

    private static void main0(String[] args) {
        var arr = new java.util.concurrent.CopyOnWriteArraySet<String>();
        for (var a : args) {
            arr.add(a);
        }
        System.out.println(arr.toString());
    }
}
