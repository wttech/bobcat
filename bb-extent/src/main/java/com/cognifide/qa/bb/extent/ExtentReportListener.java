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
package com.cognifide.qa.bb.extent;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.AbstractReporter;

import cucumber.runtime.java.guice.impl.GuiceFactory;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Row;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.Tag;

/**
 * Created by daniel.madejek on 2017-05-29.
 */
public class ExtentReportListener implements Reporter, Formatter {

  private static List<AbstractReporter> reporters;

  private static ExtentReports extentReports;

  private boolean isScenarioOutline;

  public ExtentReportListener() throws IOException {
    GuiceFactory guiceFactory = new GuiceFactory();
    if (reporters == null) {
      reporters = guiceFactory.getInstance(ExtentReporterFactory.class).getExtentReporters();
    }
    if (extentReports == null) {
      extentReports = new ExtentReports();
      extentReports.attachReporter(reporters.toArray(new AbstractReporter[reporters.size()]));
    }
    ExtentTests.setTestSteps(new LinkedList<>());
  }

  @Override
  public void syntaxError(String s, String s1, List<String> list, String s2, Integer integer) {
  }

  @Override
  public void uri(String s) {

  }

  @Override
  public void feature(Feature feature) {
    ExtentTest test =
        extentReports.createTest("Feature: " + feature.getName(), feature.getDescription());
    ExtentTests.setTestFeature(test);
    test.assignCategory(
        feature.getTags().stream().map(Tag::getName).toArray(size -> new String[size]));
  }

  @Override
  public void scenarioOutline(ScenarioOutline scenarioOutline) {
    isScenarioOutline = true;
    ExtentTest test = ExtentTests.getTestFeature()
        .createNode(scenarioOutline.getKeyword() + ": " + scenarioOutline.getName());
    ExtentTests.setTestScenarioOutline(test);

  }

  @Override
  public void examples(Examples examples) {
    addDataMarkupToTest(ExtentTests.getTestScenarioOutline(), examples.getRows());
  }

  @Override
  public void startOfScenarioLifeCycle(Scenario scenario) {
    ExtentTest scenarioTest;
    if (ExtentTests.getTestScenarioOutline() != null && scenario.getKeyword().trim()
        .equalsIgnoreCase("Scenario Outline")) {
      scenarioTest =
          ExtentTests.getTestScenarioOutline().createNode("Scenario: " + scenario.getName());
    } else {
      scenarioTest =
          ExtentTests.getTestFeature().createNode("Scenario: " + scenario.getName());
    }

    scenarioTest.assignCategory(
        scenario.getTags().stream().map(Tag::getName).toArray(size -> new String[size]));
    ExtentTests.setTestScenario(scenarioTest);
    isScenarioOutline = false;
  }

  @Override
  public void background(Background background) {

  }

  @Override
  public void scenario(Scenario scenario) {

  }

  @Override
  public void step(Step step) {
    if (isScenarioOutline) {
      return;
    }
    ExtentTests.getTestSteps().add(step);
  }

  @Override
  public void endOfScenarioLifeCycle(Scenario scenario) {

  }

  @Override
  public void done() {
    extentReports.flush();
  }

  @Override
  public void close() {

  }

  @Override
  public void eof() {

  }

  @Override
  public void before(Match match, Result result) {

  }

  @Override
  public void result(Result result) {

    if (!isScenarioOutline) {
      if (Result.SKIPPED.equals(result)) {
        ExtentTests.getTestStep().skip(Result.SKIPPED.getStatus());
      }
      if (Result.UNDEFINED.equals(result)) {
        ExtentTests.getTestStep().skip(Result.UNDEFINED.getStatus());
      }
      if (Result.PASSED.equals(result.getStatus())) {
        ExtentTests.getTestStep().pass(Result.PASSED);
      }
      if (Result.FAILED.equals(result.getStatus())) {
        ExtentTests.getTestStep().fail(result.getError());
      }
    }
  }

  @Override
  public void after(Match match, Result result) {

  }

  @Override
  public void match(Match match) {
    Step step = ExtentTests.getTestSteps().poll();
    ExtentTest stepTest =
        ExtentTests.getTestScenario().createNode(step.getKeyword() + ": " + step.getName());
    addDataMarkupToTest(stepTest, step.getRows());
    ExtentTests.setTestStep(stepTest);
  }

  @Override
  public void embedding(String s, byte[] bytes) {

  }

  @Override
  public void write(String s) {

  }

  private void addDataMarkupToTest(ExtentTest test, List<? extends Row> rows) {
    if (rows != null && rows.size() > 0) {
      String[][] data = new String[rows.size()][];
      for (int i = 0; i < rows.size(); i++) {
        List<String> dataTableRow = rows.get(i).getCells();
        data[i] = new String[dataTableRow.size()];
        for (int j = 0; j < dataTableRow.size(); j++) {
          data[i][j] = dataTableRow.get(j);
        }
      }
      test.info(MarkupHelper.createTable(data));
    }
  }
}
