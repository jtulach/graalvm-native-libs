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

import com.oracle.truffle.api.TruffleStackTrace;
import com.oracle.truffle.api.exception.AbstractTruffleException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.Node;

@ExportLibrary(delegateTo = "delegate", value = InteropLibrary.class)
final class OtherJvmTruffleException extends AbstractTruffleException {
  final OtherJvmObject delegate;

  OtherJvmTruffleException(String message, OtherJvmObject delegate, Node location) {
    super(message, location);
    assert delegate != null && InteropLibrary.getUncached().isException(delegate);
    this.delegate = delegate;
    TruffleStackTrace.fillIn(this);
  }

  @ExportMessage
  boolean isException() {
    return true;
  }

  @ExportMessage
  OtherJvmTruffleException throwException() {
    throw this;
  }

  @ExportMessage
  boolean hasExceptionStackTrace() {
    return false;
  }

  @ExportMessage
  Object getExceptionStackTrace() throws UnsupportedMessageException {
    throw UnsupportedMessageException.create();
  }
}
