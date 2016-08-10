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
package com.cognifide.qa.bb.junit.concurrent;

import java.util.Arrays;
import java.util.List;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

final class ConcurrentRunnerBuilder extends AllDefaultPossibilitiesBuilder {

  ConcurrentRunnerBuilder(boolean canUseSuiteMethod) {
    super(canUseSuiteMethod);
  }

  @Override
  public Runner runnerForClass(Class<?> testClass) {
    RunnerBuilder concurrentRunner = new RunnerBuilder() {
      @Override
      public Runner runnerForClass(Class<?> testClass) throws InitializationError {
        Concurrent annotation = testClass.getAnnotation(Concurrent.class);
        if (annotation != null) {
          return new ConcurrentJunitRunner(testClass);
        }
        return null;
      }
    };
    List<RunnerBuilder> builders =
        Arrays.asList(
            concurrentRunner,
            ignoredBuilder(),
            annotatedBuilder(),
            suiteMethodBuilder(),
            junit3Builder(),
            junit4Builder());

    for (RunnerBuilder each : builders) {
      Runner runner = each.safeRunnerForClass(testClass);
      if (runner != null) {
        return runner;
      }
    }
    return null;
  }
}
