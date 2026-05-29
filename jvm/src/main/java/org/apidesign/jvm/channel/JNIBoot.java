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
package org.apidesign.jvm.channel;

import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.function.CFunctionPointer;
import org.graalvm.nativeimage.c.function.InvokeCFunctionPointer;
import org.graalvm.nativeimage.c.struct.CField;
import org.graalvm.nativeimage.c.struct.CStruct;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.WordPointer;
import org.graalvm.word.PointerBase;

/**
 * Java virtual machine initialization API.
 */
@CContext(JNIDirectives.class)
final class JNIBoot {

    interface JNICreateJavaVMPointer extends CFunctionPointer {

        @InvokeCFunctionPointer
        int call(JNI.JavaVMPointer jvmptr, JNI.JNIEnvPointer env, Args args);
    }

    @CStruct("JavaVMInitArgs")
    interface Args extends PointerBase {

        @CField
        int version();

        @CField
        void version(int v);

        @CField
        int nOptions();

        @CField
        void nOptions(int n);

        @CField
        Option options();

        @CField
        void options(Option ptr);

        @CField
        boolean ignoreUnrecognized();

        @CField
        void ignoreUnrecognized(boolean v);
    }

    @CStruct(value = "JavaVMOption")
    interface Option extends PointerBase {

        @CField("optionString")
        CCharPointer getOptionString();

        @CField("optionString")
        void setOptionString(CCharPointer value);

        @CField("extraInfo")
        WordPointer getExtraInfo();

        @CField("extraInfo")
        void setExtraInfo(WordPointer value);

        Option addressOf(int index);
    }
}
