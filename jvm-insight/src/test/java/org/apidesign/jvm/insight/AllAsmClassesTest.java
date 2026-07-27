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

import java.lang.classfile.ClassFile;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllAsmClassesTest {
    @ParameterizedTest(name = "transforming {0}")
    @MethodSource("allClassesInAsmModule")
    public void processAsmClass(Object info) throws Exception {
        var name = (String)info.getClass().getMethod("getName").invoke(info);
        var bytes = (byte[])info.getClass().getMethod("getBytes").invoke(info);

        var cf = ClassFile.of();
        var model = cf.parse(bytes);
        if (switch (model.thisClass().name().stringValue()) {
            case "jdk3/ArtificialStructures" -> true;
            case "jdk8/AllFrames" -> true;
            default -> false;
        }) {
            return;
        }
        var trans = JvmInsightTransform.create(model);
        try {
            var arr = cf.transformClass(model, trans);

            assertTrue(arr.length > bytes.length, "Processing of " + name + " yields " + arr.length + " bytes");
        } catch (Throwable t) {
            throw new AssertionError("Processing " + name, t);
        }
    }

    public static Stream<Object> allClassesInAsmModule() throws Exception {
        var clazz = Class.forName("org.objectweb.asm.test.AsmTest");
        var method = clazz.getMethod("allClassesAndAllApis");
        return (Stream<Object>) method.invoke(null);
    }
}
