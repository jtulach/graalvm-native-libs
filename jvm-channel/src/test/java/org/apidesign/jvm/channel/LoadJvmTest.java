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
import java.nio.file.Files;

import org.graalvm.nativeimage.ImageInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class LoadJvmTest {
    @BeforeEach
    public void initializeChannel() throws Exception {
        Assumptions.assumeTrue(ImageInfo.inImageCode(), "Can only run in Native Image mode");
    }

    @Test
    public void executeMainClass() throws Exception {
        if (!ImageInfo.inImageRuntimeCode()) {
            return;
        }
        var out = File.createTempFile("check-main", ".log");
        for (var i = 0; i < 5; i++) {
            var n = 10 + i;
            TestUtils.jvm().executeMain("org/apidesign/jvm/channel/ChannelFactorialTest", out.getPath(), "" + n);
            var content = Files.readString(out.toPath());
            assertEquals(ChannelFactorialTest.factorial(n).toString(), content, "Factorial of " + n + " is the same");
            out.delete();
        }
    }
}
