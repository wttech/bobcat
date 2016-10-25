/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.cumber;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

import com.cognifide.qa.bb.modules.CoreModule;
import com.cognifide.qa.bb.quarantine.QuarantinedElement;
import com.cognifide.qa.bb.quarantine.TestsQuarantine;
import com.google.common.collect.Sets;
import com.google.inject.Guice;

public class QuarantineAwareRunListener extends RunListener {

  private final TestsQuarantine quarantine;

  private final Description parentDescription;

  private final RunNotifier runNotifier;

  private Description lastFailedDescription;

  public QuarantineAwareRunListener(Description parentDescription, RunNotifier runNotifier) {
    this.parentDescription = parentDescription;
    this.runNotifier = runNotifier;
    this.quarantine = Guice.createInjector(new CoreModule()).getInstance(TestsQuarantine.class);
  }

  @Override
  public void testAssumptionFailure(Failure failure) {
    runNotifier.fireTestAssumptionFailed(failure);
  }

  @Override
  public void testFinished(Description description) throws Exception {
    String featureName = getFeatureName(parentDescription.getChildren().get(0));
    String scenarioName = getScenarioName(description);
    if (quarantine.getQuarantined().contains(new QuarantinedElement(featureName, scenarioName))) {
      if (!description.equals(lastFailedDescription)) {
        runNotifier.fireTestFinished(description);
      }
    } else  {
      runNotifier.fireTestFinished(description);
    }
  }

  @Override
  public void testRunFinished(Result result) throws Exception {
    runNotifier.fireTestRunFinished(result);
  }

  @Override
  public void testIgnored(Description description) throws Exception {
    runNotifier.fireTestIgnored(description);
  }

  @Override
  public void testFailure(Failure failure) throws Exception {
    String featureName = getFeatureName(parentDescription.getChildren().get(0));
    String scenarioName = getScenarioName(failure.getDescription());
    if (quarantine.getQuarantined().contains(new QuarantinedElement(featureName, scenarioName))) {
      if (failure.getDescription().isTest()) {
        lastFailedDescription = failure.getDescription();
        runNotifier.fireTestIgnored(lastFailedDescription);
      }
    } else {
      runNotifier.fireTestFailure(failure);
    }
  }

  @Override
  public void testRunStarted(Description description) throws Exception {
    runNotifier.fireTestRunStarted(description);
  }

  @Override
  public void testStarted(Description description) throws Exception {
    runNotifier.fireTestStarted(description);
  }

  private String getScenarioName(Description description) {
    return description.getClassName().replace("Scenario:", "").trim();
  }

  private String getFeatureName(Description description) {
    return description.getClassName().replace("Feature:", "").trim();
  }




}
