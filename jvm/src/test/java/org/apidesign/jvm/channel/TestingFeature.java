package org.apidesign.jvm.channel;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeSerialization;

public final class TestingFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        for (var clazz : ChannelMockInSingleJvmTest.class.getNestMembers()) {
            RuntimeSerialization.registerIncludingAssociatedClasses(clazz);
        }
    }
}
