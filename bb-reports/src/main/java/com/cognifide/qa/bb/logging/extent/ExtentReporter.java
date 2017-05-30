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
package com.cognifide.qa.bb.logging.extent;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.cognifide.qa.bb.extent.ExtentReporterFactory;
import com.cognifide.qa.bb.extent.constants.ExtentConfigKeys;
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
import com.cognifide.qa.bb.logging.reporter.AbstractReporter;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Reporter that uses ExtentReports Created by daniel.madejek on 2017-05-25.
 */
public class ExtentReporter extends AbstractReporter {

  private static final Logger LOG = LoggerFactory.getLogger(ExtentReporter.class);

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_INCLUDE_PROPERTIES)
  private String includeProperties;

  @Inject
  private ExtentReporterFactory reporterFactory;

  private List<com.aventstack.extentreports.reporter.AbstractReporter> reporters;

  private ExtentReports extent;

  private TestEntry test;

  private ExtentTest subReport;

  @Override
  public void generateReport() {
    try {
      createReport();
      testEventCollector.accept(this);
      closeReport();
    } catch (IOException e) {
      LOG.error("Exception when creating report", e);
    }

  }

  @Override
  public void suiteStart() {
    // Nothing happens here
  }

  @Override
  public void suiteEnd() {
    // Nothing happens here
  }

  @Override
  public void testStart(TestInfo testInfo) {
    test = new TestEntry(extent.createTest(testInfo.getTestName()), null);
  }

  @Override
  public void testEnd(TestInfo testInfo) {
    // Nothing to do
  }

  @Override
  public void errorEntry(ErrorEntry errorLogEntry) {
    test.getCurrentTest().error(errorLogEntry.getMessage());
  }

  @Override
  public void exceptionEntry(ExceptionEntry exceptionLogEntry) {
    if (exceptionLogEntry.getException() instanceof AssertionError) {
      test.getCurrentTest().fail(exceptionLogEntry.getMessage());
    } else {
      test.getCurrentTest().fail(exceptionLogEntry.getException());
    }

  }

  @Override
  public void screenshotEntry(ScreenshotEntry screenshotLogEntry) {
    try {
      test.getCurrentTest().addScreenCaptureFromPath(screenshotLogEntry.getFilePath(),
          screenshotLogEntry.getFileName());
    } catch (Exception e) {
      LOG.error("Exception when adding screenshot: ", e);
      test.getCurrentTest().fatal(e);
    }
  }

  @Override
  public void infoEntry(InfoEntry infoEntry) {
    test.getCurrentTest().info(infoEntry.getMessage());
  }

  @Override
  public void eventEntry(EventEntry eventLogEntry) {
    test.getCurrentTest().info(eventLogEntry.getEvent() + " " + eventLogEntry.getParameter());
  }

  @Override
  public void subreportStart(SubreportStartEntry subreportStartLogEntry) {
    subReport = test.getCurrentTest().createNode(subreportStartLogEntry.getName());
    test.setParentTest(new TestEntry(test.getCurrentTest(), test.getParentTest()));
    test.setCurrentTest(subReport);
  }

  @Override
  public void subreportEnd(SubreportEndEntry subreportEndLogEntry) {

    test.setCurrentTest(test.getParentTest().getCurrentTest());
    test.setParentTest(test.getParentTest().getParentTest());
  }

  @Override
  public void browserInfoEntry(BrowserInfoEntry browserInfoEntry) {
    Capabilities capabilities = browserInfoEntry.getCapabilities();
    test.getCurrentTest()
        .info(MarkupHelper
            .createLabel("Browser: " + capabilities.getBrowserName()
                + " " + capabilities.getVersion(), ExtentColor.BLUE));
    test.getCurrentTest().info(MarkupHelper
        .createLabel("Platform: " + capabilities.getPlatform(), ExtentColor.AMBER));
  }

  @Override
  public void assertion(AssertionFailedEntry assertionFailedEntry) {
    test.getCurrentTest().fail(assertionFailedEntry.getError().getMessage());
  }

  @Override
  public void softAssertion(SoftAssertionFailedEntry softAssertionFailedEntry) {
    test.getCurrentTest().fail(softAssertionFailedEntry.getMessage());
  }

  @Override
  public void properties(Properties properties) {
    if (Boolean.valueOf(includeProperties)) {
      Iterator<Map.Entry<Object, Object>> entryIterator = properties.entrySet().iterator();
      while (entryIterator.hasNext()) {
        Map.Entry<Object, Object> entry = entryIterator.next();
        extent.setSystemInfo(entry.getKey().toString(), entry.getValue().toString());
      }
    }
  }

  @Override
  public void browserLogEntry(BrowserLogEntry browserLogEntry) {
    test.getCurrentTest().info(browserLogEntry.getMessage());
  }

  private void createReport() throws IOException {
    extent = new ExtentReports();
    createReporters();
  }

  private void createReporters() {
    reporters = reporterFactory.getExtentReporters();
    extent.attachReporter(reporters
        .toArray(new com.aventstack.extentreports.reporter.AbstractReporter[reporters.size()]));
  }

  private void closeReport() {
    extent.flush();
    closeReporters();
  }

  private void closeReporters() {
    for (com.aventstack.extentreports.reporter.AbstractReporter reporter : reporters) {
      reporter.stop();
    }
  }
}
