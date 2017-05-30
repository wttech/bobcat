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

import com.aventstack.extentreports.reporter.AbstractReporter;

import java.io.IOException;
import java.util.List;

import cucumber.runtime.java.guice.impl.GuiceFactory;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

/**
 * Created by daniel.madejek on 2017-05-29.
 */
public class ExtentReportListener implements Reporter, Formatter {

  private List<AbstractReporter> reporters;

  public ExtentReportListener() throws IOException {
    GuiceFactory guiceFactory = new GuiceFactory();
    reporters = guiceFactory.getInstance(ExtentReporterFactory.class).getExtentReporters();
  }

  @Override
  public void syntaxError(String s, String s1, List<String> list, String s2, Integer integer) {

  }

  @Override
  public void uri(String s) {

  }

  @Override
  public void feature(Feature feature) {

  }

  @Override
  public void scenarioOutline(ScenarioOutline scenarioOutline) {

  }

  @Override
  public void examples(Examples examples) {

  }

  @Override
  public void startOfScenarioLifeCycle(Scenario scenario) {

  }

  @Override
  public void background(Background background) {

  }

  @Override
  public void scenario(Scenario scenario) {

  }

  @Override
  public void step(Step step) {

  }

  @Override
  public void endOfScenarioLifeCycle(Scenario scenario) {

  }

  @Override
  public void done() {

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

  }

  @Override
  public void after(Match match, Result result) {

  }

  @Override
  public void match(Match match) {

  }

  @Override
  public void embedding(String s, byte[] bytes) {

  }

  @Override
  public void write(String s) {

  }
}
