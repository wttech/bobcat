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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.CharEncoding;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gherkin.formatter.model.BasicStatement;

class BobcumberListener extends RunListener {

  private static final Logger LOG = LoggerFactory.getLogger(BobcumberListener.class);

  private static final String FEATURE_STATEMENT = "feature";

  private static final String SCENARIO_STATEMENT = "Scenario";

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
    try (PrintWriter writer = new PrintWriter(bobcumber.getStatisticsFile(), CharEncoding.UTF_8)) {
      writer.println(scenarioCounter.get());
      writer.println(testFailureCounter.get());
    }
  }

  @Override
  public void testStarted(Description description) throws Exception {
    String keyword = getStatementKeyword(description);
    if (keyword.contains(SCENARIO_STATEMENT)) {
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
    return trace.substring(trace.lastIndexOf("(") + 1, trace.lastIndexOf(")"));
  }

  private synchronized void addScenario(String failedScenario) throws IOException {
    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(bobcumber.getFeatureFile(), false)))) {
      featureMap.addFeature(failedScenario);
      featureMap.writeFeatures(out);
    }
  }

  private synchronized String getStatementKeyword(Description description) {
    String keyword = "";
    try {
      Field privateSerializableField = Description.class.getDeclaredField("fUniqueId");
      privateSerializableField.setAccessible(true);
      Serializable statementCandidate = (Serializable) privateSerializableField.get(description);
      if (statementCandidate instanceof BasicStatement) {
        BasicStatement scenario = (BasicStatement) statementCandidate;
        keyword = scenario.getKeyword();
      }
    } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
      LOG.error("Cannot access Scenario object at: " + description.toString(), e);
    }
    return keyword;
  }
}
