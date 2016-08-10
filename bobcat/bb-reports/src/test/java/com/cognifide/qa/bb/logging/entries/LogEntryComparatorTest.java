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
package com.cognifide.qa.bb.logging.entries;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Theories.class)
public class LogEntryComparatorTest {

  private static final Logger LOG = LoggerFactory.getLogger(LogEntryComparatorTest.class);

  private LogEntryComparator tested;

  private static LogEntry[] logEntries;

  @BeforeClass
  public static void createLogEntries() {
    logEntries = new LogEntry[] {
        new ExceptionEntry(new RuntimeException("A")),
        new EventEntry("event", "B", 0L),
        new SoftAssertionFailedEntry("C"),
        new SubreportEndEntry("D"),
        new InfoEntry("E"),
        new AssertionFailedEntry(new AssertionError("F")),
        new SubreportStartEntry("G"),
        new ErrorEntry("H"),

        //new ScreenshotEntry()
        new BrowserInfoEntry(new DesiredCapabilities()),

        new ErrorEntry("K"),
        new SubreportStartEntry("L"),
        new AssertionFailedEntry(new AssertionError("M")),
        new InfoEntry("N"),
        new SubreportEndEntry("O"),
        new SoftAssertionFailedEntry("P"),
        new EventEntry("event", "Q", 0L),
        new ExceptionEntry(new RuntimeException("R"))
    };
  }

  @Before
  public void createLogEntriesComparator() {
    tested = new LogEntryComparator();
  }

  @DataPoints
  public static Object[][] getAllPossiblePairsOfArrayIndexes() {
    int len = logEntries.length;
    Object[][] result = new Object[len * len][];
    for (int i = 0; i < len; i++) {
      for (int j = 0; j < len; j++) {
        result[i * len + j] = new Integer[] {i, j};
      }
    }
    return result;
  }

  /**
   * The assumption here is that log entries in array were created in ascending order.
   *
   * @param dataPoint
   */
  @Theory
  public void logEntriesHaveSameOrderAsIndexes(Object[] dataPoint) {
    int index1 = (int) dataPoint[0];
    int index2 = (int) dataPoint[1];
    LogEntry first = logEntries[index1];
    LogEntry second = logEntries[index2];

    LOG.debug("testing comparator for log entries: {} and {}", index1, index2);
    int result = tested.compare(first, second);
    int expected = Integer.compare(index1, index2);

    assertThatHaveSameSignum(expected, result);
  }

  @Test
  public void nullObjectAsFirst() {
    int actual = tested.compare(null, logEntries[5]);
    assertThatHaveSameSignum(-1, actual);
  }

  @Test
  public void nullObjectAsSecond() {
    int actual = tested.compare(logEntries[5], null);
    assertThatHaveSameSignum(1, actual);
  }

  @Test
  public void twoNullObjects() {
    int actual = tested.compare(null, null);
    assertThatHaveSameSignum(0, actual);
  }

  private void assertThatHaveSameSignum(int expected, int result) {
    if (expected < 0) {
      assertTrue(result < 0);
    }
    if (expected > 0) {
      assertTrue(result > 0);
    }
    if (expected == 0) {
      assertTrue(result == 0);
    }
  }

}
