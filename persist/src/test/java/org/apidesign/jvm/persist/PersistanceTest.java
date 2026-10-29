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
package org.apidesign.jvm.persist;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Supplier;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PersistanceTest {
  @Test
  public void testUUIDPersistance() throws Exception {
    // @start region="write"
    var obj = UUID.randomUUID();
    var buffer = Persistables.POOL.write(obj);
    assertNotNull(buffer, "Byte array is returned");
    assertNotEquals(0, buffer.length, "It has non-zero length");
    // @end region="write"

    // @start region="read"
    var ref = Persistables.POOL.read(buffer);
    var loaded = ref.get(UUID.class);
    assertEquals(obj, loaded, "The same object was recreated");
    // @end region="read"
  }

  @Test
  public void readResolve() throws Exception {
    var poolWith =
        Persistables.POOL.withReadResolve(
            (obj) -> obj instanceof Service s ? new Service(s.value() * 3) : obj);

    var in = new Service(5);
    var arr = poolWith.write(in);

    var plain = Persistables.POOL.read(arr);
    assertEquals(5, plain.get(Service.class).value(), "Remains five");

    var multiOnRead = poolWith.read(arr);
    assertEquals(15, multiOnRead.get(Service.class).value(), "Multiplied on read");
  }

  @Test
  public void writeReplace() throws Exception {
    var poolWith =
        Persistables.POOL.withWriteReplace(
            (obj) -> obj instanceof Service s ? new Service(s.value() * 3) : obj);
    var in = new Service(5);
    var arr = poolWith.write(in);

    var plain = poolWith.read(arr);
    assertEquals(15, plain.get(Service.class).value(), "Multiplied on write");
  }

  @Test
  public void readResolveInline() throws Exception {
    var poolWith =
        Persistables.POOL.withReadResolve(
            (obj) -> obj instanceof Service s ? new Service(s.value() * 3) : obj);

    var in = new ServiceSupply(new Service(5));
    var arr = poolWith.write(in);

    var plain = Persistables.POOL.read(arr);
    assertEquals(5, plain.get(ServiceSupply.class).supply().value(), "Remains five");

    var multiOnRead = poolWith.read(arr);
    assertEquals(15, multiOnRead.get(ServiceSupply.class).supply().value(), "Multiplied on read");
  }

  @Test
  public void writeReplaceInline() throws Exception {
    var poolWith =
        Persistables.POOL.withWriteReplace(
            (obj) -> obj instanceof Service s ? new Service(s.value() * 3) : obj);
    var in = new ServiceSupply(new Service(5));
    var arr = poolWith.write(in);

    var plain = Persistables.POOL.read(arr);
    assertEquals(15, plain.get(ServiceSupply.class).supply().value(), "Multiplied on write");
  }

  @Test
  public void readResolveReference() throws Exception {
    var poolWith =
        Persistables.POOL.withReadResolve(
            (obj) -> obj instanceof Service s ? new Service(s.value() * 3) : obj);

    var in = new IntegerSupply(new Service(5));
    var arr = poolWith.write(in);

    var plain = Persistables.POOL.read(arr);
    assertEquals(5, (int) plain.get(IntegerSupply.class).supply().get(), "Remains five");
    assertEquals(5, (int) plain.get(IntegerSupply.class).supply().get(), "Remains five 2");

    var multiOnRead = poolWith.read(arr);
    assertEquals(15, (int) multiOnRead.get(IntegerSupply.class).supply().get(), "Multiplied on read");
  }

  @Test
  public void writeReplaceReference() throws Exception {
    var poolWith =
        Persistables.POOL.withWriteReplace(
            (obj) -> obj instanceof Service s ? new Service(s.value() * 3) : obj);
    var in = new IntegerSupply(new Service(5));
    var arr = poolWith.write(in);

    var plain = poolWith.read(arr);
    assertEquals(15, (int) plain.get(IntegerSupply.class).supply().get(), "Multiplied on write");
  }

  static <T> T serde(Class<T> clazz, T l, int expectedSize) throws IOException {
    var arr = Persistables.POOL.write(l);
    if (expectedSize >= 0) {
      assertEquals(expectedSize, arr.length - 12);
    }
    var ref = Persistables.POOL.read(arr);
    return ref.get(clazz);
  }

  // @start region="manual"
  @Persistable(id = 328439)
  public static final class PersistUUID extends Persistance<UUID> {

    public PersistUUID() {
      super(UUID.class, false, 328439);
    }

    @Override
    protected void writeObject(UUID obj, Output out) throws IOException {
      out.writeLong(obj.getMostSignificantBits());
      out.writeLong(obj.getLeastSignificantBits());
    }

    @Override
    protected UUID readObject(Input in) throws IOException, ClassNotFoundException {
      var most = in.readLong();
      var least = in.readLong();
      return new UUID(most, least);
    }
  }

  // @end region="manual"

  public static final class Singleton {
    public static final Singleton INSTANCE = new Singleton();

    private Singleton() {}
  }

  @Persistable(id = 432433)
  public static final class PersistSingleton extends Persistance<Singleton> {

    public PersistSingleton() {
      super(Singleton.class, false, 432433);
    }

    @Override
    protected void writeObject(Singleton obj, Output out) throws IOException {}

    @Override
    protected Singleton readObject(Input in) throws IOException, ClassNotFoundException {
      return Singleton.INSTANCE;
    }
  }

  // @start region="annotation"
  @Persistable(id = 432434)
  @Persistable(clazz = IntegerSupply.class, id = 432435)
  public record Service(int value) implements Supplier<Integer> {
    @Override
    public Integer get() {
      return value;
    }
  }

  public record IntegerSupply(Supplier<Integer> supply) {}

  // @end region="annotation"

  // @start region="self-annotation"
  @Persistable(id = 432436)
  public record ServiceSupply(Service supply) {}

  // @end region="self-annotation"

  @Persistable(id = 432437)
  public static class SelfLoop {
    public Persistance.Reference<SelfLoop> self;

    public Persistance.Reference<SelfLoop> self() {
      return self;
    }

    public SelfLoop(Persistance.Reference<SelfLoop> self) {
      this.self = self;
    }
  }

  @Test
  public void testReferenceLoopsInPersistance() throws Exception {
    var obj = new SelfLoop(null);
    // make the loop
    obj.self = Persistance.Reference.of(obj, true);

    var loaded = serde(SelfLoop.class, obj, -1);
    var next = loaded.self.get(SelfLoop.class);
    var next2 = next.self.get(SelfLoop.class);
    assertSame(next, next2, "The recreated object again points to itself");
    assertSame(loaded, next, "The recreated object again points to itself");
  }

  @Persistable(id = 432439)
  public record LongerLoop1(int x, Persistance.Reference<LongerLoop2> y) {}

  @Persistable(id = 432440)
  public record LongerLoop2(Persistance.Reference<LongerLoop3> y) {}

  @Persistable(id = 432441)
  public static class LongerLoop3 {
    public final String a;
    public Persistance.Reference<LongerLoop1> y;

    public String a() {
      return a;
    }

    public Persistance.Reference<LongerLoop1> y() {
      return y;
    }

    public LongerLoop3(String a, Persistance.Reference<LongerLoop1> y) {
      this.a = a;
      this.y = y;
    }
  }

  @Test
  public void testLoopsBetweenDifferentTypes() throws Exception {
    var obj3 = new LongerLoop3("a", null);
    var obj2 = new LongerLoop2(Persistance.Reference.of(obj3, true));
    var obj1 = new LongerLoop1(1, Persistance.Reference.of(obj2, true));
    obj3.y = Persistance.Reference.of(obj1, true);

    var loaded1 = serde(LongerLoop1.class, obj1, -1);
    var r2 = loaded1.y().get(LongerLoop2.class);
    var r3 = r2.y().get(LongerLoop3.class);
    var r1 = r3.y().get(LongerLoop1.class);

    assertSame(loaded1, r1, "The recreated structure contains the loop");

    var current = r1;
    for (var i = 0; i < 10; i++) {
      var next =
          loaded1.y().get(LongerLoop2.class).y().get(LongerLoop3.class).y().get(LongerLoop1.class);
      assertSame(current, next, "current points back to itself");
      current = next;
    }
  }

  @Test
  public void testReferenceLoopsSavedTwiceInPersistance() throws Exception {
    var obj3 = new LongerLoop3("a", null);
    var obj2 = new LongerLoop2(Persistance.Reference.of(obj3, true));
    var obj1 = new LongerLoop1(1, Persistance.Reference.of(obj2, true));
    obj3.y = Persistance.Reference.of(obj1, true);

    var loaded1 = serde(LongerLoop1.class, obj1, -1);
    // Now we serialize the deserialized object again - this is to test that references read from
    // file can be serialized back to a file.
    var loadedAgain = serde(LongerLoop1.class, loaded1, -1);
    var r2 =
        loadedAgain
            .y()
            .get(LongerLoop2.class)
            .y()
            .get(LongerLoop3.class)
            .y()
            .get(LongerLoop1.class);
    assertSame(loadedAgain, r2, "The recreated structure contains the loop");
  }

  @Test
  public void testNullReference() throws Exception {
    var obj1 = new LongerLoop1(1, Persistance.Reference.none());

    var loaded1 = serde(LongerLoop1.class, obj1, -1);
    var inner1 = loaded1.y().get(LongerLoop2.class);
    assertSame(null, inner1, "The reference points to null");
  }

  @Test
  public void testEnumPersistance() throws Exception {
    var yes = serde(Logical.class, Logical.YES, -1);
    assertSame(yes, Logical.YES);
    var no = serde(Logical.class, Logical.NO, -1);
    assertSame(no, Logical.NO);
    var maybe = serde(Logical.class, Logical.MAYBE, -1);
    assertSame(maybe, Logical.MAYBE);
  }

  @Persistable(id = 432442)
  public enum Logical {
    YES,
    NO,
    MAYBE;
  }
}
