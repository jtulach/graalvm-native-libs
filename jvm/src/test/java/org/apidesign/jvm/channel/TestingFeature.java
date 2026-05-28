package org.apidesign.jvm.channel;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;
import org.graalvm.nativeimage.hosted.RuntimeSerialization;

public final class TestingFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        var jvmPeerClass = access.findClassByName("org.apidesign.jvm.channel.JVMPeer");
        RuntimeReflection.register(jvmPeerClass);
        RuntimeReflection.register(jvmPeerClass.getConstructors());

        for (var clazz : ChannelMockInSingleJvmTest.class.getNestMembers()) {
            RuntimeSerialization.registerIncludingAssociatedClasses(clazz);
        }
    }
}
