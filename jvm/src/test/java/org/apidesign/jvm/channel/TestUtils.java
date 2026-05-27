package org.apidesign.jvm.channel;

import java.io.File;
import java.net.URISyntaxException;
import org.graalvm.nativeimage.ImageInfo;

final class TestUtils {
    private TestUtils() {
    }

    private static final String PATH = System.getProperty("java.home");
    private static final String CLASS_PATH;

    static {
        try {
            var url = TestUtils.class.getProtectionDomain().getCodeSource().getLocation();
            CLASS_PATH = new File(url.toURI()).getPath();
        } catch (URISyntaxException ex) {
            throw new AssertionError(ex);
        }
    }

    private static JVM impl;
    static JVM jvm() {
        if (impl == null) {
            assert ImageInfo.inImageRuntimeCode();
            assert CLASS_PATH != null : "CLASS_PATH field must be set!";
            var path = new File(PATH);
            assert path.isDirectory() : "Java home exists: " + path;
            impl = JVM.create(path, "-Djava.class.path=" + CLASS_PATH);
        }
        return impl;
    }
}
