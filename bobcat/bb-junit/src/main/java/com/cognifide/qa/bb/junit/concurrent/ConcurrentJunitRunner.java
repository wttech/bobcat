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

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * This is a concurrent test runner. ConcurrentSuite will create an instance of this runner if it finds a test
 * class annotated with Concurrent annotation.
 */
public class ConcurrentJunitRunner extends BlockJUnit4ClassRunner {
  /**
   * <p>
   * Constructs ConcurrentJunitRunner.
   * </p>
   * <p>
   * If the Concurrent annotation is present, number of threads will be taken from there. Otherwise,
   * constructor will calculate the number of threads as "#availableProcessors * 1.5".
   * </p>
   *
   * @param type - class
   * @throws InitializationError - if the test class is malformed
   */
  public ConcurrentJunitRunner(final Class<?> type) throws InitializationError {
    super(type);
    setScheduler(new ConcurrentClassRunnerScheduler(type));
  }
}
