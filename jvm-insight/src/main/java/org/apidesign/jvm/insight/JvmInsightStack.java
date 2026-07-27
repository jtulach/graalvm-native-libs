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
package org.apidesign.jvm.insight;

import java.lang.classfile.Label;
import java.lang.classfile.attribute.StackMapFrameInfo;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Models JVM stack. Receives instructions and {@link Label}s and keeps
 * information about the types in the stack.
 */
final class JvmInsightStack {
    private final List<ClassDesc> stackList = new ArrayList<>();

    JvmInsightStack(String name) {
        // System.err.println("STACKFOR: " + name);
    }

    void refresh(StackMapFrameInfo stEn, Map<Integer, JvmInsightTransform.VarInfo> localTypes, Label startScope) {
        for (var s : stEn.stack()) {
            stackList.add(findTypeForStackMapInfo(s));
        }
        // System.err.println("  with stack : " + stackList);
        var cnt = 0;
        for (var verify : stEn.locals()) {
            var type = findTypeForStackMapInfo(verify);
            var prev = localTypes.get(cnt);
            var info = new JvmInsightTransform.VarInfo(
                    prev == null ? null : prev.name(),
                    cnt, type, startScope,
                    prev == null ? null : prev.endScope()
            );
            localTypes.put(cnt, info);
            cnt++;
        }
    }
    void push(ClassDesc type) {
        stackList.add(type);
    }

    ClassDesc pop() {
        return stackList.removeLast();
    }

    private ClassDesc findTypeForStackMapInfo(StackMapFrameInfo.VerificationTypeInfo verify) {
        return switch (verify) {
            case StackMapFrameInfo.ObjectVerificationTypeInfo obj -> {
                yield obj.classSymbol();
            }
            case StackMapFrameInfo.SimpleVerificationTypeInfo.DOUBLE -> {
                yield ConstantDescs.CD_double;
            }
            case StackMapFrameInfo.SimpleVerificationTypeInfo.FLOAT -> {
                yield ConstantDescs.CD_float;
            }
            case StackMapFrameInfo.SimpleVerificationTypeInfo.INTEGER -> {
                yield ConstantDescs.CD_int;
            }
            case StackMapFrameInfo.SimpleVerificationTypeInfo.LONG -> {
                yield ConstantDescs.CD_long;
            }
            case StackMapFrameInfo.SimpleVerificationTypeInfo.TOP -> {
                yield ConstantDescs.CD_Object;
            }
            case StackMapFrameInfo.SimpleVerificationTypeInfo.NULL -> {
                yield ConstantDescs.CD_Object;
            }
            case StackMapFrameInfo.SimpleVerificationTypeInfo.UNINITIALIZED_THIS -> {
                yield ConstantDescs.CD_Object;
            }
            case StackMapFrameInfo.UninitializedVerificationTypeInfo noInit -> {
                yield ConstantDescs.CD_Object;
            }
        };
    }
}
