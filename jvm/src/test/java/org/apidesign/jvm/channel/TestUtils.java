package org.apidesign.jvm.channel;

import java.io.File;
import java.net.URISyntaxException;

final class TestUtils {
    private TestUtils() {
    }

    static final String PATH = System.getProperty("java.home");
    static final String CLASS_PATH;

    static {
        try {
            var url = TestUtils.class.getProtectionDomain().getCodeSource().getLocation();
            CLASS_PATH = new File(url.toURI()).getPath();
        } catch (URISyntaxException ex) {
            throw new AssertionError(ex);
        }
    }

}
