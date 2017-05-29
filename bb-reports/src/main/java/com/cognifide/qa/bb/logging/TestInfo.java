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
package com.cognifide.qa.bb.logging;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.cognifide.qa.bb.provider.selenium.webdriver.close.ClosingAwareWebDriverWrapper;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.logging.entries.AssertionFailedEntry;
import com.cognifide.qa.bb.logging.entries.BrowserInfoEntry;
import com.cognifide.qa.bb.logging.entries.BrowserLogEntry;
import com.cognifide.qa.bb.logging.entries.ErrorEntry;
import com.cognifide.qa.bb.logging.entries.EventEntry;
import com.cognifide.qa.bb.logging.entries.ExceptionEntry;
import com.cognifide.qa.bb.logging.entries.InfoEntry;
import com.cognifide.qa.bb.logging.entries.LogEntry;
import com.cognifide.qa.bb.logging.entries.LogEntryComparator;
import com.cognifide.qa.bb.logging.entries.ScreenshotEntry;
import com.cognifide.qa.bb.logging.entries.SoftAssertionFailedEntry;
import com.cognifide.qa.bb.logging.entries.SubreportEndEntry;
import com.cognifide.qa.bb.logging.entries.SubreportStartEntry;
import com.cognifide.qa.bb.logging.reporter.AbstractReporter;
import com.cognifide.qa.bb.logging.reporter.ReportFileCreator;
import com.google.inject.Inject;

/**
 * Collects all information about single test, from test name to start date to all log entries.
 * Bobcat users won't access this class directly.
 */
public class TestInfo {

