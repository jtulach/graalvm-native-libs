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
package org.apidesign.jvm.channel.hosted;

import java.lang.reflect.Modifier;
import org.apidesign.jvm.channel.Channel;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

public final class ChannelFeature implements Feature {
    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        access.registerSubtypeReachabilityHandler((acc, clazz) -> {
            if ((clazz.getModifiers() & Modifier.ABSTRACT) == 0) {
                RuntimeReflection.registerForReflectiveInstantiation(clazz);
            }
        }, Channel.Config.class);
    }

}
