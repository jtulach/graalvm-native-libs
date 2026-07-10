## Load Classes as Values from the Other JVM

Let your application start in AOT mode, let it create a HotSpot
[JVM](http://hudson.apidesign.org/job/graalvm-native-libs/12/javadoc/org.apidesign.jvm.channel/org/apidesign/jvm/channel/JVM.html)
in the same process and then create a `loader` to load classes
(represented as `org.graalvm.polyglot.Value`) from that JVM.

### Build & Run

Clone [this repository](https://github.com/jtulach/graalvm-native-libs), and build it:
```bash
graalvm-native-libs$ export JAVA_HOME=/graalvm-25.1/
graalvm-native-libs$ mvn clean install
graalvm-native-libs$ cd examples/jvminterop
jvmlauncher$ mvn -Pnative package
```
Such a compilation turns [FactorialViaInterop](./src/main/java/org/apidesign/demo/jvminterop/FactorialViaInterop.java)
into a native executable `./target/demo-jvminterop`. Now we can test it:
```
jvminterop$ ./target/demo-jvminterop 5
{Substrate VM} Loaded org.apidesign.demo.jvminterop.FactorialImpl class from HotSpot JVM
{Substrate VM} Invoking org.apidesign.demo.jvminterop.FactorialImpl.fac(5) method in the HotSpot JVM
{Java HotSpot(TM) 64-Bit Server VM} Computing fac(5)
{Java HotSpot(TM) 64-Bit Server VM} Result of fac(5) is 120
{Substrate VM} Result is back in native code: 120
{Substrate VM} fac(5) = 120
```
Our code starts in the _native executable_. Hence the first messages come
from _Substate VM_ (that's how GraalVM's native image identifies itself).
The executable locates appropriate JDK25+ and instantiates a `jvm` handle to it.

```java
var loader = OtherJvmClassLoader.create(jvm);
Value FactorialImpl = loader.loadClass("org.apidesign.demo.jvminterop.FactorialImpl");
```

Then a `org.graalvm.polyglot.Value` representing `FactorialImpl` class in the
HotSpot VM is obtained via `OtherJvmClassLoader`. One can `invokeMember("fac", 5)`
on such a value and that transfers execution into the other JVM.
As such the next two messages come from HotSpot (identifies as _Java HotSpot_ in this case).
Once factorial is computed the HotSpot JVM returns the `BigInteger` value back
to _native executable_ - which then prints the _"result"_ messages.

Back to [main readme](../../README.md)...
