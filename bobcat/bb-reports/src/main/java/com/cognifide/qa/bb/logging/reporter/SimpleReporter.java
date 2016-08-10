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

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.output.NullOutputStream;
import org.openqa.selenium.Capabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.google.inject.Inject;

/**
 * This is a reporter that reports into a provided PrintStream. You need to subclass SimpleReporter
 * to tell it which stream it should use. There is currently one subclass, {@link StdoutReporter}.
 */
public class SimpleReporter extends AbstractReporter {

  private static final Logger LOG = LoggerFactory.getLogger(SimpleReporter.class);

  protected PrintStream stream;

  @Inject
  private ReportFileCreator fileCreator;

  @Override
  public void suiteStart() {
    stream.println("This is a console test report.");
  }

  @Override
  public void suiteEnd() {
    stream.println("Suite summary:");
    int testCount = getTestCount();
    stream.println("Total number of tests: " + testCount);
    stream.println("Number of failed tests: " + (testCount - getPassedTestCount()));
    stream.println("End of the report.");
    stream.close();
  }

  @Override
  public void testStart(TestInfo testInfo) {
    stream.println(String.format("Test: %s", testInfo.getTestName()));
    stream.println(String.format("Result: %s", testInfo.getResult()));
  }

  @Override
  public void testEnd(TestInfo testInfo) {
    stream.println("EOT");
  }

  @Override
  public void errorEntry(ErrorEntry errorLogEntry) {
    stream.println(
        String.format("[%s] Error: %s", errorLogEntry.getTime(), errorLogEntry.getMessage()));
  }

  @Override
  public void exceptionEntry(ExceptionEntry exceptionLogEntry) {
    stream.println(String.format("[%s] Caught exception: %s %s", exceptionLogEntry.getTime(),
        exceptionLogEntry.getMessage(), exceptionLogEntry.getException()));
  }

  @Override
  public void screenshotEntry(ScreenshotEntry screenshotLogEntry) {
    stream.println(String.format("[%s] Screenshot generated: %s", screenshotLogEntry.getTime(),
        screenshotLogEntry.getFilePath()));
  }

  @Override
  public void infoEntry(InfoEntry infoLogEntry) {
    stream.println(String.format("[%s] %s", infoLogEntry.getTime(), infoLogEntry.getMessage()));
  }

  @Override
  public void eventEntry(EventEntry eventLogEntry) {
    stream.println(
        String.format("[%s] Event: %s, parameters: %s, duration: %d", eventLogEntry.getTime(),
            eventLogEntry.getEvent(), eventLogEntry.getParameter(), eventLogEntry.getDuration()));

  }

  @Override
  public void subreportStart(SubreportStartEntry subreportStartLogEntry) {
    stream.println(
        String.format("[%s] Subreport start %s", subreportStartLogEntry.getTime(),
            subreportStartLogEntry.getName()));
  }

  @Override
  public void subreportEnd(SubreportEndEntry subreportEndLogEntry) {
    stream.println(
        String.format("[%s] Subreport end %s", subreportEndLogEntry.getTime(),
            subreportEndLogEntry.getName()));
  }

  @Override
  public void browserInfoEntry(BrowserInfoEntry browserInfoEntry) {
    Capabilities c = browserInfoEntry.getCapabilities();
    stream.println(
        String.format("[%s] Browser type: %s %s", browserInfoEntry.getTime(),
            c.getBrowserName(), c.getVersion()));
  }

  @Override
  public void assertion(AssertionFailedEntry assertionFailedEntry) {
    stream.println(
        String.format("[%s] Assertion failed: %s", assertionFailedEntry.getTime(),
            assertionFailedEntry.getError()));
  }

  @Override
  public void softAssertion(SoftAssertionFailedEntry softAssertionFailedEntry) {
    stream.println(
        String.format("[%s] Soft assertion failed: %s", softAssertionFailedEntry.getTime(),
            softAssertionFailedEntry.getMessage()));
  }

  @Override
  public void properties(Properties properties) {
    setStream();
    stream.println("Properties:");
    Iterator<Entry<Object, Object>> entryIterator = properties.entrySet().iterator();
    while (entryIterator.hasNext()) {
      Entry<Object, Object> entry = entryIterator.next();
      stream.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
    }
  }

  @Override
  public void browserLogEntry(BrowserLogEntry browserLogEntry) {
    stream.println(
        String.format("[%s] Browser Log: %s", browserLogEntry.getTime(),
            browserLogEntry.getMessage()));
  }

  private void setStream() {
    try {
      stream = new PrintStream(fileCreator.getReportFile("txt", getReportStartingDate()),
          StandardCharsets.UTF_8.name());
    } catch (IOException e) {
      LOG.error("Can't create simple reporter file", e);
      try {
        stream = new PrintStream(new NullOutputStream(), false, StandardCharsets.UTF_8.name());
      } catch (UnsupportedEncodingException e2) {
        LOG.error("UTF-8 is not supported", e2);
      }
    }
  }
}
