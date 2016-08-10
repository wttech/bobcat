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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.logging.reporter.ReportFileCreator;

/**
 * Log entry with attached screenshot. Screenshot file is created together with the entry itself.
 */
public class ScreenshotEntry extends LogEntry {

  private final File screenshotFile;

  private final String message;

  /**
   * Constructs ScreenshotEntry. Creates a screenshot file.
   *
   * @param webDriver Constructor will use this WebDriver instance to create the screenshot
   * @param fileCreator report file creator
   * @throws IOException Thrown when it's not possible to create the screenshot file.
   */
  public ScreenshotEntry(WebDriver webDriver, ReportFileCreator fileCreator) throws IOException {
    this(webDriver, fileCreator, null);
  }

  /**
   * Constructs ScreenshotEntry. Creates a screenshot file.
   *
   * @param webDriver Constructor will use this WebDriver instance to create the screenshot
   * @param fileCreator report file creator
   * @param message screenshot entry message
   * @throws IOException Thrown when it's not possible to create the screenshot file.
   */
  public ScreenshotEntry(WebDriver webDriver, ReportFileCreator fileCreator, String message)
      throws IOException {
    super();
    this.screenshotFile = fileCreator.getReportFile("png");
    this.message = message;

    final File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    FileUtils.copyFile(scrFile, screenshotFile);
  }

  /**
   * @return Location of the screenshot file
   */
  public String getFilePath() {
    return screenshotFile.getAbsolutePath();
  }

  /**
   * @return Name of the screenshot file
   */
  public String getFileName() {
    return screenshotFile.getName();
  }

  /**
   * @return Caption message attached to the screenshot
   */
  public String getMessage() {
    return message;
  }
}
