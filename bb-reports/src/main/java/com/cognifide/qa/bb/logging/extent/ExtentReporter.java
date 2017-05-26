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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.cognifide.qa.bb.logging.TestInfo;
import com.cognifide.qa.bb.logging.constants.ReportsConfigKeys;
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
  @Named(ReportsConfigKeys.BOBCAT_REPORT_EXTENT_INCLUDE_PROPERTIES)
  private String includeProperties;

  @Inject
  private ExtentReporterFactory reporterFactory;

  private List<com.aventstack.extentreports.reporter.AbstractReporter> reporters;

  private ExtentReports extent;

  private ExtentTest test;

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
    test = extent.createTest(testInfo.getTestName());
  }

  @Override
  public void testEnd(TestInfo testInfo) {
    // Nothing to do
  }

  @Override
  public void errorEntry(ErrorEntry errorLogEntry) {
    correctTestEntry().error(errorLogEntry.getMessage());
  }

  @Override
  public void exceptionEntry(ExceptionEntry exceptionLogEntry) {
    if(exceptionLogEntry.getException() instanceof AssertionError){
      correctTestEntry().fail(exceptionLogEntry.getMessage());
    } else {
      correctTestEntry().fail(exceptionLogEntry.getException());
    }

  }

  @Override
  public void screenshotEntry(ScreenshotEntry screenshotLogEntry) {
    try {
      correctTestEntry().addScreenCaptureFromPath(screenshotLogEntry.getFilePath(),
          screenshotLogEntry.getFileName());
    } catch (Exception e) {
      LOG.error("Exception when adding screenshot: ", e);
      correctTestEntry().fatal(e);
    }
  }

  @Override
  public void infoEntry(InfoEntry infoEntry) {
    correctTestEntry().info(infoEntry.getMessage());
  }

  @Override
  public void eventEntry(EventEntry eventLogEntry) {
    correctTestEntry().info(eventLogEntry.getEvent()+" "+eventLogEntry.getParameter());
  }

  @Override
  public void subreportStart(SubreportStartEntry subreportStartLogEntry) {
    subReport = test.createNode(subreportStartLogEntry.getName());
  }

  @Override
  public void subreportEnd(SubreportEndEntry subreportEndLogEntry) {
    subReport = null;
  }

  @Override
  public void browserInfoEntry(BrowserInfoEntry browserInfoEntry) {

  }

  @Override
  public void assertion(AssertionFailedEntry assertionFailedEntry) {
    correctTestEntry().fail(assertionFailedEntry.getError().getMessage());
  }

  @Override
  public void softAssertion(SoftAssertionFailedEntry softAssertionFailedEntry) {
    correctTestEntry().fail(softAssertionFailedEntry.getMessage());
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

  }


  private ExtentTest correctTestEntry() {
    return subReport == null ? test : subReport;
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
