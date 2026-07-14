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
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.CodeModel;
import java.lang.classfile.Label;
import java.lang.classfile.MethodModel;
import java.lang.classfile.TypeKind;
import java.lang.classfile.attribute.LocalVariableInfo;
import java.lang.classfile.attribute.LocalVariableTableAttribute;
import java.lang.classfile.attribute.StackMapFrameInfo;
import java.lang.classfile.instruction.IncrementInstruction;
import java.lang.classfile.instruction.LineNumber;
import java.lang.classfile.instruction.LoadInstruction;
import java.lang.classfile.instruction.LocalVariable;
import java.lang.classfile.instruction.LocalVariableType;
import java.lang.classfile.instruction.ReturnInstruction;
import java.lang.classfile.instruction.StoreInstruction;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DirectMethodHandleDesc;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

/** Transformer patching byte code to be {@link JvmInsight}-ready.
 */
final class JvmInsightTransform implements ClassTransform, Consumer<ClassBuilder> {
    private final ClassModel model;
    private final ClassDesc callbackClass;
    private boolean cinitDone;

    private JvmInsightTransform(ClassModel clazz) {
        this.model = clazz;
        this.callbackClass = ClassDesc.of("java.util.function.Consumer");
    }

    static ClassTransform create(ClassModel clazz) {
        var jvm = new JvmInsightTransform(clazz);
        var end = ClassTransform.endHandler(jvm);
        return jvm.andThen(end);
    }

