## Communicate Between the JVMs

Define your own custom messages like `RequestFactorial` and responses like `ReportResult`
and exchange them between the JVMs just like it is done in this
[FactorialViaChannel](./src/main/java/org/apidesign/demo/jvmchannel/FactorialViaChannel.java)
class. Let your application start in AOT mode, let it create a HotSpot
[JVM](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/JVM.html)
in the same process and then open a
[Channel](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/Channel.html)
for exchanging these messages back and forth between the JVMs!

### Build & Run

Clone [this repository](https://github.com/jtulach/graalvm-native-libs), and build it:
```bash
graalvm-native-libs$ export JAVA_HOME=/graalvm-25/
graalvm-native-libs$ mvn clean install
graalvm-native-libs$ cd examples/jvmchannel
jvmlauncher$ mvn -Pnative package
```
Such a compilation turns [FactorialViaChannel](./src/main/java/org/apidesign/demo/jvmchannel/FactorialViaChannel.java)
into a native executable `./target/jvmchannel`. Now we can test it:
```
jvmlauncher$ ./target/jvmchannel 5
[Substrate VM] Sending 5 to HotSpot JVM
[OpenJDK 64-Bit Server VM] Parsing 5 as long number
[OpenJDK 64-Bit Server VM] Result computed to 120 - sending it to the other JVM
[Substrate VM] Obtained result fac(5) is 120
```
Our code starts in the _native executable_. Hence the first message comes
from _Substate VM_ (that's how GraalVM's native image identifies itself).
The executable locates appropriate JDK25+ and instantiates a `jvm` handle to it.

```java
var ch = Channel.create(jvm, SerdeConf.class);
ch.execute(Void.class, new RequestFactorial(args[0]));
```

Then a [Channel](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/Channel.html)
is established and a `RequestFactorial` message is sent/executed to the other JVM.
As such the next two messages come from HotSpot (identifies as _OpenJDK_ in this case).
Once factorial is computed the HotSpot JVM sends `ReportResult` message back to
_native executable_ - which then prints the _"result"_ message.

### Serde

There can be multiple `Channel`s created over the same `jvm` object. Their
behavior can be different, as it is configured by associated
[Channel.Config](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/Channel.Config.html)
subclass. The subclass (in this example `SerdeConf`) is responsible for
_serialization_ and _deserialization_ of messages between the two JVMs.

To make sure both sides of the channel understand each other, the same class
is loaded in _Substrate VM_ as well as _HotSpot VM_. As such it needs public
default constructor and needs to be available on the class path/module path
of both JVMs.

This example implements the `SerdeConf` class manually. For more complex
set of messages one may want to use `java.io.ObjectOutputStream` serialization
(accepting its [native image quirks](https://www.graalvm.org/latest/reference-manual/native-image/metadata/#serialization))
or via a dedicated library automatically generating the necessary code
(TBD to provide a link).

### Running on the Other Side

The magic of executing code in the _"other JVM"_ is handled by
[Channel.execute](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/Channel.html#execute(java.lang.Class,java.util.function.Function))
method. It takes a `Function`, serializes it, sends it over the channel
and **synchronously evaluates** it in the other JVM. The API doesn't prescribe how the function
should look like, but it is generally recommended to make it a `record`:

```java
record RequestFactorial(String number) implements Function<Channel<SerdeConf>, ReportResult> {
    @Override
    public ReportResult apply(Channel<SerdeConf> channel) {
        var n = Long.parseLong(number);
        var acc = BigInteger.ONE;
        for (var i = 1l; i <= n; i++) {
            acc = acc.multiply(BigInteger.valueOf(i));
        }
        channel.execute(Void.class, new ReportResult(n, acc));
        return new ReportResult(n, acc);
    }
}
```

Then the serde is simple: just serialize all the `record` constructor arguments
one by one and read them back when deserializing. The function accepts a
[Channel](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/Channel.html)
argument and thus it can communicate back with the JVM that originated the request
establishing fully **duplex communication**. Once a `apply` method is finished, the
result is then returned to the calling JVM (again via serialization and deserialization).

```java
ReportResult res = ch.execute(ReportResult.class, new RequestFactorial("5"));
```

Calling such a function then becomes as easy as the previous line shows.


Back to [main readme](../../README.md)...
