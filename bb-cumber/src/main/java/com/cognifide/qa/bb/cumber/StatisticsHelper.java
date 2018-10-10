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
package com.cognifide.qa.bb.cumber;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @deprecated since 1.6.0, removing re-run feature
 */
@Deprecated
class StatisticsHelper {

  private static final String NO_STATISTICS_FILE_FOUND_MESSAGE =
      "There was no statistics file found. Returning default value.";

  private static final Logger LOG = LoggerFactory.getLogger(StatisticsHelper.class);

  private static final int DEFAULT_NUMBER_OF_TESTS = 0;

  private int getNumberOfTests(File file) {
    int returnValue = DEFAULT_NUMBER_OF_TESTS;
    try {
      Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name());
      returnValue = nextInt(scanner);
    } catch (FileNotFoundException e) {
      LOG.warn(NO_STATISTICS_FILE_FOUND_MESSAGE, e);
    }
    return returnValue;
  }

  int getNumberOfFailedTests(File file) {
    int returnValue = DEFAULT_NUMBER_OF_TESTS;
    try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name())) {
      if (scanner.hasNext()) {
        scanner.nextLine();
        returnValue = nextInt(scanner);
      }
    } catch (FileNotFoundException e) {
      LOG.warn(NO_STATISTICS_FILE_FOUND_MESSAGE, e);
    }
    return returnValue;
  }

  double getPercentageOfFailedTests(File file) {
    int numberOfTests = getNumberOfTests(file);
    double result;
    if (numberOfTests == 0) {
      result = 0.0;
    } else {
      result = ((double) getNumberOfFailedTests(file) / numberOfTests) * 100;
    }
    return result;
  }

  private int nextInt(Scanner scanner) {
    int returnValue = 0;
    if (scanner.hasNext()) {
      returnValue = Integer.valueOf(scanner.nextLine());
    }
    return returnValue;
  }
}
