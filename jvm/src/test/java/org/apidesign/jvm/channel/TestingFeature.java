package org.apidesign.jvm.channel;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;
import org.graalvm.nativeimage.hosted.RuntimeSerialization;

public final class TestingFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        RuntimeReflection.register(JVMPeer.class);
        RuntimeReflection.register(JVMPeer.class.getConstructors());

        RuntimeReflection.register(ChannelCountReturnTest.Conf.class);
        RuntimeReflection.register(ChannelCountReturnTest.Conf.class.getConstructors());

        for (var clazz : ChannelMockInSingleJvmTest.class.getNestMembers()) {
            RuntimeSerialization.registerIncludingAssociatedClasses(clazz);
        }
    }
}
