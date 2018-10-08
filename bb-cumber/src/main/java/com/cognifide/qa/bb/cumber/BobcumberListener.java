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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * @deprecated since 1.6.0, removing re-run feature
 */
@Deprecated
class BobcumberListener extends RunListener {

  private static final String FEATURE_STATEMENT = "feature";

  private static final String SCENARIO_STATEMENT = "Scenario";

  private static final String COLON = ":";

  private static final String PIPE = "|";

  private final Bobcumber bobcumber;

  private final FeatureMap featureMap;

  private final AtomicInteger scenarioCounter;

  private final AtomicInteger testFailureCounter;

  private boolean alreadyRegistered;

  BobcumberListener(Bobcumber bobcumber) {
    this.bobcumber = bobcumber;
    featureMap = new FeatureMap();
    scenarioCounter = new AtomicInteger();
    testFailureCounter = new AtomicInteger();
  }

  @Override
  public void testRunFinished(Result result) throws Exception {
    try (PrintWriter writer =
        new PrintWriter(bobcumber.getStatisticsFile(), StandardCharsets.UTF_8.name())) {
      writer.println(scenarioCounter.get());
      writer.println(testFailureCounter.get());
    }
  }

  @Override
  public void testStarted(Description description) throws Exception {
    String displayName = description.getDisplayName();
    if (isScenario(displayName) || isScenarioOutline(displayName)) {
      scenarioCounter.incrementAndGet();
      alreadyRegistered = false;
    }
  }

  @Override
  public void testFailure(Failure failure) throws Exception {
    String trace = normalizeTrace(failure.getTrace());
    if (trace.contains(FEATURE_STATEMENT)) {
      addScenario(trace);
      if (!alreadyRegistered) {
        testFailureCounter.incrementAndGet();
        alreadyRegistered = true;
      }
    }
  }

  private String normalizeTrace(String trace) {
    return trace.substring(trace.lastIndexOf('(') + 1, trace.lastIndexOf(')'));
  }

  private synchronized void addScenario(String failedScenario) throws IOException {
    try (PrintWriter out =
        new PrintWriter(
            new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                bobcumber.getFeatureFile(), false), StandardCharsets.UTF_8)))) {
      featureMap.addFeature(failedScenario);
      featureMap.writeFeatures(out);
    }
  }

  private boolean isScenario(String displayName) {
    boolean isScenario = false;
    if (displayName.contains(COLON)) {
      String testStep = StringUtils.substringBefore(displayName, COLON);
      isScenario = SCENARIO_STATEMENT.equals(testStep);
    }
    return isScenario;
  }

  private boolean isScenarioOutline(String displayName) {
    return displayName.startsWith(PIPE);
  }
}