  /**
   * Comparator of test entries, for sorting TestInfo by date.
   */
  public static final Comparator<TestInfo> BY_START_DATE_COMPARATOR =
      (o1, o2) -> o1.getStart().compareTo(o2.getStart());

  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TestInfo.class);

  private static final String NATIVE_APP_CONTEXT = "NATIVE_APP";

  private final Date start;

  private final Deque<String> subreports;

  private final SortedSet<LogEntry> logEntries;

  @Inject
  private ReportFileCreator fileCreator;

  @Inject
  private WebDriver webDriver;

  @Inject
  private BrowserLogEntryCollector browserLogEntryCollector;

  private Date endDate;

  private String testName;

  private TestResult testResult;

  private boolean last;

  private BrowserInfoEntry browserInfoEntry;

  /**
   * Constructs TestInfo. Initializes its fields to default values. Don't call it manually, use
   * Guice instead. Test is successful by default.
   */
  public TestInfo() {
    this.testName = "Default test name";
    this.logEntries = new TreeSet<>(new LogEntryComparator());
    this.testResult = new Success();
    this.start = new Date();
    this.last = false;
    this.subreports = new LinkedList<>();
  }

  private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
    long diffInMillies = date2.getTime() - date1.getTime();
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
  }

  /**
   * @return Name of the test.
   */
  public String getTestName() {
    return testName;
  }

  /**
   * @return Start date of the test.
   */
  public Date getStart() {
    return (Date) start.clone();
  }

  /**
   * @return End date of the test.
   */
  public Date getEnd() {
    if (endDate == null) {
      return null;
    }
    return (Date) endDate.clone();
  }

  /**
   * @return List of all log entries associated with this test.
   */
  public SortedSet<LogEntry> getLogEntries() {
    return logEntries;
  }

  /**
   * @return Browser info entry.
   */
  public BrowserInfoEntry getBrowser() {
    return browserInfoEntry;
  }

  /**
   * Adds an exception entry and marks the test as failed.
   *
   * @param t throwable
   */
  public void exception(Throwable t) {
    fail();
    addLogEntry(new ExceptionEntry(t));
    addLogEntry(browserLogEntryCollector.getBrowserLogEntries());
  }

  /**
   * Adds a log entry.
   *
   * @param logEntry log entry
   */
  void addLogEntry(LogEntry logEntry) {
    logEntries.add(logEntry);
  }

  /**
   * Adds collection of log entries
   *
   * @param browserLogEntries collection of log entries
   */
  void addLogEntry(Collection<LogEntry> browserLogEntries) {
    logEntries.addAll(browserLogEntries);
  }

  /**
   * Ends all subreports and saves the end date.
   */
  public void end() {
    while (!subreports.isEmpty()) {
      String subreport = subreports.pop();
      addLogEntry(new SubreportEndEntry(subreport));
    }
    this.endDate = new Date();
  }

  /**
   * Stores the info entry.
   *
   * @param message info message
   */
  public void info(String message) {
    addLogEntry(new InfoEntry(message));
  }

  /**
   * Makes a screenshot and marks the test as failed.
   */
  public void fail() {
    screenshot();
    this.testResult = new Fail();
  }

  /**
   * Makes a screenshot and marks the test as failed. Stores the failure message.
   *
   * @param message fail message
   */
  public void fail(String message) {
    screenshot();
    this.testResult = new Fail(message);
  }

  /**
   * Marks the test as failed and adds an error entry with the failure message.
   *
   * @param message error message
   */
  public void error(String message) {
    fail();
    addLogEntry(new ErrorEntry(message));
    addLogEntry(browserLogEntryCollector.getBrowserLogEntries());
  }

  /**
   * Retrieves capabilities info from webDriver and stores the BrowserInfoEntry.
   */
  public void addBrowserInfo() {
    Capabilities caps = ((HasCapabilities) webDriver).getCapabilities();
    browserInfoEntry = new BrowserInfoEntry(caps);
  }

  /**
   * @return Test result in the form of TestResult.
   */
  public TestResult getResult() {
    return testResult;
  }

  /**
   * Creates a screenshot with empty message.
   */
  public void screenshot() {
    screenshot(null);
  }

  /**
   * Creates a screenshot with a caption message.
   *
   * @param message screenshot message
   */
  public void screenshot(String message) {
    try {
      if (webDriver instanceof ClosingAwareWebDriverWrapper
          && ((ClosingAwareWebDriverWrapper) this.webDriver).getWrappedDriver() instanceof AppiumDriver) {
        AppiumDriver appiumDriver =
            (AppiumDriver) ((ClosingAwareWebDriverWrapper) this.webDriver).getWrappedDriver();
        String originalContext = appiumDriver.getContext();
        appiumDriver.context(NATIVE_APP_CONTEXT);
        ScreenshotEntry screenshotEntry = new ScreenshotEntry(this.webDriver, fileCreator, message);
        appiumDriver.context(originalContext);
        addLogEntry(screenshotEntry);
      } else {
        addLogEntry(new ScreenshotEntry(webDriver, fileCreator, message));
      }
    } catch (IOException e) {
      LOG.error("Can't take screenshot", e);
    }
  }

  /**
   * Implementation of Visitor pattern. Takes the Report for a guided tour through all information
   * stored in the TestInfo instance, from start of the test to all entries to test end. It is up to
   * a particular implementation of the Reporter to react to different types of test info and log
   * entries. <br>
   * Also, detects the last log entry on the fly.
   */
  public void accept(AbstractReporter r) {
    r.testStart(this);
    r.browserInfoEntry(browserInfoEntry);

    Iterator<LogEntry> entryIterator = logEntries.iterator();
    while (entryIterator.hasNext()) {
      LogEntry entry = entryIterator.next();
      if (!entryIterator.hasNext()) {
        entry.setLast(true);
      }
      visit(entry, r);
    }

    r.testEnd(this);

  }

  private void visit(LogEntry logEntry, AbstractReporter r) {
    if (logEntry instanceof AssertionFailedEntry) {
      r.assertion((AssertionFailedEntry) logEntry);
    } else if (logEntry instanceof BrowserInfoEntry) {
      r.browserInfoEntry((BrowserInfoEntry) logEntry);
    } else if (logEntry instanceof ErrorEntry) {
      r.errorEntry((ErrorEntry) logEntry);
    } else if (logEntry instanceof EventEntry) {
      r.eventEntry((EventEntry) logEntry);
    } else if (logEntry instanceof ExceptionEntry) {
      r.exceptionEntry((ExceptionEntry) logEntry);
    } else if (logEntry instanceof InfoEntry) {
      r.infoEntry((InfoEntry) logEntry);
    } else if (logEntry instanceof BrowserLogEntry) {
      r.browserLogEntry((BrowserLogEntry) logEntry);
    } else if (logEntry instanceof ScreenshotEntry) {
      r.screenshotEntry((ScreenshotEntry) logEntry);
    } else if (logEntry instanceof SoftAssertionFailedEntry) {
      r.softAssertion((SoftAssertionFailedEntry) logEntry);
    } else if (logEntry instanceof SubreportEndEntry) {
      r.subreportEnd((SubreportEndEntry) logEntry);
    } else if (logEntry instanceof SubreportStartEntry) {
      r.subreportStart((SubreportStartEntry) logEntry);
    }
  }

  /**
   * Adds a web event entry.
   *
   * @param event event name
   * @param parameter event parameter
   * @param duration event duration
   */
  public void event(String event, String parameter, long duration) {
    logEntries.add(new EventEntry(event, parameter, duration));
  }

  /**
   * Sets the name of the test.
   *
   * @param testName test name
   */
  public void setName(String testName) {
    this.testName = testName;
  }

  /**
   * @return True if the test is last in the suite. False otherwise.
   */
  public boolean isLast() {
    return last;
  }

  /**
   * Sets the position of the test within suite.
   *
   * @param isLast True means last test in suite.
   */
  public void setLast(boolean isLast) {
    this.last = isLast;
  }

  /**
   * Starts a new subreport. Stores the name of the subreport on the stack. Creates a timestamped
   * subreport start log entry.
   *
   * @param subreport Subreport name
   */
  public void startSubreport(String subreport) {
    subreports.push(subreport);
    addLogEntry(new SubreportStartEntry(subreport));
  }

  /**
   * Closes the subreport. Checks if the provided subreport name matches subreport name stored on
   * the stack. If the names match, endSubreport will remove the subreport from stack and close it.
   * Otherwise, endSubreport will log an error in the report.
   *
   * @param subreport Name of the subreport.
   */
  public void endSubreport(String subreport) {
    if (subreport.isEmpty()) {
      LOG.error(String.format("Tried to end non-existing report \"%s\"", subreport));
      return;
    }
    String topSubreport = subreports.peek();
    if (topSubreport.equals(subreport)) {
      subreports.pop();
      addLogEntry(new SubreportEndEntry(subreport));
      return;
    }
    LOG.error(String.format("Tried to end non-existing report \"%s\"", subreport));
  }

  /**
   * @return Duration of the test, calculated as the difference between end and start timestamps.
   */
  public long getDuration() {
    return getDateDiff(start, endDate, TimeUnit.SECONDS);
  }

  /**
   * Marks the test as failed and logs a failed hard assertion.
   *
   * @param e assertion error
   */
  public void assertion(AssertionError e) {
    fail();
    logEntries.add(new AssertionFailedEntry(e));
  }

  /**
   * Marks the test as failed and logs a failed soft assertion.
   *
   * @param message soft assertion message
   */
  public void softAssertion(String message) {
    fail();
    logEntries.add(new SoftAssertionFailedEntry(message));
  }

}
