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
package com.cognifide.qa.bb.junit;

/**
 * Interface for test events that needs to be collected for further use (i.e. reporting)
 */
public interface TestEventCollector {

  /**
   * Marks the start of the test. All subsequent entries will be associated with this test (called "current test"),
   * until it's closed with "end" method.
   *
   * @param testName Meaningful name of the test.
   */
  void start(String testName);

  /**
   * Stores an exception entry in the current test.
   *
   * @param t The exception to be logged.
   */
  void exception(Throwable t);

  /**
   * Marks the current test as finished.
   */
  void end();

  /**
   * Stores the info entry with the current test.
   *
   * @param message info message
   */
  void info(String message);

  /**
   * Remove information about test which is now collected
   */
  void removeLastEntry();
}
