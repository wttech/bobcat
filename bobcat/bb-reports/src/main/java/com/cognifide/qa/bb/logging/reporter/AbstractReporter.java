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

import java.util.Date;
import java.util.Properties;

import com.cognifide.qa.bb.logging.Success;
import com.cognifide.qa.bb.logging.TestEventCollectorImpl;
import com.cognifide.qa.bb.logging.TestInfo;
import com.cognifide.qa.bb.logging.entries.AssertionFailedEntry;
import com.cognifide.qa.bb.logging.entries.BrowserInfoEntry;
import com.cognifide.qa.bb.logging.entries.BrowserLogEntry;
import com.cognifide.qa.bb.logging.entries.ErrorEntry;
import com.cognifide.qa.bb.logging.entries.EventEntry;
import com.cognifide.qa.bb.logging.entries.ExceptionEntry;
import com.cognifide.qa.bb.logging.entries.InfoEntry;
import com.cognifide.qa.bb.logging.entries.ScreenshotEntry;
import com.cognifide.qa.bb.logging.entries.SoftAssertionFailedEntry;
import com.cognifide.qa.bb.logging.entries.SubreportEndEntry;
import com.cognifide.qa.bb.logging.entries.SubreportStartEntry;
import com.cognifide.qa.bb.reporter.Reporter;
import com.google.inject.Inject;

/**
 * Base class for all reporters. Part of Visitor pattern implementation. Each TestReportElement
 * should have a corresponding method in this class. <br>
 * The method that triggers generation of the report file is {@link #generateReport()}.
 */
public abstract class AbstractReporter implements Reporter {

  @Inject
  protected TestEventCollectorImpl testEventCollector;

  /**
   * Triggers generation of the report.
   */
  @Override
  public void generateReport() {
    testEventCollector.accept(this);
  }

  /**
   * @return Number of tests
   */
  public int getTestCount() {
    return testEventCollector.getTestInfoEntries().size();
  }

  /**
   * @return Number of passed tests
   */
  public int getPassedTestCount() {
    int passedCount = 0;
    for (TestInfo entry : testEventCollector.getTestInfoEntries()) {
      if (entry.getResult() instanceof Success) {
        passedCount++;
      }
    }
    return passedCount;
  }

  /**
   * @return Percentage of passed tests, rounded to nearest integer value
   */
  public double getPassedTestPercent() {
    return (int) (100.0 * getPassedTestCount() / getTestCount());
  }

  /**
   * @return Percentage of failed tests, rounded to nearest integer value
   */
  public double getFailedPercent() {
    return 100 - getPassedTestPercent();
  }

  /**
   * @return Number of failed tests
   */
  public int getFailedCount() {
    return getTestCount() - getPassedTestCount();
  }

  /**
   * @return Time when the suite was started
   */
  public Date getReportStartingDate() {
    return testEventCollector.getStartingDate();
  }

  // --- Visitor callbacks ---

  /**
   * Reports start of the suite
   */
  public abstract void suiteStart();

  /**
   * Reports end of the suite
   */
  public abstract void suiteEnd();

  /**
   * Reports start of the test
   * @param testInfo test start message
   */
  public abstract void testStart(TestInfo testInfo);

  /**
   * Reports end of the test
   * @param testInfo test end message
   */
  public abstract void testEnd(TestInfo testInfo);

  /**
   * Reports error entry
   * @param errorLogEntry error log entry
   */
  public abstract void errorEntry(ErrorEntry errorLogEntry);

  /**
   * Reports exception
   * @param exceptionLogEntry exception log entry
   */
  public abstract void exceptionEntry(ExceptionEntry exceptionLogEntry);

  /**
   * Reports screenshot
   * @param screenshotLogEntry screenshot log entry
   */
  public abstract void screenshotEntry(ScreenshotEntry screenshotLogEntry);

  /**
   * Reports information entry
   * @param infoEntry info entry
   */
  public abstract void infoEntry(InfoEntry infoEntry);

  /**
   * Reports WebDriver event
   * @param eventLogEntry event log entry
   */
  public abstract void eventEntry(EventEntry eventLogEntry);

  /**
   * Reports start of the subreport
   * @param subreportStartLogEntry subreport log entry
   */
  public abstract void subreportStart(SubreportStartEntry subreportStartLogEntry);

  /**
   * Reports end of the subreport
   * @param subreportEndLogEntry subreport log entry
   */
  public abstract void subreportEnd(SubreportEndEntry subreportEndLogEntry);

  /**
   * Reports browser info
   * @param browserInfoEntry browser info entry
   */
  public abstract void browserInfoEntry(BrowserInfoEntry browserInfoEntry);

  /**
   * Reports failed hard assertion
   * @param assertionFailedEntry assertion failed entry
   */
  public abstract void assertion(AssertionFailedEntry assertionFailedEntry);

  /**
   * Reports failed soft assertion
   * @param softAssertionFailedEntry soft assertion failed entry
   */
  public abstract void softAssertion(SoftAssertionFailedEntry softAssertionFailedEntry);

  /**
   * Reports properties
   * @param properties properties
   */
  public abstract void properties(Properties properties);

  /**
   * Report browser log entry
   * @param browserLogEntry browser log entry
   */
  public abstract void browserLogEntry(BrowserLogEntry browserLogEntry);

}
