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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.logging.ReportEntryLogger;
import com.cognifide.qa.bb.logging.TestEventCollectorImpl;
import com.cognifide.qa.bb.logging.TestInfo;
import com.cognifide.qa.bb.logging.entries.ErrorEntry;
import com.cognifide.qa.bb.logging.entries.InfoEntry;
import com.cognifide.qa.bb.logging.entries.LogEntry;
import com.cognifide.qa.bb.logging.entries.ScreenshotEntry;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModulesInstaller.class)
public class ReporterTest {

  @Inject
  private TestEventCollectorImpl testEventCollector;

  @Test
  public void shouldCreateLogEntries()
      throws IllegalAccessException, InitializationError, InstantiationException {
    assumeTrue(System.getProperties().getProperty("webdriver.type").equals("chrome"));
    TestRunner testRunner = new TestRunner(SampleTestClass.class);

    RunNotifier notifier = mock(RunNotifier.class);
    testRunner.run(notifier);

    Map<Class<? extends LogEntry>, Set<String>> expectedLogEntries = prepareExpectedLogEntries();

    String testName = "SampleTestClass - sampleTest";

    for (TestInfo testInfo : testEventCollector.getTestInfoEntries()) {
      if (testName.equals(testInfo.getTestName())) {
        for (LogEntry entry : testInfo.getLogEntries()) {
          Class<?> entryClass = entry.getClass();

          Set<String> expectedLogEntryMessages = expectedLogEntries.get(entryClass);
          if (expectedLogEntryMessages == null) {
            continue;
          }

          String entryMessage = null;
          if (entry instanceof InfoEntry) {
            entryMessage = ((InfoEntry) entry).getMessage();
          } else if (entry instanceof ScreenshotEntry) {
            entryMessage = ((ScreenshotEntry) entry).getMessage();
          } else if (entry instanceof ErrorEntry) {
            entryMessage = ((ErrorEntry) entry).getMessage();
          }

          if (entryMessage != null && expectedLogEntryMessages.contains(entryMessage)) {
            expectedLogEntryMessages.remove(entryMessage);
          }

          if (expectedLogEntryMessages.isEmpty()) {
            expectedLogEntries.remove(entryClass);
          }
        }
        break;
      }
    }

    if (!expectedLogEntries.isEmpty()) {
      fail(buildFailMessage(expectedLogEntries));
    }
  }

  private Map<Class<? extends LogEntry>, Set<String>> prepareExpectedLogEntries() {
    Map<Class<? extends LogEntry>, Set<String>> expectedLogEntries = new HashMap<>();
    expectedLogEntries.put(InfoEntry.class,
        new HashSet<>(Arrays
            .asList("test info message", "expected exception: '/ by zero'")));
    expectedLogEntries.put(ScreenshotEntry.class, new HashSet<>(Arrays.asList("test screenshot")));
    expectedLogEntries.put(ErrorEntry.class,
        new HashSet<>(
            Arrays.asList("test error message", "test error message with exception")));

    return expectedLogEntries;
  }

  private String buildFailMessage(Map<Class<? extends LogEntry>, Set<String>> expectedLogEntries) {
    StringBuilder failMessage = new StringBuilder("Expected entries not found: ");
    String prefix = "";
    for (Map.Entry<Class<? extends LogEntry>, Set<String>> entry : expectedLogEntries
        .entrySet()) {
      for (String entryMessage : entry.getValue()) {
        failMessage
            .append(prefix)
            .append(entry.getKey().getSimpleName())
            .append("(\"")
            .append(entryMessage)
            .append("\")");
        prefix = ", ";
      }
    }

    return failMessage.toString();
  }

  @RunWith(TestRunner.class)
  @Modules(GuiceModulesInstaller.class)
  public static final class SampleTestClass {

    @Inject
    private ReportEntryLogger reportEntryLogger;

    @Test
    public void sampleTest() {
      assumeTrue(System.getProperties().getProperty("webdriver.type").equals("chrome"));
      reportEntryLogger.info("test info message");
      reportEntryLogger.screenshot("test screenshot");
      reportEntryLogger.error("test error message");
      reportEntryLogger.error("test error message with exception", new RuntimeException("Test"));
      int i = 0;
      try {
        i = 1 / 0;
      } catch (ArithmeticException ae) {
        reportEntryLogger.info("expected exception: '" + ae.getMessage() + "'", ae);
      }
      assertEquals("should have initial value", 0, i);

    }
  }
}
