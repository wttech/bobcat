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

import java.util.LinkedList;

import com.aventstack.extentreports.ExtentTest;

import gherkin.formatter.model.Step;

/**
 * Created by daniel.madejek on 2017-05-30.
 */
public class ExtentTests {

  private static ThreadLocal<ExtentTest> testFeature = new InheritableThreadLocal<>();

  private static ThreadLocal<ExtentTest> testScenario = new InheritableThreadLocal<>();

  private static ThreadLocal<LinkedList<Step>> testSteps = new ThreadLocal<LinkedList<Step>>();

  private static ThreadLocal<ExtentTest> testScenarioOutline = new InheritableThreadLocal<>();

  private static ThreadLocal<ExtentTest> testStep = new InheritableThreadLocal<>();

  public static ExtentTest getTestFeature() {
    return testFeature.get();
  }

  public static void setTestFeature(ExtentTest test) {
    testFeature.set(test);
  }

  public static ExtentTest getTestScenario() {
    return testScenario.get();
  }

  public static void setTestScenario(ExtentTest test) {
    testScenario.set(test);
  }

  public static ExtentTest getTestScenarioOutline() {
    return testScenarioOutline.get();
  }

  public static void setTestScenarioOutline(ExtentTest test) {
    testScenarioOutline.set(test);
  }

  public static LinkedList<Step> getTestSteps() {
    return testSteps.get();
  }

  public static void setTestSteps(LinkedList<Step> steps) {
    testSteps.set(steps);
  }

  public static ExtentTest getTestStep() {
    return testStep.get();
  }

  public static void setTestStep(ExtentTest test) {
    testStep.set(test);
  }

}
