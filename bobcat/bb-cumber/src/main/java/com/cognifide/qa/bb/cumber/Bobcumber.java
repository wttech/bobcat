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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.provider.selenium.webdriver.WebDriverRegistry;
import com.google.common.collect.Sets;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.Backend;
import cucumber.runtime.Runtime;
import cucumber.runtime.java.JavaBackend;
import cucumber.runtime.java.guice.impl.GuiceFactory;

/**
 * Classes annotated with {@code @RunWith(Bobcumber.class)} will run a Cucumber Feature
 */
public class Bobcumber extends Cucumber {

  private static final Logger LOG = LoggerFactory.getLogger(Bobcumber.class);

  private static final Map<String, Set<String>> ADDED_FEATURES = new HashMap<>();

  private static final String COLON = ":";

  private static final String FEATURE = "feature";

  private static final String RERUN_FAILED_TESTS_CLASS_NAME = "com.cognifide.qa.bobcumber.RerunFailedTests";

  private boolean storeFailedResults;

  private boolean noTestsToRerun;

  private File file;

  /**
   * Constructor called by JUnit.
   *
   * @param clazz the class with the @RunWith annotation.
   * @throws java.io.IOException                         if there is a problem
   * @throws org.junit.runners.model.InitializationError if there is another problem
   */
  public Bobcumber(Class<?> clazz) throws InitializationError, IOException {
    super(clazz);
    storeFailedResults = clazz.isAnnotationPresent(StoreFailedResults.class);
    if (storeFailedResults) {
      file = new File(clazz.getAnnotation(StoreFailedResults.class).value());
      if (!file.exists()) {
        PrintWriter writer = new PrintWriter(file, CharEncoding.UTF_8);
        writer.close();
      }
      if (RERUN_FAILED_TESTS_CLASS_NAME.equals(clazz.getName()) && isFeatureFileEmpty()) {
        noTestsToRerun = true;
      }
    }
  }

  @Override
  public void run(RunNotifier notifier) {
    if (noTestsToRerun) {
      notifier.fireTestFinished(Description.EMPTY);
      return;
    }
    if (storeFailedResults) {
      notifier.addListener(new RunListener() {
        public void testFailure(Failure failure) throws Exception {
          String trace = normalizeTrace(failure.getTrace());
          if (trace.contains(FEATURE)) {
            addScenario(trace);
          }
        }
      });
    }
    super.run(notifier);
    closeWebDriverPool();
  }
  private String normalizeTrace(String trace) {
    return trace.substring(trace.lastIndexOf("(") + 1, trace.lastIndexOf(")"));
  }

  private synchronized void addScenario(String failedScenario) throws IOException {

    String featureName = failedScenario.substring(0, failedScenario.lastIndexOf(COLON));
    String failedLineNumber =
        failedScenario.substring(failedScenario.lastIndexOf(COLON) + 1, failedScenario.length());

    if (ADDED_FEATURES.containsKey(featureName)) {
      Set<String> featureFailedLines = ADDED_FEATURES.get(featureName);
      featureFailedLines.add(failedLineNumber);
      ADDED_FEATURES.put(featureName, featureFailedLines);
    } else {
      ADDED_FEATURES.put(featureName, Sets.newHashSet(failedLineNumber));
    }

    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
    for (String feature : ADDED_FEATURES.keySet()) {
      out.print(feature);
      Set<String> lines = ADDED_FEATURES.get(feature);
      for (String line : lines) {
        out.print(COLON + line);
      }
      out.print(" ");
    }
    out.close();
  }

  @SuppressWarnings("unchecked")
  private void closeWebDriverPool() {
    try {
      Runtime runtime = (Runtime) FieldUtils.readField(this, "runtime", true);
      Collection<? extends Backend> backends =
          (Collection<? extends Backend>) FieldUtils.readField(runtime, "backends", true);
      for (Backend backend : backends) {
        if (backend instanceof JavaBackend) {
          GuiceFactory factory =
              (GuiceFactory) FieldUtils.readField(backend, "objectFactory", true);
          WebDriverRegistry webDriverRegistry = factory.getInstance(WebDriverRegistry.class);
          webDriverRegistry.shutdown();
        }
      }
    } catch (IllegalAccessException e) {
      LOG.error("unable to close web driver pool", e);
    }
  }

  private boolean isFeatureFileEmpty() throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      return br.readLine() == null;
    }
  }
}
