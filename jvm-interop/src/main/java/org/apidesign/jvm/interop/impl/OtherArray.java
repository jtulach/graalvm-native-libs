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

import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
final class OtherArray implements TruffleObject {
  private final TruffleObject[] arr;

  OtherArray(TruffleObject... arr) {
    this.arr = arr;
  }

  @ExportMessage
  boolean hasArrayElements() {
    return true;
  }

  @ExportMessage
  long getArraySize() {
    return arr.length;
  }

  @ExportMessage
  boolean isArrayElementReadable(long index) {
    return index >= 0 && index < arr.length;
  }

  @ExportMessage
  TruffleObject readArrayElement(long index) throws InvalidArrayIndexException {
    if (!isArrayElementReadable(index)) {
      throw InvalidArrayIndexException.create(index);
    }
    return arr[Math.toIntExact(index)];
  }
}
