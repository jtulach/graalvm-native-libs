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
package org.apidesign.graalvm.insight;

import java.lang.classfile.Attributes;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.CodeModel;
import java.lang.classfile.Label;
import java.lang.classfile.MethodModel;
import java.lang.classfile.TypeKind;
import java.lang.classfile.attribute.LocalVariableInfo;
import java.lang.classfile.attribute.LocalVariableTableAttribute;
import java.lang.classfile.constantpool.ClassEntry;
import java.lang.classfile.instruction.IncrementInstruction;
import java.lang.classfile.instruction.LineNumber;
import java.lang.classfile.instruction.LoadInstruction;
import java.lang.classfile.instruction.LocalVariable;
import java.lang.classfile.instruction.StoreInstruction;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

/** Transformer patching byte code to be {@link JvmInsight}-ready.
 */
final class JvmInsightTransform implements ClassTransform {
    private final ClassModel model;
    private final ClassDesc callbackClass;

    public JvmInsightTransform(ClassModel clazz) {
        this.model = clazz;
        this.callbackClass = ClassDesc.of("java.util.function.BiConsumer");
    }

    @Override
    public void accept(ClassBuilder builder, ClassElement element) {
        if (element instanceof MethodModel method) {
            builder.transformMethod(method, (mb, me) -> {
                if (
                    me instanceof CodeModel code
                    && code.findAttribute(Attributes.localVariableTable()) instanceof Optional<LocalVariableTableAttribute> opt
                    && opt.isPresent()
                ) {
                    mb.withCode(cb -> {
                        var firstLabel = cb.newLabel();
                        var lastLabel = cb.newLabel();
                        var methodType = method.methodTypeSymbol();
                        var count = methodType.parameterCount();
                        int argsArr; // slot with reference to array with values
                        {
                            var mapOpt = opt.get().localVariables().stream().mapToInt(LocalVariableInfo::slot).max();
                            var maxSlotIndex = mapOpt.isPresent() ? mapOpt.getAsInt() + 1 : 1;

                            // copies all the values into an argument
                            cb.loadConstant(maxSlotIndex);
                            cb.anewarray(ConstantDescs.CD_Object);
                            for (var i = 0; i < count; i++) {
                                var typeDescr = methodType.parameterType(i); // type of i-th parameter
                                var slot = cb.parameterSlot(i); // slot for i-th parameter

                                cb.dup(); // arrayref
                                cb.loadConstant(i); // index
                                loadObjectWraper(cb, typeDescr, slot); // value
                                cb.arrayStore(TypeKind.REFERENCE);
                            }
                            argsArr = maxSlotIndex;
                            cb.localVariable(argsArr, "dbgArgsArr",
                                ClassDesc.ofDescriptor("[Ljava/lang/Object;"),
                                firstLabel, lastLabel
                            );
                            cb.astore(argsArr);
                            cb.labelBinding(firstLabel);
                        }
                        var enterGenerated = false;
                        var localTypes = new HashMap<Integer, LocalVariableInfo>();
                        var locals = new HashMap<Integer, LocalVariableInfo>();
                        for (var instr : code.elementList()) {
                            // System.err.println("  instr: " + instr);
                            if (instr instanceof LocalVariableInfo localVar) {
                                if (localVar.typeSymbol() == ConstantDescs.CD_long || localVar.typeSymbol() == ConstantDescs.CD_double) {
                                    // long and double vars occupy two slots
                                    count++;
                                }
                                localTypes.put(localVar.slot(), localVar);
                                if (!localVar.name().equalsString("this")) {
                                    if (localVar.slot() < count) {
                                        locals.put(localVar.slot(), localVar);
                                    }
                                }
                            }

                            var enableArgsArr = method.methodName().stringValue().startsWith("simple");

                            if (instr instanceof LoadInstruction load) {
                                if (enableArgsArr) {
                                    if (load.slot() > 0 || method.flags().has(AccessFlag.STATIC)) {
                                        loadFromArray(cb, argsArr, load.typeKind(), load.slot());
                                        continue;
                                    }
                                }
                            }
                            if (instr instanceof StoreInstruction store) {
                                var initializedVar = localTypes.get(store.slot());
                                if (initializedVar != null) {
                                    // initializedVar can be null when there is no debug info
                                    locals.put(store.slot(), initializedVar);
                                }
                                if (store.slot() > 0 || method.flags().has(AccessFlag.STATIC)) {
                                    storeToArray(cb, argsArr, store.typeKind(), store.slot());
                                    if (enableArgsArr) {
                                        continue;
                                    }
                                }
                            }
                            if (instr instanceof IncrementInstruction inc) {
                                if (enableArgsArr) {
                                    loadFromArray(cb, argsArr, TypeKind.INT, inc.slot());
                                    cb.loadConstant(inc.constant());
                                    cb.iadd();
                                    storeToArray(cb, argsArr, TypeKind.INT, inc.slot());
                                    continue;
                                }
                            }

                            cb.with(instr);

                            if (instr instanceof Label label) {
                                if (!enterGenerated) {
                                    onEnter("ROOTS", method, -1, locals.values(), cb);
                                    enterGenerated = true;
                                }
                                var it = localTypes.entrySet().iterator();
                                while (it.hasNext()) {
                                    var en = it.next();
                                    if (
                                        en.getValue() instanceof LocalVariable localVar
                                        && localVar.endScope() == label
                                    ) {
                                        it.remove();
                                        locals.remove(en.getKey());
                                    }
                                }
                            }
                            if (instr instanceof LineNumber line) {
                                onEnter("STATEMENTS", method, line.line(), locals.values(), cb);
                            }
                        }
                        cb.labelBinding(lastLabel);
                    });
                } else {
                    mb.with(me);
                }
            });
        } else {
            builder.with(element);
        }
    }

