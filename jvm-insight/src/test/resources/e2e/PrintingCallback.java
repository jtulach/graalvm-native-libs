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
package e2e;

import java.util.Map;
import java.util.function.BiConsumer;

/* This is not a class to execute. Thus just: */
// $ exit

public final class PrintingCallback implements BiConsumer<String, Map<String, Object>> {
    @Override
    public void accept(String methodName, Map<String, Object> localVars) {
        var msg = "[Callback]: Method " + methodName + " with local variables: " + localVars;
        System.err.println(msg);
    }
}
