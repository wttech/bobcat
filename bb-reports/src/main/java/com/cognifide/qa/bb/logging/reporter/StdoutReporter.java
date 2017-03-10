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
package com.cognifide.qa.bb.logging.reporter;

import java.util.List;

import com.cognifide.qa.bb.logging.TestInfo;

/**
 * This is a SimpleReporter implementation. It reports to System.out stream (aka console).
 */
public class StdoutReporter extends SimpleReporter {

  // index of the next test which should be put into STDOUT
  private int testToDisplay;

  /**
   * Constructs StdoutReporter. Sets the stream to System.out.
   */
  public StdoutReporter() {
    this.stream = System.out;
  }

  @Override
  public void generateReport() {
    final List<TestInfo> entries = testEventCollector.getTestInfoEntries();
    while (testToDisplay < entries.size()) {
      entries.get(testToDisplay).accept(this);
      testToDisplay++;
    }
  }
}
