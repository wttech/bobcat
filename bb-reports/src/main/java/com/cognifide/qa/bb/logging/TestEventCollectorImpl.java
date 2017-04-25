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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.junit.TestEventCollector;
import com.cognifide.qa.bb.logging.reporter.AbstractReporter;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Central class in Bobcat's reporting API. Collects all test events so that it's possible to
 * generate reports. Don't its methods manually. Bobcat provides you with several specialized
 * classes that you should use instead.
 */
@Singleton
public class TestEventCollectorImpl implements TestEventCollector {

  private final List<TestInfo> testInfoEntries;

  private final Date startingDate;

  @Inject
  private Injector injector;

  @Inject
  private Provider<WebDriver> webDriverProvider;

  @Inject
  private Properties properties;

  private final ThreadLocal<TestInfo> currentTest = new ThreadLocal<TestInfo>() {
    @Override
    protected TestInfo initialValue() {
      return injector.getInstance(TestInfo.class);
    }
  };

  /**
   * Constructs TestEventCollector. Don't call it manually. Let Guice construct the instance and
   * inject it instead.
   */
  public TestEventCollectorImpl() {
    this.testInfoEntries = Collections.synchronizedList(new ArrayList<TestInfo>());
    this.startingDate = new Date();
  }

  /**
   * Marks the start of the test. All subsequent entries will be associated with this test (called
   * "current test"), until it's closed with "end" method.
   *
   * @param testName Meaningful name of the test.
   */
  @Override
  public void start(String testName) {
    currentTest.get().setLast(false);
    currentTest.set(injector.getInstance(TestInfo.class));
    currentTest.get().setLast(true);
    currentTest.get().setName(testName);
    browserInfo();
    testInfoEntries.add(currentTest.get());
  }

  /**
   * Stores an exception entry in the current test.
   *
   * @param t The exception to be logged.
   */
  @Override
  public void exception(Throwable t) {
    currentTest.get().exception(t);
  }

  /**
   * Marks the current test as finished.
   */
  @Override
  public void end() {
    currentTest.get().end();
  }

  /**
   * Marks current test as failed.
   */
  public void fail() {
    currentTest.get().fail();
  }

  /**
   * Marks current test as failed and stores the provided failure message.
   *
   * @param errorMessage error message
   */
  public void fail(String errorMessage) {
    currentTest.get().fail(errorMessage);
  }

  /**
   * Stores the info entry with the current test.
   *
   * @param message info message
   */
  @Override
  public void info(String message) {
    currentTest.get().info(message);
  }

  /**
   * Stores the info entry with the current test.
   *
   * @param message info message
   * @param args arguments of message
   */
  public void info(String message, Object... args) {
    currentTest.get().info(String.format(message, args));
  }

  /**
   * @return All tests that have been registered until now, sorted by startDate.
   */
  public List<TestInfo> getTestInfoEntries() {
    Set<TestInfo> result = new TreeSet<>(TestInfo.BY_START_DATE_COMPARATOR);
    synchronized (testInfoEntries) {
      for (TestInfo test : testInfoEntries) {
        if (test.getEnd() != null) {
          result.add(test);
        }
      }
    }
    return new ArrayList<>(result);
  }

  /**
   * @return Properties instance that contains all properties from property files.
   */
  public Set<Entry<Object, Object>> getProperties() {
    return properties.entrySet();
  }

  /**
   * Stores the error entry in the current test, together with error message.
   *
   * @param message error message
   */
  public void error(String message) {
    currentTest.get().error(message);
  }

  /**
   * Stores the error entry in the current test.
   *
   * @param message error message
   * @param args arguments of message
   */
  public void error(String message, Object... args) {
    currentTest.get().error(String.format(message, args));
  }

  /**
   * Creates the screenshot image and stores the screenshot entry in the current test.
   */
  public void screenshot() {
    currentTest.get().screenshot();
  }

  public void accept(AbstractReporter r) {
    r.properties(properties);
    r.suiteStart();

    for (TestInfo testInfo : getTestInfoEntries()) {
      testInfo.accept(r);
    }

    r.suiteEnd();
  }

  /**
   * Creates the screenshot image and stores the screenshot entry in the current test, together with
   * provided message.
   */
  public void screenshot(String message) {
    currentTest.get().screenshot(message);
  }

  /**
   * Stores the web event entry in the current test.
   *
   * @param action event action
   * @param param event parameter
   * @param duration event duration
   */
  public void event(String action, String param, long duration) {
    currentTest.get().event(action, param, duration);
  }

  /**
   * Retrieves browser info from webDriver instance and stores it in the current test. If the
   * webDriver lacks capabilities, browserInfo will create an error entry.
   */
  public final void browserInfo() {
    final WebDriver webDriver = webDriverProvider.get();
    if (webDriver instanceof HasCapabilities) {
      currentTest.get().addBrowserInfo();
    } else {
      currentTest.get()
          .error("Your WebDriver doesn't provide capabilities, so we can't log its info.");
    }
  }

  /**
   * Marks a beginning of a subreport in the current test.
   *
   * @param subreport Subreport name
   */
  public void startSubreport(String subreport) {
    currentTest.get().startSubreport(subreport);
  }

  /**
   * Marks end of subreport in the current test.
   *
   * @param subreport subreport name
   */
  public void endSubreport(String subreport) {
    currentTest.get().endSubreport(subreport);
  }

  /**
   * Logs the hard assertion failure in the current test.
   *
   * @param e assertion error
   */
  public void assertion(AssertionError e) {
    currentTest.get().assertion(e);
  }

  /**
   * Logs the soft assertion failure in the current test.
   *
   * @param message soft assertion message
   */
  public void softAssertion(String message) {
    currentTest.get().softAssertion(message);
  }

  /**
   * @return Start date of the suite.
   */
  public Date getStartingDate() {
    return (Date) startingDate.clone();
  }

  @Override
  public void removeLastEntry() {
    testInfoEntries.remove(currentTest.get());
  }

  TestInfo getCurrentTest() {
    return currentTest.get();
  }
}
