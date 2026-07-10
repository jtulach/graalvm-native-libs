## Execute Java Code in AOT or JVM Mode

Run the same Java code in AOT or in JVM mode with this
[few lines long launcher](./src/main/java/org/apidesign/demo/jvmlauncher/LaunchJvm.java).
Let your application start in AOT mode and let it switch its execution into
HotSpot JVM when a command line `--jvm` switch is found.

### Build & Run

Clone [this repository](https://github.com/jtulach/graalvm-native-libs), and build it:
```bash
graalvm-native-libs$ export JAVA_HOME=/graalvm-25/
graalvm-native-libs$ mvn clean install
graalvm-native-libs$ cd examples/jvmlauncher
jvmlauncher$ mvn -Pnative package
```
Such a compilation turns [LaunchJvm Java file](./src/main/java/org/apidesign/demo/jvmlauncher/LaunchJvm.java)
into a native executable `./target/demo-jvmlauncher`. Now we can test it:
```
jvmlauncher$ ./target/demo-jvmlauncher
Running in: Substrate VM

./target/jvmlauncher --jvm
Running in: OpenJDK 64-Bit Server VM
```
The first execution runs the `System.err.println` code in the `main` method
of [LaunchJvm](./src/main/java/org/apidesign/demo/jvmlauncher/LaunchJvm.java)
directly in the _native executable_. Hence the JVM is **Substrate VM**.

The second execution detects the `--jvm` argument and uses
[JVM.create](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/JVM.html#create(java.io.File,java.lang.String...))
method to create an instance of HotSpot JVM (in the same process)
and then uses [executeMain](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/JVM.html#executeMain(java.lang.String,java.lang.String...))
method to **re-launch itself** in the HotSpot  as the printed
name _OpenJDK 64-Bit Server VM_ indicates.

Back to [main readme](../../README.md)...
