/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.cumber;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * @deprecated since 1.6.0, removing re-run feature
 */
@Deprecated
class FeatureMap {

  private static final String COLON = ":";

  private Map<String, Set<String>> addedFeatures = new HashMap<>();

  void addFeature(String failedScenario) {
    String featureName = failedScenario.substring(0, failedScenario.lastIndexOf(COLON));
    String failedLineNumber = failedScenario.substring(failedScenario.lastIndexOf(COLON) + 1,
        failedScenario.length());

    if (addedFeatures.containsKey(featureName)) {
      Set<String> featureFailedLines = addedFeatures.get(featureName);
      featureFailedLines.add(failedLineNumber);
      addedFeatures.put(featureName, featureFailedLines);
    } else {
      addedFeatures.put(featureName, Sets.newHashSet(failedLineNumber));
    }
  }

  void writeFeatures(PrintWriter out) {
    for (Map.Entry<String, Set<String>> feature : addedFeatures.entrySet()) {
      out.print(feature);
      Set<String> lines = feature.getValue();
      for (String line : lines) {
        out.print(COLON + line);
      }
      out.print(" ");
    }
  }
}
