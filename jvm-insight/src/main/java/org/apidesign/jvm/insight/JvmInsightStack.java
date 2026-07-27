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
import java.lang.classfile.MethodModel;
import java.lang.classfile.Opcode;
import java.lang.classfile.attribute.StackMapFrameInfo;
import java.lang.classfile.instruction.ArrayLoadInstruction;
import java.lang.classfile.instruction.ArrayStoreInstruction;
import java.lang.classfile.instruction.BranchInstruction;
import java.lang.classfile.instruction.ConstantInstruction;
import java.lang.classfile.instruction.ConvertInstruction;
import java.lang.classfile.instruction.DiscontinuedInstruction;
import java.lang.classfile.instruction.FieldInstruction;
import java.lang.classfile.instruction.IncrementInstruction;
import java.lang.classfile.instruction.InvokeDynamicInstruction;
import java.lang.classfile.instruction.InvokeInstruction;
import java.lang.classfile.instruction.LoadInstruction;
import java.lang.classfile.instruction.LookupSwitchInstruction;
import java.lang.classfile.instruction.MonitorInstruction;
import java.lang.classfile.instruction.NewMultiArrayInstruction;
import java.lang.classfile.instruction.NewObjectInstruction;
import java.lang.classfile.instruction.NewPrimitiveArrayInstruction;
import java.lang.classfile.instruction.NewReferenceArrayInstruction;
import java.lang.classfile.instruction.NopInstruction;
import java.lang.classfile.instruction.OperatorInstruction;
import java.lang.classfile.instruction.ReturnInstruction;
import java.lang.classfile.instruction.StackInstruction;
import java.lang.classfile.instruction.StoreInstruction;
import java.lang.classfile.instruction.TableSwitchInstruction;
import java.lang.classfile.instruction.ThrowInstruction;
import java.lang.classfile.instruction.TypeCheckInstruction;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/** Models JVM stack. Receives instructions and {@link Label}s and keeps
 * information about the types in the stack.
 */
final class JvmInsightStack {
    private final List<ClassDesc> stackList = new ArrayList<>();
    private final String name;

