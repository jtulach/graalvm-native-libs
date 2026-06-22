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

// $ javac Hello.java PrintingCallback.java -g -d ${classes}
// $ java -cp ${classes} -javaagent:${jvminsight}=classes=.*Hello,methods=.*hi.*,handler=e2e.PrintingCallback Hello
// > Hi World!
// 2> [JvmInsightAgent]: Transforming Hello
// 2> enter Hello.main
// 2> [Callback]: Method -1:LHello;.hi(Ljava/lang/String;)Ljava/lang/String; with local variables: {subject=World!}
// 2> exit Hello.main
// $ exit

class Hello {
    private static String hi(String subject) {
        return "Hi " + subject;
    }

    static void main(String... args) {
        System.err.println("enter Hello.main");
        System.out.println(hi("World!"));
        System.err.println("exit Hello.main");
    }
}
