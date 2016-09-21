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
package com.cognifide.qa.bb.cumber.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@Singleton
public class StatisticsHelper {

  public static final String NO_STATISTICS_FILE_FOUND_MESSAGE =
      "There was no statistics file found. Returning default value.";

  private static final Logger LOG = LoggerFactory.getLogger(StatisticsHelper.class);

  /**
   * @param file
   * @return number of all triggered test
   */
  public int getNumberOfTests(File file) {
    return getNumberOfTests(file, 0);
  }

  public int getNumberOfTests(File file, int defaultValue) {
    int returnValue = defaultValue;
    try {
      Scanner scanner = new Scanner(file);
      returnValue = nextInt(scanner);
    } catch (FileNotFoundException e) {
      LOG.warn(NO_STATISTICS_FILE_FOUND_MESSAGE);
    }
    return returnValue;
  }

  /**
   * @param file
   * @return number of failed test
   */
  public int getNumberOfFailedTests(File file) {
    return getNumberOfFailedTests(file, 0);
  }

  public int getNumberOfFailedTests(File file, int defaultValue) {
    int returnValue = defaultValue;
    try {
      Scanner scanner = new Scanner(file);
      if (scanner.hasNext()) {
        scanner.nextLine();
        returnValue = nextInt(scanner);
      }
    } catch (FileNotFoundException e) {
      LOG.warn(NO_STATISTICS_FILE_FOUND_MESSAGE);
    }
    return returnValue;
  }

  /**
   * @param file
   * @return percentage of failed test
   */
  public double getPercentageOfFailedTests(File file) {
    if (getNumberOfTests(file) == 0) {
      return 0.0;
    }
    return ((double)getNumberOfFailedTests(file)/getNumberOfTests(file)) * 100;
  }

  private int nextInt(Scanner scanner) {
    int returnValue = 0;
    if (scanner.hasNext()) {
      returnValue = Integer.valueOf(scanner.nextLine());
    }
    return returnValue;
  }
}
