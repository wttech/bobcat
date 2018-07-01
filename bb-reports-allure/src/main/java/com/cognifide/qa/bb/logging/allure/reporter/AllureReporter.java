/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.logging.allure.reporter;

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
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import java.util.Properties;
import java.util.UUID;

public class AllureReporter extends AbstractReporter {

  private final ThreadLocal<String> testCases = new InheritableThreadLocal<String>() {
    @Override
    protected String initialValue() {
      return UUID.randomUUID().toString();
    }
  };

  private final AllureLifecycle lifecycle = Allure.getLifecycle();

  @Override
  public void suiteStart() {
    //Nothing now
  }

  @Override
  public void suiteEnd() {
    //Nothing now
  }

  @Override
  public void testStart(TestInfo testInfo) {
    final String uuid = testCases.get();
    final TestResult result = createTestResult(uuid, testInfo);
    lifecycle.scheduleTestCase(result);
    lifecycle.startTestCase(uuid);
  }

  @Override
  public void testEnd(TestInfo testInfo) {

  }

  @Override
  public void errorEntry(ErrorEntry errorLogEntry) {

  }

  @Override
  public void exceptionEntry(ExceptionEntry exceptionLogEntry) {

  }

  @Override
  public void screenshotEntry(ScreenshotEntry screenshotLogEntry) {

  }

  @Override
  public void infoEntry(InfoEntry infoEntry) {

  }

  @Override
  public void eventEntry(EventEntry eventLogEntry) {

  }

  @Override
  public void subreportStart(SubreportStartEntry subreportStartLogEntry) {

  }

  @Override
  public void subreportEnd(SubreportEndEntry subreportEndLogEntry) {

  }

  @Override
  public void browserInfoEntry(BrowserInfoEntry browserInfoEntry) {

  }

  @Override
  public void assertion(AssertionFailedEntry assertionFailedEntry) {

  }

  @Override
  public void softAssertion(SoftAssertionFailedEntry softAssertionFailedEntry) {

  }

  @Override
  public void properties(Properties properties) {

  }

  @Override
  public void browserLogEntry(BrowserLogEntry browserLogEntry) {

  }

  private TestResult createTestResult(String uuid, TestInfo testInfo) {
    return null;
  }
}
