/*-
 * #%L
 * Bobcat Parent
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.CharEncoding;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import com.google.common.collect.Sets;

/**
 * Classes annotated with {@code @RunWith(BobcumberWithFailedRecorder.class)} will run a Cucumber Feature
 * and store list of failed tests in target/rerun.properties file.
 */
public class BobcumberWithFailedRecorder extends Bobcumber {

  private static final String FILE_NAME = "target/rerun.properties";

  private static final Map<String, Set<String>> ADDED_FEATURES = new HashMap<>();

  private static final String COLON = ":";

  /**
   * Constructor called by JUnit.
   *
   * @param clazz the class with the @RunWith annotation.
   * @throws IOException         if there is a problem
   * @throws InitializationError if there is another problem
   */
  public BobcumberWithFailedRecorder(Class<?> clazz) throws InitializationError, IOException {
    super(clazz);
  }

  @Override
  public void run(RunNotifier notifier) {
    notifier.addListener(new RunListener() {
      public void testFailure(Failure failure) throws Exception {
        String trace = failure.getTrace();
        trace = trace.substring(trace.lastIndexOf("(") + 1, trace.lastIndexOf(")"));
        addScenario(trace);
      }
    });
    super.run(notifier);
    closeWebDriverPool();
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

    File file = new File(FILE_NAME);
    if (!file.exists()) {
      PrintWriter writer = new PrintWriter(file, CharEncoding.UTF_8);
      writer.close();
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
}
