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
import java.io.IOException;
import java.util.Scanner;

public class StatisticsUtils {
  private StatisticsUtils() {
    // util class
  }

  /**
   * @param file
   * @return number of all triggered test
   */
  public static Integer getNumberOfTests(File file) throws IOException {
    Scanner scanner = new Scanner(file);
    if (!scanner.hasNext()) {
      return 0;
    }
    return new Integer(scanner.nextLine());
  }

  /**
   * @param file
   * @return number of failed test
   */
  public static Integer getNumberOfFailedTests(File file) throws IOException{
    Scanner scanner = new Scanner(file);
    scanner.nextLine();
    if (!scanner.hasNext()) {
      return 0;
    }
    return new Integer(scanner.nextLine());
  }

  /**
   * @param file
   * @return percentage of failed test
   */
  public static Double getPercentageOfFailedTests(File file) throws IOException {
    if (getNumberOfTests(file).equals(0)) {
      return 0.0;
    }
    return (getNumberOfFailedTests(file).doubleValue()/getNumberOfTests(file)) * 100;
  }
}
