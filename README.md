# Extra GraalVM `native-image` Utilities

This package contains useful utilities for working with [GraalVM](http://graalvm.org) `native-image`
ahead-of-time compilation. What can you do with these utilities?

## Execute Java in AOT or JVM Mode

It is common in the GraalVM world to support `--jvm` option. By default an application runs compiled
into _native executable_, but when `--jvm` option is added, it can **relaunch itself** in HotSpot JVM
mode. Let's do that with the help of this project! Add a simple [Maven dependency](https://central.sonatype.com/artifact/org.apidesign.graalvm/jvm-channel):

```xml
<dependency>
   <groupId>org.apidesign.native</groupId>
   <artifactId>jvm-channel</artifactId>
</dependency>
```

Now let's use the [JVM class](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/JVM.html)
to launch the HotSpot JVM (in the same process!) when needed:

```java
package apptest;
class Launcher {
  public static void main(String... args) {
    if (List.of(args).contains("--jvm") && ImageInfo.inImageRuntimeCode()) {
      var javaHome = System.getenv("JAVA_HOME");
      var jvm = JVM.create(javaHome, "-Djava.class.path=.");
      jvm.executeMain("apptest/Launcher", args);
      return;
    }
    System.err.println("Running in: " + System.getProperty("java.vm.name")); 
  }
}
```

Once such a project is compiled by [GraalVM Native Image tools](https://graalvm.github.io/native-build-tools/latest/maven-plugin.html)
it starts in _native mode_, but if `--jvm` flag is present, it relaunches itself in HotSpot JVM 
specified by `JAVA_HOME` environment variable.

## Communicate Between the JVMs

The previous example just launched second JVM in the same process and launched a 
main class in the HotSpot JVM. There was no additional communication between the two JVMs since them.
That's OK for a simple launcher scenario, but for more intricate use-cases some communication may be needed.

That's why this project supports exchange of messages between the JVMs launched via the `JVM` class. Just use the
[Channel class](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/). Details are coming...