    private void onEnter(String fieldName, MethodModel method, int line, Collection<LocalVariableInfo> locals, CodeBuilder cb) {
        if (method.methodName().stringValue().startsWith("simple")) {
            return;
        }
        var insightClazz = ClassDesc.of(JvmInsight.class.getName());
        var boot = ConstantDescs.ofCallsiteBootstrap(insightClazz, "metafactory", ConstantDescs.CD_CallSite);
        var ref = DynamicCallSiteDesc.of(boot, fieldName, MethodTypeDesc.of(callbackClass));
        cb.invokedynamic(ref);
        var noCallback = cb.newLabel();
        cb.ifnull(noCallback);
        cb.invokedynamic(ref);
        cb.loadConstant(fqn(model.thisClass(), method, line));
        var argumentCount = 0;
        for (var l : locals) {
            cb.loadConstant(l.name().stringValue());
            loadObjectWraper(cb, l.typeSymbol(), l.slot());
            argumentCount += 2;
        }
        var Map = ClassDesc.of("java.util.Map");
        var ofArgsType = MethodTypeDesc.of(Map, Collections.nCopies(argumentCount, ConstantDescs.CD_Object));
        cb.invokestatic(Map, "of", ofArgsType, true);
        var consumeType = MethodTypeDesc.of(ConstantDescs.CD_void, ConstantDescs.CD_Object, ConstantDescs.CD_Object);
        cb.invokeinterface(callbackClass, "accept", consumeType);
        cb.labelBinding(noCallback);
    }

    private void loadObjectWraper(CodeBuilder cb, ClassDesc typeSymbol, int slot) {
        var kind = TypeKind.fromDescriptor(typeSymbol.descriptorString());
        cb.loadLocal(kind, slot);
        wrapperToReference(cb, kind);
    }

