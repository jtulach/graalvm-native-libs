# Extra GraalVM `native-image` Utilities

[This project](https://github.com/jtulach/graalvm-native-libs) contains useful utilities
for working with [GraalVM](http://graalvm.org) `native-image`
ahead-of-time compilation. What can you do with these tools?

## The same Java Code in AOT or JVM Mode

It is common in the GraalVM world to support `--jvm` option. By default an application runs compiled
into _native executable_, but when `--jvm` option is added, it can **relaunch itself** in HotSpot JVM
mode. [Graal.js](https://github.com/oracle/graaljs) or [GraalPy](https://github.com/oracle/graalpython)
launchers provide such an `--jvm` flag.

Let's implement the same functionality with the help of this project!
Add a simple [Maven dependency](https://central.sonatype.com/artifact/org.apidesign.graalvm/jvm-channel):

```xml
<dependency>
   <groupId>org.apidesign.graalvm</groupId>
   <artifactId>jvm-channel</artifactId>
</dependency>
```

Let's use the API provided by [JVM class](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/JVM.html)
to launch the HotSpot JVM (in the **same process**!) when needed:

```java
package apptest;
class Launcher {
  public static void main(String... args) {
    if (List.of(args).contains("--jvm") && ImageInfo.inImageRuntimeCode()) {
      var javaHome = new File(System.getenv("JAVA_HOME"));
      var jvm = JVM.create(javaHome, "-Djava.class.path=.");
      jvm.executeMain("apptest/Launcher", args);
      return;
    }
    System.err.println("Running in: " + System.getProperty("java.vm.name"));
  }
}
```

Once such a class gets compiled by [GraalVM Native Image tools](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html)
and is executed, it starts in _native mode_, but if `--jvm` flag is present, it relaunches itself in HotSpot JVM
specified by the `JAVA_HOME` environment variable:

```bash
$ ./target/jvmlauncher
Running in: Substrate VM

$ export JAVA_HOME=/jdk-11/
$ ./target/jvmlauncher --jvm
Running in: OpenJDK 64-Bit Server VM
```

Fine more [details here](examples/jvmlauncher/README.md) including steps to reproduce.

## Communicate Between the JVMs

The previous example just launched second JVM in the same process and launched a
main class in the HotSpot JVM. There was no additional communication between the two JVMs since them.
That's OK for a simple launcher scenario, but for more intricate use-cases some communication may be needed.

```java
var jvm = JVM.create(javaDir, "-Djava.class.path=.");
var ch = Channel.create(jvm, SerdeConf.class);
var res = ch.execute(ReportResult.class, new RequestFactorial(5));
System.out.println("fac(5) = " + res.result());
```

That's why this project supports exchange of messages between the JVMs launched via the `JVM` class. Just use the
[Channel class](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/).
Define your own custom messages like `RequestFactorial` and responses like `ReportResult`
and exchange them between the JVMs!
Details are available in a [separate document](examples/jvmchannel/README.md)
including a sample project to execute.
