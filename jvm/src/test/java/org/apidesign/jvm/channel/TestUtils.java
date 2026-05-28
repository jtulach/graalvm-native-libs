package org.apidesign.jvm.channel;

import java.io.File;
import java.net.URISyntaxException;
import org.graalvm.nativeimage.ImageInfo;
import org.junit.jupiter.api.Assertions;

final class TestUtils {
    private TestUtils() {
    }

    private static final String PATH = System.getProperty("java.home");
    private static final String CLASS_PATH = pathForClasses(
        Channel.class,
        TestUtils.class,
        Assertions.class
    );

    private static String pathForClasses(Class<?>... classes) {
        var sb = new StringBuilder();
        var sep = "";
        for (var c : classes) {
            try {
                var url = c.getProtectionDomain().getCodeSource().getLocation();
                var file = new File(url.toURI()).getPath();
                sb.append(sep);
                sb.append(file);
                sep = File.pathSeparator;
            } catch (URISyntaxException ex) {
                throw new AssertionError(ex);
            }
        }
        return sb.toString();
    }

    private static JVM impl;

    /**
     * Creates an instance of {@link JVM} to perform some tests with it. Please
     * note: <strong>there can be only one HotSpot JVM</strong> per process. Always
     * use this method in tests. Avoid creating another JVMs directly.
     *
     * @return singleton JVM when in native image mode or {@code null}
     *    when in HotSpot mode
     */
    static JVM jvm() {
        if (impl == null && ImageInfo.inImageRuntimeCode()) {
            assert CLASS_PATH != null : "CLASS_PATH field must be set!";
            var path = new File(PATH);
            assert path.isDirectory() : "Java home exists: " + path;
            impl = JVM.create(path, "-Djava.class.path=" + CLASS_PATH);
        }
        return impl;
    }
}
