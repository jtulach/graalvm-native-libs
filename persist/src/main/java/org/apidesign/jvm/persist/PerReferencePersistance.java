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
import org.apidesign.jvm.persist.Persistance.Reference;

final class PerReferencePeristance extends Persistance<Reference> {
  static final Persistance<Reference> INSTANCE = new PerReferencePeristance();

  private PerReferencePeristance() {
    super(Reference.class, true, 4320);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void writeObject(Reference ref, Output out) throws IOException {
    if (ref.isDeferredWrite()) {
      var refId = PerGenerator.registerReference(out, ref);
      out.writeInt(refId);
    } else {
      out.writeInt(PerGenerator.INLINED_REFERENCE_ID);
      var obj = ref.get(Object.class);
      out.writeObject(obj);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Reference readObject(Input in) throws IOException, ClassNotFoundException {
    var refId = in.readInt();
    if (refId != PerGenerator.INLINED_REFERENCE_ID) {
      return PerInputImpl.findReference(in, refId);
    } else {
      var ref = in.readReference(Object.class);
      return ref;
    }
  }
}
