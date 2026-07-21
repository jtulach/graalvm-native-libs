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

import org.apidesign.jvm.insight.JvmInsight;

/* This is not a class to execute. Thus just: */
// $ exit

public final class Insight {
  public static void insightmain(String args, org.apidesign.jvm.insight.JvmInsight insight) throws Exception {
    var prefix = findPrefix(args);
    var in = new ThreadLocal<Boolean>();
    in.set(false);
    insight.configure((n) -> {
        var patch = n.jvmName().startsWith(prefix);
        if (patch) {
            System.err.println("[Insight] patch: " + n + " => " + patch);
        }
        return patch;
    }, (bldr) -> {
        bldr.when(JvmInsight.When.ENTER).roots(true).call((at, frame) -> {
            if (!in.get()) {
                try {
                    in.set(true);
                    System.err.println("[Insight] at: " + at + " frame: " + frame);
                } finally {
                    in.set(false);
                }
            }
        });
    });
  }

    private static String findPrefix(String args) throws IllegalArgumentException {
        System.err.println("[Insight] args: " + args);
        String prefix = null;
        for (var seg : args.split(",")) {
            var keyVal = seg.split("=");
            assert keyVal.length == 2;
            switch (keyVal[0]) {
                case "prefix" -> prefix = keyVal[1];
                default -> throw new IllegalArgumentException("Unknown key: " + keyVal[0]);
            }
        }   if (prefix == null) {
            throw new IllegalArgumentException("Specify: prefix=<prefix>");
        }
        return prefix;
    }
}
