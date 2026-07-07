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

import java.io.IOException;
import java.lang.classfile.ClassFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JvmInsightTransformTest {

    public JvmInsightTransformTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testTransformArrayList() throws Exception {
        assertTransform("java/util/ArrayList.class");
    }

    private static void assertTransform(String rsrc) throws IOException {
        var is = ClassLoader.getPlatformClassLoader().getResourceAsStream(rsrc);
        assertNotNull(is, "Resource " + rsrc + " found");
        var arr = is.readAllBytes();
        var file = ClassFile.of();
        var model = file.parse(arr);
        var transformer = JvmInsightTransform.create(model);
        var data = file.transformClass(model, transformer);
        assertNotNull(data, "Some data generated");
    }

}
