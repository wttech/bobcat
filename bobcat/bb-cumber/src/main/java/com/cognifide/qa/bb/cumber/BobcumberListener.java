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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

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

  private boolean failureRegistered;

  BobcumberListener(Bobcumber bobcumber) {
    this.bobcumber = bobcumber;
    featureMap = new FeatureMap();
    scenarioCounter = new AtomicInteger();
    testFailureCounter = new AtomicInteger();
  }

  @Override
  public void testRunFinished(Result result) throws Exception {
    updateStatisticsFile();
    updateFailedFeaturesFile();
  }

  @Override
  public void testStarted(Description description) throws Exception {
    String keyword = getStatementKeyword(description);
    if (keyword.contains(SCENARIO_STATEMENT)) {
      scenarioCounter.incrementAndGet();
      failureRegistered = false;
    }
  }

  @Override
  public void testFailure(Failure failure) throws Exception {
    String trace = normalizeTrace(failure.getTrace());
    if (trace.contains(FEATURE_STATEMENT)) {
      if (!failureRegistered) {
        addScenario(trace);
        testFailureCounter.incrementAndGet();
        failureRegistered = true;
      }
    }
  }

  private String normalizeTrace(String trace) {
    return trace.substring(trace.lastIndexOf("(") + 1, trace.lastIndexOf(")"));
  }

  /**
   * This method opens connection to file and if file is empty, it cause action on file specified in action param.
   * It is used to report information about failed tests and some statistics.
   */
  private synchronized void fillEmptyBobcumberFile(File file, ActionOnFile action)
      throws FileNotFoundException {
    try (Scanner scanner = new Scanner(file)) {
      if (!scanner.hasNext()) {
        try (PrintWriter writer = new PrintWriter(file)) {
          action.action(writer);
        }
      }
    }
  }

  private synchronized void addScenario(String failedScenario) {
    featureMap.addFeature(failedScenario);
  }

  private String getStatementKeyword(Description description) {
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

  private void updateStatisticsFile() throws FileNotFoundException {
    fillEmptyBobcumberFile(bobcumber.getStatisticsFile(), writer -> {
      writer.println(scenarioCounter.get());
      writer.println(testFailureCounter.get());
    });
  }

  private void updateFailedFeaturesFile() throws FileNotFoundException {
    fillEmptyBobcumberFile(bobcumber.getFeatureFile(), writer ->
        featureMap.writeFeatures(writer));
  }

  private interface ActionOnFile {
    void action(PrintWriter writer);
  }
}
