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
package org.apidesign.jvm.interop.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.apidesign.jvm.channel.Channel;

final class OtherJvmRef extends WeakReference<OtherJvmObject> {

  private static final ReferenceQueue<? super OtherJvmObject> ALIVE = new ReferenceQueue<>();
  private static final List<OtherJvmRef> KEEP = new ArrayList<>();
  private final long id;
  private final Channel<OtherJvmPool> channel;

  private OtherJvmRef(OtherJvmObject referent, Channel<OtherJvmPool> ch) {
    super(referent, ALIVE);
    this.id = referent.id();
    this.channel = ch;
    assert this.channel != null;
  }

  @Override
  public String toString() {
    return "Ref{" + "id=" + id + '}';
  }

  static synchronized void registerGCable(OtherJvmObject other, Channel<OtherJvmPool> ch) {
    KEEP.add(new OtherJvmRef(other, ch));
  }

  static synchronized void closeChannel(Channel<OtherJvmPool> ch) {
    var it = KEEP.iterator();
    while (it.hasNext()) {
      var ref = it.next();
      if (ref.channel == ch) {
        ref.enqueue();
        it.remove();
      }
    }
    flushQueue(ch);
  }

  static void flushQueue(Channel<OtherJvmPool> beingClosed) {
    while (true) {
      var r = (OtherJvmRef) ALIVE.poll();
      if (r == null) {
        break;
      }
      if (r.channel == beingClosed) {
        // no need to deliver messages
        continue;
      }
      r.channel.execute(Void.class, new OtherJvmMessage.GC(r.id));
      synchronized (OtherJvmRef.class) {
        KEEP.remove(r);
      }
    }
  }
}