    /**
     * This method assumes there is a variable of type {@code kind} on the top
     * of the stack and converts it to wrapper type. E.g. {@code int} is
     * converted to {@link Integer}, etc. If the value isn't primitive, it
     * stays as it is.
     *
     * @param cb code builder to emit instructions to
     * @param kind the type of the variable on the stack
     * @throws IllegalStateException if the {@code kind} isn't recognized
     */
    private static void wrapperToReference(CodeBuilder cb, TypeKind kind) throws IllegalStateException {
        switch (kind) {
            case BOOLEAN -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Boolean, ConstantDescs.CD_boolean);
                cb.invokestatic(ConstantDescs.CD_Boolean, "valueOf", type);
            }
            case BYTE -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Byte, ConstantDescs.CD_byte);
                cb.invokestatic(ConstantDescs.CD_Byte, "valueOf", type);
            }
            case CHAR -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Character, ConstantDescs.CD_char);
                cb.invokestatic(ConstantDescs.CD_Character, "valueOf", type);
            }
            case SHORT -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Short, ConstantDescs.CD_short);
                cb.invokestatic(ConstantDescs.CD_Short, "valueOf", type);
            }
            case INT -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Integer, ConstantDescs.CD_int);
                cb.invokestatic(ConstantDescs.CD_Integer, "valueOf", type);
            }
            case LONG -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Long, ConstantDescs.CD_long);
                cb.invokestatic(ConstantDescs.CD_Long, "valueOf", type);
            }
            case FLOAT -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Float, ConstantDescs.CD_float);
                cb.invokestatic(ConstantDescs.CD_Float, "valueOf", type);
            }
            case DOUBLE -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_Double, ConstantDescs.CD_double);
                cb.invokestatic(ConstantDescs.CD_Double, "valueOf", type);
            }
            case REFERENCE -> {
                // no conversion
            }
            default -> {
                throw new IllegalStateException("Unknown kind: " + kind);
            }

        }
    }

    /**
     * This method assumes there is a wrapper type on the top
     * of the stack and converts it to wrapper type. E.g. {@code Integer} is
     * converted to {@link int}, etc. If the value isn't primitive, it
     * stays as it is.
     *
     * @param cb code builder to emit instructions to
     * @param kind the type of the variable on be on the stack
     * @throws IllegalStateException if the {@code kind} isn't recognized
     */
    private static void wrapperToPrimitive(CodeBuilder cb, TypeKind kind) throws IllegalStateException {
        switch (kind) {
            case BOOLEAN -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_boolean);
                cb.checkcast(ConstantDescs.CD_Boolean);
                cb.invokevirtual(ConstantDescs.CD_Boolean, "booleanValue", type);
            }
            case BYTE -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_byte);
                cb.checkcast(ConstantDescs.CD_Byte);
                cb.invokevirtual(ConstantDescs.CD_Byte, "byteValue", type);
            }
            case CHAR -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_char);
                cb.checkcast(ConstantDescs.CD_Character);
                cb.invokevirtual(ConstantDescs.CD_Character, "charValue", type);
            }
            case SHORT -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_short);
                cb.checkcast(ConstantDescs.CD_Short);
                cb.invokevirtual(ConstantDescs.CD_Short, "shortValue", type);
            }
            case INT -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_int);
                cb.checkcast(ConstantDescs.CD_Integer);
                cb.invokevirtual(ConstantDescs.CD_Integer, "intValue", type);
            }
            case LONG -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_long);
                cb.checkcast(ConstantDescs.CD_Long);
                cb.invokevirtual(ConstantDescs.CD_Long, "longValue", type);
            }
            case FLOAT -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_float);
                cb.checkcast(ConstantDescs.CD_Float);
                cb.invokevirtual(ConstantDescs.CD_Float, "floatValue", type);
            }
            case DOUBLE -> {
                var type = MethodTypeDesc.of(ConstantDescs.CD_double);
                cb.checkcast(ConstantDescs.CD_Double);
                cb.invokevirtual(ConstantDescs.CD_Double, "doubleValue", type);
            }
            case REFERENCE -> {
                // no conversion
            }
            default -> {
                throw new IllegalStateException("Unknown kind: " + kind);
            }

        }
    }

    private static void loadFromArray(CodeBuilder cb, int argsArr, TypeKind typeKind, int slot) throws IllegalStateException {
        cb.aload(argsArr); // array with locals
        cb.loadConstant(slot); // index
        cb.arrayLoad(TypeKind.REFERENCE);
        wrapperToPrimitive(cb, typeKind);
    }

    private void storeToArray(CodeBuilder cb, int argsArr, TypeKind typeKind, int slot) throws IllegalStateException {
        cb.dup();
        wrapperToReference(cb, typeKind); // convert to wrapper - 3rd arg

        cb.aload(argsArr); // array with locals - 1st arg
        cb.dup_x1();
        cb.pop();

        cb.loadConstant(slot); // index - 2nd arg
        cb.dup_x1(); // copy value to store
        cb.pop();

        cb.arrayStore(TypeKind.REFERENCE);
    }

    private static int slotSize(ClassDesc desc) {
        var type = TypeKind.fromDescriptor(desc.descriptorString());
        return switch (type) {
            case DOUBLE -> 2;
            case LONG -> 2;
            default -> 1;
        };
    }

    private ConstantDesc fqn(ClassEntry clazz, MethodModel method, int line) {
        var methodName = "L" + clazz.asInternalName() + ";." + method.methodName() + method.methodTypeSymbol().descriptorString();
        return "" + line + ":" + methodName;
    }
}
