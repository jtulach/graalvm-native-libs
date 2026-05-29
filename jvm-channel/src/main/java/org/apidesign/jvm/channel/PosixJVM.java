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

import java.io.File;
import java.util.List;
import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.constant.CConstant;
import org.graalvm.nativeimage.c.function.CFunction;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.word.PointerBase;

@CContext(PosixJVM.Direct.class)
final class PosixJVM {

    static JNIBoot.JNICreateJavaVMPointer loadImpl(String libJvmPath) {
        try (var libPath = CTypeConversion.toCString(libJvmPath); var createJvm = CTypeConversion.toCString("JNI_CreateJavaVM")) {
            var jvmSo = dlopen(libPath.get(), RTLD_NOW());
            if (jvmSo.isNull()) {
                var err = new StringBuilder("Cannot load ").append(libJvmPath);
                err.append(" error: ").append(CTypeConversion.toJavaString(dlerror()));
                throw new AssertionError(err.toString());
            }
            JNIBoot.JNICreateJavaVMPointer sym = dlsym(jvmSo, createJvm.get());
            if (sym.isNull()) {
                throw new AssertionError("No such symbol found in " + libJvmPath);
            }
            return sym;
        }
    }

    static File findDynamicLibrary(File javaHome) {
        var libName = System.mapLibraryName("jvm");
        assert libName != null;
        var lib = new File(new File(new File(javaHome, "lib"), "server"), libName);
        if (!lib.exists()) {
            throw new IllegalStateException("Cannot find " + lib);
        }
        return lib;
    }

    @CConstant
    static native int RTLD_NOW();

    @CFunction
    static native PointerBase dlopen(CCharPointer file, int mode);

    @CFunction(transition = CFunction.Transition.NO_TRANSITION)
    static native <T extends PointerBase> T dlsym(PointerBase handle, CCharPointer name);

    @CFunction
    static native CCharPointer dlerror();

    static final class Direct implements CContext.Directives {

        @Override
        public boolean isInConfiguration() {
            return Platform.includedIn(Platform.LINUX.class)
                    || Platform.includedIn(Platform.DARWIN.class);
        }

        @Override
        public List<String> getHeaderFiles() {
            return List.of("<dlfcn.h>");
        }
    }
}
