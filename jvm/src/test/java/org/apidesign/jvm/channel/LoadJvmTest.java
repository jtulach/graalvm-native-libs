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