    /** Finish rebuilding of the class.
     *
     * @param builder code builder to use
     */
    @Override
    public void accept(ClassBuilder builder) {
        if (!cinitDone) {
            builder.withMethod(ConstantDescs.CLASS_INIT_NAME, MethodTypeDesc.of(ConstantDescs.CD_void), ClassFile.ACC_STATIC, (t) -> {
                t.withCode((cb) -> {
                    onClassEnter(cb);
                    cb.return_();
                });
            });
        }
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
                        if (method.methodName().equalsString(ConstantDescs.CLASS_INIT_NAME)) {
                            onClassEnter(cb);
                            cinitDone = true;
                        }

                        var firstLabel = cb.newLabel();
                        var lastLabel = cb.newLabel();
                        Label enterLabel = null;
                        var methodType = method.methodTypeSymbol();
                        var localTypes = new HashMap<Integer, VarInfo>();
                        var locals = new HashMap<Integer, VarInfo>();
                        int argsArr; // slot with reference to array with values
                        int argsNames; // slot with reference to variable names
                        {
                            var mapOpt = opt.get().localVariables().stream().mapToInt(LocalVariableInfo::slot).max();
                            var maxSlotIndex = mapOpt.isPresent() ? mapOpt.getAsInt() + 1 : 1;

                            // copies all the values into an argument
                            cb.loadConstant(maxSlotIndex);
                            cb.anewarray(ConstantDescs.CD_Object);
                            for (var i = 0; i < methodType.parameterCount(); i++) {
                                var typeDescr = methodType.parameterType(i); // type of i-th parameter
                                var slot = cb.parameterSlot(i); // slot for i-th parameter

                                cb.dup(); // arrayref
                                cb.loadConstant(slot); // index
                                loadObjectWraper(cb, typeDescr, slot); // value
                                cb.arrayStore(TypeKind.REFERENCE);
                            }
                            if (!method.flags().has(AccessFlag.STATIC) && !method.methodName().equalsString(ConstantDescs.INIT_NAME)) {
                                cb.dup();
                                cb.loadConstant(0); // this
                                cb.aload(0);
                                cb.arrayStore(TypeKind.REFERENCE);
                            }
                            argsArr = maxSlotIndex;
                            cb.localVariable(argsArr, "$JvmInsight$locals",
                                ClassDesc.ofDescriptor("[Ljava/lang/Object;"),
                                firstLabel, lastLabel
                            );
                            cb.astore(argsArr);

                            argsNames = maxSlotIndex + 1;
                            // copies all the values into an argument
                            cb.loadConstant(maxSlotIndex);
                            cb.anewarray(ConstantDescs.CD_String);
                            for (var localVar : opt.get().localVariables()) {
                                var slot = localVar.slot();
                                if (localVar.startPc() == 0) {
                                    final VarInfo info = new VarInfo(localVar);
                                    localTypes.put(slot, info);
                                    locals.put(slot, info);

                                    cb.dup(); // arrayref
                                    cb.loadConstant(slot); // index
                                    cb.loadConstant(info.name()); // value
                                    cb.arrayStore(TypeKind.REFERENCE);
                                }
                            }
                            cb.localVariable(argsNames, "$JvmInsight$names",
                                ClassDesc.ofDescriptor("[Ljava/lang/String;"),
                                firstLabel, lastLabel
                            );
                            cb.astore(argsNames);

                            cb.labelBinding(firstLabel);
                        }
                        var endOfStatement = new Object() {
                            LineNumber lastLine;

                            final void endOfLine() {
                                if (lastLine != null) {
                                    onHook("return", "statements", method, lastLine.line(), argsNames, argsArr, cb);
                                    lastLine = null;
                                }
                            }
                        };
                        // System.err.println("method: " + method.methodName().stringValue());
                        var stackList = new ArrayList<ClassDesc>();
                        for (var instr : code.elementList()) {
                            // System.err.println("  instr: " + instr);
                            if (instr instanceof LocalVariableInfo localVar) {
                                var info = new VarInfo(localVar);
                                localTypes.put(localVar.slot(), info);
                            }
                            if (instr instanceof LocalVariableType) {
                                // types aren't stored in local variables, skip
                                continue;
                            }
                            if (instr instanceof LoadInstruction load) {
                                var info = localTypes.get(load.slot());
                                if (info == null) {
                                    info = new VarInfo(null, load.slot(), ConstantDescs.CD_Object, null, null);
                                    localTypes.put(load.slot(), info);
                                }
                                locals.put(load.slot(), info);
                                if (load.slot() > 0 || method.flags().has(AccessFlag.STATIC)) {
                                    var type = info.typeSymbol();
                                    stackList.add(type);
                                    loadFromArray(cb, argsArr, type, load.slot());
                                    continue;
                                }
                            }
                            if (instr instanceof StoreInstruction store) {
                                var initializedVar = localTypes.get(store.slot());
                                if (initializedVar == null && store.typeKind() != TypeKind.REFERENCE) {
                                    var info = new VarInfo(
                                        null, store.slot(),
                                        store.typeKind().upperBound(), null, null
                                    );
                                    localTypes.put(store.slot(), info);
                                }
                                if (initializedVar != null) {
                                    // initializedVar can be null when there is no debug info
                                    locals.put(store.slot(), initializedVar);
                                }
                                if (store.slot() > 0 || method.flags().has(AccessFlag.STATIC)) {
                                    var info = localTypes.get(store.slot());
                                    if (info == null) {
                                        // read from the stack
                                        var fromStack = stackList.removeLast();
                                        info = new VarInfo(null, store.slot(), fromStack, null, null);
                                        localTypes.put(store.slot(), info);
                                    }
                                    var type = info.typeSymbol();
                                    storeToArray(cb, argsArr, type, store.slot());
                                    continue;
                                }
                            }
                            if (instr instanceof IncrementInstruction inc) {
                                loadFromArray(cb, argsArr, ConstantDescs.CD_int, inc.slot());
                                cb.loadConstant(inc.constant());
                                cb.iadd();
                                storeToArray(cb, argsArr, ConstantDescs.CD_int, inc.slot());
                                continue;
                            }
                            if (instr instanceof ReturnInstruction ret) {
                                endOfStatement.endOfLine();
                                onHook("return", "roots", method, -1, argsNames, argsArr, cb);
                            }

                            cb.with(instr);

                            if (instr instanceof Label label) {
                                if (enterLabel == null) {
                                    onHook("enter", "roots", method, -1, argsNames, argsArr, cb);
                                    enterLabel = label;
                                }
                                var optStack = code.findAttribute(Attributes.stackMapTable());
                                if (optStack.isPresent()) {
                                    for (var stEn : optStack.get().entries()) {
                                        if (stEn.target() == label) {
                                            // System.err.println("found entry: " + stEn);
                                            stackList.clear();
                                            for (var s : stEn.stack()) {
                                                stackList.add(findTypeForStackMapInfo(s));
                                            }
                                            // System.err.println("  with stack : " + stackList);
                                            var cnt = 0;
                                            for (var verify : stEn.locals()) {
                                                var type = findTypeForStackMapInfo(verify);
                                                var prev = localTypes.get(cnt);
                                                var info = new VarInfo(
                                                    prev == null ? null : prev.name(),
                                                    cnt, type, label,
                                                    prev == null ? null : prev.endScope()
                                                );
                                                localTypes.put(cnt, info);
                                                cnt++;
                                            }
                                        }
                                    }
                                }
                                try (
                                    var updateNamesArr = new AutoCloseable() {
                                        private boolean ready;

                                        void putName(int index, String name) {
                                            if (!ready) {
                                                ready = true;
                                                cb.aload(argsNames);
                                            }
                                            cb.dup(); // array for store
                                            cb.loadConstant(index); // index
                                            cb.loadConstant(name); // value
                                            cb.aastore();
                                        }

                                        @Override
                                        public void close() {
                                            if (ready) {
                                                cb.pop();
                                            }
                                        }
                                    }
                                ) {
                                    var it = locals.entrySet().iterator();
                                    while (it.hasNext()) {
                                        var en = it.next();
                                        if (en.getValue().endScope() == label) {
                                            updateNamesArr.putName(en.getValue().slot(), null);
                                            it.remove();
                                        }
                                    }
                                    for (var info : localTypes.values()) {
                                        if (info.startScope() == label) {
                                            locals.put(info.slot(), info);
                                            updateNamesArr.putName(info.slot(), info.name());
                                        }
                                    }
                                }
                            }
                            if (instr instanceof LineNumber line) {
                                endOfStatement.endOfLine();
                                endOfStatement.lastLine = line;
                                onHook("enter", "statements", method, line.line(), argsNames, argsArr, cb);
                            }
                        }
                        cb.labelBinding(lastLabel);
                        var isConstructor = method.methodName().equalsString(ConstantDescs.INIT_NAME);
                        if (!isConstructor && enterLabel != null) {
                            var onReturnExceptional = cb.newBoundLabel();
                            onHook("return", "roots", method, -1, argsNames, argsArr, cb);
                            cb.athrow();
                            cb.exceptionCatchAll(enterLabel, lastLabel, onReturnExceptional);
                        }
                    });
                } else {
                    mb.with(me);
                }
            });
        } else {
            builder.with(element);
        }
    }

    private record VarInfo(String name, int slot, ClassDesc typeSymbol, Label startScope, Label endScope) {
        VarInfo(LocalVariableInfo localVar) {
            this(
                localVar.name().stringValue(), localVar.slot(), localVar.typeSymbol(),
                localVar instanceof LocalVariable var ? var.startScope() : null,
                localVar instanceof LocalVariable var ? var.endScope() : null
            );
        }
    }

    private void onHook(String type, String fieldName, MethodModel method, int line, int argsNames, int argsArr, CodeBuilder cb) {
        var boot = bootMetafactory();
        var thizClass = model.thisClass().constantValue();
        var ref = DynamicCallSiteDesc.of(
            boot, fieldName,
            MethodTypeDesc.of(callbackClass),
            type, thizClass,
            method.methodName().stringValue(), method.methodTypeSymbol().descriptorString(),
            line
        );
        cb.invokedynamic(ref);
        var noCallback = cb.newLabel();
        cb.ifnull(noCallback);
        cb.invokedynamic(ref);
        var argumentCount = 0;

        {
            argumentCount += 2;
            // array with names
            cb.loadConstant("names");
            cb.aload(argsNames);
        }

        {
            argumentCount += 2;
            // array with values
            cb.loadConstant("values");
            cb.aload(argsArr);
        }
        var Map = ClassDesc.of("java.util.Map");
        var ofArgsType = MethodTypeDesc.of(Map, Collections.nCopies(argumentCount, ConstantDescs.CD_Object));
        cb.invokestatic(Map, "of", ofArgsType, true);
        var consumeType = MethodTypeDesc.of(ConstantDescs.CD_void, ConstantDescs.CD_Object);
        cb.invokeinterface(callbackClass, "accept", consumeType);
        cb.labelBinding(noCallback);
    }

    private void onClassEnter(CodeBuilder cb) {
        String initEvent = "init";
        String initType = "enter";
        var boot = bootMetafactory();
        var thizClass = model.thisClass().constantValue();
        var ref = DynamicCallSiteDesc.of(
            boot, initEvent,
            MethodTypeDesc.of(callbackClass),
            initType, thizClass,
            "", "",
            -1
        );
        cb.invokedynamic(ref);
    }

    private DirectMethodHandleDesc bootMetafactory() {
        var insightClazz = ClassDesc.of(JvmInsight.class.getName());
        var boot = ConstantDescs.ofCallsiteBootstrap(
                insightClazz, "metafactory", ConstantDescs.CD_CallSite,
                ConstantDescs.CD_String, ConstantDescs.CD_Class,
                ConstantDescs.CD_String, ConstantDescs.CD_String,
                ConstantDescs.CD_int
        );
        return boot;
    }

    private void loadObjectWraper(CodeBuilder cb, ClassDesc typeSymbol, int slot) {
        var kind = TypeKind.fromDescriptor(typeSymbol.descriptorString());
        cb.loadLocal(kind, slot);
        wrapperToReference(cb, typeSymbol);
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
    private static void wrapperToReference(CodeBuilder cb, ClassDesc expType) throws IllegalStateException {
        var kind = TypeKind.fromDescriptor(expType.descriptorString());
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
     * @param wrapperType the type of the variable on be on the stack
     * @throws IllegalStateException if the {@code kind} isn't recognized
     */
    private static void wrapperToPrimitive(CodeBuilder cb, ClassDesc wraperType) throws IllegalStateException {
        var kind = TypeKind.fromDescriptor(wraperType.descriptorString());
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
                // no conversion, but check for proper type
                cb.checkcast(wraperType);
            }
            default -> {
                throw new IllegalStateException("Unknown kind: " + kind);
            }

        }
    }

    private static void loadFromArray(CodeBuilder cb, int argsArr, ClassDesc type, int slot) throws IllegalStateException {
        cb.aload(argsArr); // array with locals
        cb.loadConstant(slot); // index
        cb.arrayLoad(TypeKind.REFERENCE);
        wrapperToPrimitive(cb, type);
    }

    private void storeToArray(CodeBuilder cb, int argsArr, ClassDesc type, int slot) throws IllegalStateException {
        wrapperToReference(cb, type); // convert to wrapper - 3rd arg

        cb.aload(argsArr); // array with locals - 1st arg
        cb.dup_x1();
        cb.pop();

        cb.loadConstant(slot); // index - 2nd arg
        cb.dup_x1(); // copy value to store
        cb.pop();

        cb.arrayStore(TypeKind.REFERENCE);
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
