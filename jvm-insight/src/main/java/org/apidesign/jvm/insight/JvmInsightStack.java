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

import java.lang.classfile.Instruction;
import java.lang.classfile.Label;
import java.lang.classfile.Opcode;
import java.lang.classfile.attribute.StackMapFrameInfo;
import java.lang.classfile.instruction.ArrayLoadInstruction;
import java.lang.classfile.instruction.BranchInstruction;
import java.lang.classfile.instruction.ConstantInstruction;
import java.lang.classfile.instruction.ConvertInstruction;
import java.lang.classfile.instruction.FieldInstruction;
import java.lang.classfile.instruction.InvokeDynamicInstruction;
import java.lang.classfile.instruction.InvokeInstruction;
import java.lang.classfile.instruction.LoadInstruction;
import java.lang.classfile.instruction.NewMultiArrayInstruction;
import java.lang.classfile.instruction.NewObjectInstruction;
import java.lang.classfile.instruction.NewPrimitiveArrayInstruction;
import java.lang.classfile.instruction.NewReferenceArrayInstruction;
import java.lang.classfile.instruction.OperatorInstruction;
import java.lang.classfile.instruction.StackInstruction;
import java.lang.classfile.instruction.StoreInstruction;
import java.lang.classfile.instruction.TypeCheckInstruction;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
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

    /** Updates the stack based on the instruction.
     *
     * @param instr the instruction that manipulates the stack
     * @param info additional info of the operand or {@code null} if not available
     * @return the type that was just "take off" the stack
     */
    ClassDesc process(Instruction instr, ClassDesc info) {
        // System.err.println("process: " + instr);
        return switch (instr.opcode().kind()) {
            case LOAD -> {
                var load = (LoadInstruction)instr;
                if (info == null) {
                    info = load.typeKind().upperBound();
                }
                stackList.add(info);
                yield null;
            }
            case STORE -> {
                var store = (StoreInstruction)instr;
                assert store.typeKind().upperBound().isPrimitive() == stackList.getLast().isPrimitive();
                yield stackList.removeLast();
            }
            case ARRAY_LOAD -> {
                var load = (ArrayLoadInstruction)instr;
                stackList.removeLast();
                stackList.removeLast();
                stackList.add(load.typeKind().upperBound());
                yield null;
            }
            case ARRAY_STORE -> {
                stackList.removeLast();
                stackList.removeLast();
                stackList.removeLast();
                yield null;
            }
            case FIELD_ACCESS -> {
                var field = (FieldInstruction) instr;
                yield switch (field.opcode()) {
                    case PUTFIELD -> {
                        stackList.removeLast();
                        stackList.removeLast();
                        yield null;
                    }
                    case PUTSTATIC -> {
                        stackList.removeLast();
                        yield null;
                    }
                    case GETFIELD -> {
                        stackList.removeLast();
                        stackList.add(field.typeSymbol());
                        yield null;
                    }
                    case GETSTATIC -> {
                        stackList.add(field.typeSymbol());
                        yield null;
                    }
                    default -> throw new IllegalArgumentException();
                };
            }
            case CONSTANT -> {
                var constant = (ConstantInstruction)instr;
                stackList.add(constant.typeKind().upperBound());
                yield null;
            }
            case NEW_OBJECT -> {
                var newo = (NewObjectInstruction)instr;
                stackList.add(newo.className().asSymbol());
                yield null;
            }
            case NEW_REF_ARRAY -> {
                var newa = (NewReferenceArrayInstruction)instr;
                var type = newa.componentType().asSymbol().arrayType();
                var count = stackList.removeLast();
                assert count == ConstantDescs.CD_int;
                stackList.add(type);
                yield null;
            }
            case NEW_MULTI_ARRAY -> {
                var newm = (NewMultiArrayInstruction)instr;
                removeLastN(newm.dimensions());
                var type = newm.arrayType().asSymbol();
                for (int i = 0; i < newm.dimensions(); i++) {
                    type = type.arrayType();
                }
                stackList.add(type);
                yield null;
            }
            case NEW_PRIMITIVE_ARRAY -> {
                var newp = (NewPrimitiveArrayInstruction)instr;
                var type = newp.typeKind().upperBound();
                var count = stackList.removeLast();
                assert count == ConstantDescs.CD_int;
                stackList.add(type);
                yield null;
            }
            case INVOKE -> {
                var invoke = (InvokeInstruction)instr;
                invokeMethod(invoke.typeSymbol());
                yield null;
            }
            case INVOKE_DYNAMIC -> {
                var invoke = (InvokeDynamicInstruction)instr;
                invokeMethod(invoke.typeSymbol());
                yield null;
            }
            case OPERATOR -> {
                var op = (OperatorInstruction)instr;
                var unary = switch (op.opcode()) {
                    case INEG, LNEG, FNEG, DNEG -> true;
                    case ARRAYLENGTH -> true;
                    default -> false;
                };
                if (!unary) {
                    stackList.removeLast();
                }
                stackList.removeLast();
                stackList.add(op.typeKind().upperBound());
                yield null;
            }
            case STACK -> {
                var stack = (StackInstruction)instr;
                switch (stack.opcode()) {
                    case Opcode.POP -> stackList.removeLast();
                    case Opcode.POP2 -> {
                        var value1 = stackList.removeLast();
                        if (!isCategory2(value1)) {
                            stackList.removeLast();
                        }
                    }
                    case Opcode.DUP -> {
                        stackList.add(stackList.getLast());
                    }
                    case Opcode.DUP2 -> {
                        var value1 = stackList.removeLast();
                        if (isCategory2(value1)) {
                            stackList.add(value1);
                            stackList.add(value1);
                        } else {
                            var value2 = stackList.removeLast();
                            stackList.add(value2);
                            stackList.add(value1);
                            stackList.add(value2);
                            stackList.add(value1);
                        }
                    }

                    case Opcode.DUP_X1 -> {
                        var value1 = stackList.removeLast();
                        var value2 = stackList.removeLast();
                        stackList.add(value1);
                        stackList.add(value2);
                        stackList.add(value1);
                    }
                    case Opcode.DUP_X2 -> {
                        var value1 = stackList.removeLast();
                        var value2 = stackList.removeLast();
                        if (isCategory2(value1)) {
                            stackList.add(value1);
                            stackList.add(value2);
                            stackList.add(value1);
                        } else {
                            var value3 = stackList.removeLast();
                            stackList.add(value1);
                            stackList.add(value3);
                            stackList.add(value2);
                            stackList.add(value1);
                        }
                    }
                    case Opcode.DUP2_X1 -> {
                        var value1 = stackList.removeLast();
                        var value2 = stackList.removeLast();
                        if (isCategory2(value1)) {
                            stackList.add(value1);
                            stackList.add(value2);
                            stackList.add(value1);
                        } else {
                            var value3 = stackList.removeLast();
                            stackList.add(value2);
                            stackList.add(value1);
                            stackList.add(value3);
                            stackList.add(value2);
                            stackList.add(value1);
                        }
                    }
                    case Opcode.DUP2_X2 -> {
                        var value1 = stackList.removeLast();
                        var value2 = stackList.removeLast();
                        if (!isCategory2(value1) && !isCategory2(value2)) {
                            // form 1
                            var value3 = stackList.removeLast();
                            var value4 = stackList.removeLast();
                            stackList.add(value2);
                            stackList.add(value1);
                            stackList.add(value4);
                            stackList.add(value3);
                            stackList.add(value2);
                            stackList.add(value1);
                        } else if (isCategory2(value1) && isCategory2(value2)) {
                            // form 4
                            stackList.add(value1);
                            stackList.add(value2);
                            stackList.add(value1);
                        } else {
                            var value3 = stackList.removeLast();
                            if (isCategory2(value3)) {
                                // form 3
                                stackList.add(value2);
                                stackList.add(value1);
                                stackList.add(value3);
                                stackList.add(value2);
                                stackList.add(value1);
                            } else {
                                // form 2
                                stackList.add(value1);
                                stackList.add(value3);
                                stackList.add(value2);
                                stackList.add(value1);
                            }
                        }
                    }
                    case Opcode.SWAP -> {
                        var nextToLast = stackList.remove(stackList.size() - 2);
                        stackList.add(nextToLast);
                    }
                    default -> throw new IllegalArgumentException("Unexpected: " + stack.opcode());
                }
                yield null;
            }
            case BRANCH -> {
                var branch = (BranchInstruction)instr;
                yield switch (branch.opcode()) {
                    case IF_ACMPEQ, IF_ACMPNE -> {
                        stackList.removeLast();
                        stackList.removeLast();
                        yield null;
                    }
                    case IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE -> {
                        stackList.removeLast();
                        yield null;
                    }
                    case IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE -> {
                        stackList.removeLast();
                        stackList.removeLast();
                        yield null;
                    }
                    case IFNULL, IFNONNULL -> {
                        stackList.removeLast();
                        yield null;
                    }
                    case GOTO, GOTO_W -> {
                        ignoring(instr);
                        yield null;
                    }
                    default -> throw new IllegalArgumentException("Unexpected: " + branch);
                };
            }
            case RETURN -> {
                ignoring(instr);
                yield null;
            }
            case THROW_EXCEPTION -> {
                stackList.removeLast();
                yield null;
            }
            case CONVERT -> {
                var conv = (ConvertInstruction)instr;
                var from = stackList.removeLast();
                assert from == conv.fromType().upperBound();
                stackList.add(conv.toType().upperBound());
                yield null;
            }
            case TYPE_CHECK -> {
                var check = (TypeCheckInstruction)instr;
                stackList.removeLast();
                stackList.add(check.type().asSymbol());
                yield null;
            }
            case MONITOR -> {
                stackList.removeLast();
                yield null;
            }
            case TABLE_SWITCH, LOOKUP_SWITCH -> {
                stackList.removeLast();
                yield null;
            }
            case INCREMENT, NOP -> {
                // no change
                yield null;
            }
            case DISCONTINUED_RET, DISCONTINUED_JSR -> {
                throw new IllegalStateException("Discontinued: " + instr);
            }
        };
    }

    private static boolean isCategory2(ClassDesc value1) {
        var category2 = (value1 == ConstantDescs.CD_long || value1 == ConstantDescs.CD_double);
        return category2;
    }

    private void invokeMethod(final MethodTypeDesc typeSymbol) {
        var remove = typeSymbol.parameterCount();
        removeLastN(remove);
        if (typeSymbol.returnType() != ConstantDescs.CD_void) {
            stackList.add(typeSymbol.returnType());
        }
    }

    private void removeLastN(int remove) {
        var len = stackList.size();
        stackList.subList(len - remove, len).clear();
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

    private void ignoring(Instruction instr) {
        // System.err.println("ignoring stack instruction " + instr);
    }
}