    JvmInsightStack(MethodModel mm) {
        this.name = mm.methodName().stringValue() + mm.methodType().stringValue();
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
        // System.err.println("  process: " + instr);
        return switch (instr) {
            case LoadInstruction load -> {
                if (info == null) {
                    info = load.typeKind().upperBound();
                }
                stackList.add(info);
                yield null;
            }
            case StoreInstruction store -> {
                yield pop();
            }
            case ArrayLoadInstruction load -> {
                pop();
                pop();
                stackList.add(load.typeKind().upperBound());
                yield null;
            }
            case ArrayStoreInstruction _ -> {
                pop();
                pop();
                pop();
                yield null;
            }
            case FieldInstruction field -> {
                yield switch (field.opcode()) {
                    case PUTFIELD -> {
                        pop();
                        pop();
                        yield null;
                    }
                    case PUTSTATIC -> {
                        pop();
                        yield null;
                    }
                    case GETFIELD -> {
                        pop();
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
            case ConstantInstruction constant -> {
                stackList.add(constant.typeKind().upperBound());
                yield null;
            }
            case NewObjectInstruction newo -> {
                stackList.add(newo.className().asSymbol());
                yield null;
            }
            case NewReferenceArrayInstruction newa -> {
                var type = newa.componentType().asSymbol().arrayType();
                var count = pop();
                assert count == ConstantDescs.CD_int;
                stackList.add(type);
                yield null;
            }
            case NewMultiArrayInstruction newm -> {
                removeLastN(newm.dimensions());
                var type = newm.arrayType().asSymbol();
                for (int i = 0; i < newm.dimensions(); i++) {
                    type = type.arrayType();
                }
                stackList.add(type);
                yield null;
            }
            case NewPrimitiveArrayInstruction newp -> {
                var type = newp.typeKind().upperBound();
                var count = pop();
                assert count == ConstantDescs.CD_int;
                stackList.add(type);
                yield null;
            }
            case InvokeInstruction invoke -> {
                invokeMethod(invoke.typeSymbol());
                yield null;
            }
            case InvokeDynamicInstruction invoke -> {
                invokeMethod(invoke.typeSymbol());
                yield null;
            }
            case OperatorInstruction op -> {
                var unary = switch (op.opcode()) {
                    case INEG, LNEG, FNEG, DNEG -> true;
                    case ARRAYLENGTH -> true;
                    default -> false;
                };
                if (!unary) {
                    pop();
                }
                pop();
                stackList.add(op.typeKind().upperBound());
                yield null;
            }
            case StackInstruction stack -> {
                switch (stack.opcode()) {
                    case Opcode.POP -> pop();
                    case Opcode.POP2 -> {
                        var value1 = pop();
                        if (!isCategory2(value1)) {
                            pop();
                        }
                    }
                    case Opcode.DUP -> {
                        stackList.add(stackList.getLast());
                    }
                    case Opcode.DUP2 -> {
                        var value1 = pop();
                        if (isCategory2(value1)) {
                            stackList.add(value1);
                            stackList.add(value1);
                        } else {
                            var value2 = pop();
                            stackList.add(value2);
                            stackList.add(value1);
                            stackList.add(value2);
                            stackList.add(value1);
                        }
                    }

                    case Opcode.DUP_X1 -> {
                        var value1 = pop();
                        var value2 = pop();
                        stackList.add(value1);
                        stackList.add(value2);
                        stackList.add(value1);
                    }
                    case Opcode.DUP_X2 -> {
                        var value1 = pop();
                        var value2 = pop();
                        if (isCategory2(value1) || isCategory2(value2)) {
                            stackList.add(value1);
                            stackList.add(value2);
                            stackList.add(value1);
                        } else {
                            var value3 = pop();
                            stackList.add(value1);
                            stackList.add(value3);
                            stackList.add(value2);
                            stackList.add(value1);
                        }
                    }
                    case Opcode.DUP2_X1 -> {
                        var value1 = pop();
                        var value2 = pop();
                        if (isCategory2(value1)) {
                            stackList.add(value1);
                            stackList.add(value2);
                            stackList.add(value1);
                        } else {
                            var value3 = pop();
                            stackList.add(value2);
                            stackList.add(value1);
                            stackList.add(value3);
                            stackList.add(value2);
                            stackList.add(value1);
                        }
                    }
                    case Opcode.DUP2_X2 -> {
                        var value1 = pop();
                        var value2 = pop();
                        if (!isCategory2(value1) && !isCategory2(value2)) {
                            // form 1
                            var value3 = pop();
                            var value4 = pop();
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
                            var value3 = pop();
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
            case BranchInstruction branch -> {
                yield switch (branch.opcode()) {
                    case IF_ACMPEQ, IF_ACMPNE -> {
                        pop();
                        pop();
                        yield null;
                    }
                    case IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE -> {
                        pop();
                        yield null;
                    }
                    case IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE -> {
                        pop();
                        pop();
                        yield null;
                    }
                    case IFNULL, IFNONNULL -> {
                        pop();
                        yield null;
                    }
                    case GOTO, GOTO_W -> {
                        ignoring(instr);
                        yield null;
                    }
                    default -> throw new IllegalArgumentException("Unexpected: " + branch);
                };
            }
            case ReturnInstruction _ -> {
                ignoring(instr);
                yield null;
            }
            case ThrowInstruction _ -> {
                pop();
                yield null;
            }
            case ConvertInstruction conv -> {
                var from = pop();
                stackList.add(conv.toType().upperBound());
                yield null;
            }
            case TypeCheckInstruction check -> {
                yield switch (check.opcode()) {
                    case CHECKCAST -> {
                        pop();
                        stackList.add(check.type().asSymbol());
                        yield null;
                    }
                    case INSTANCEOF -> {
                        pop();
                        stackList.add(ConstantDescs.CD_int);
                        yield null;
                    }
                    default -> throw new IllegalArgumentException("Unexpected: " + check);
                };
            }
            case MonitorInstruction _ -> {
                pop();
                yield null;
            }
            case TableSwitchInstruction _, LookupSwitchInstruction _ -> {
                pop();
                yield null;
            }
            case IncrementInstruction _, NopInstruction _ -> {
                // no change
                yield null;
            }
            case DiscontinuedInstruction.JsrInstruction jsr -> {
                stackList.add(ConstantDescs.CD_int);
                yield null;
            }
            case DiscontinuedInstruction.RetInstruction ret -> {
                // no change
                yield null;
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

    private ClassDesc pop() {
        try {
            return stackList.removeLast();
        } catch (NoSuchElementException ex) {
            assert false : "Stack underflow in " + name;
            return ConstantDescs.CD_Object;
        }
    }
}
