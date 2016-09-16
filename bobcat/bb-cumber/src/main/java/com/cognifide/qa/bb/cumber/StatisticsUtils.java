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
