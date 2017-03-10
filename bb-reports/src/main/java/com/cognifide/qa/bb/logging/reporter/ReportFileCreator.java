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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.cognifide.qa.bb.logging.constants.ReportsConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Creates a report file.
 */
public class ReportFileCreator {

  private static final String PATH_PATTERN = "test-report-%s-%04d.%s";

  private static final String DATE_PATTERN = "yyyyMMdd-HHmmss.SSS";

  private static final AtomicInteger COUNTER = new AtomicInteger();

  @Inject
  @Named(ReportsConfigKeys.BOBCAT_REPORT_PATH)
  private String parentPath;

  /**
   * Creates a report file with the provided extension and current time.
   *
   * @param ext file extension
   * @return The newly created file
   * @throws IOException Thrown if file creation failed
   */
  public File getReportFile(String ext) throws IOException {
    return getReportFile(ext, new Date());
  }

  /**
   * Creates a report file with the provided extension and time.
   *
   * @param ext  file extension
   * @param date date for filename
   * @return The newly created file
   * @throws IOException Thrown if file creation failed
   */
  public File getReportFile(String ext, Date date) throws IOException {
    final File parent = new File(parentPath);
    if (!parent.isDirectory() && !parent.mkdirs()) {
      throw new IOException("Can't create parent directory " + parentPath);
    }

    final String formattedDate = new SimpleDateFormat(DATE_PATTERN).format(date);
    int reportIndex = COUNTER.getAndIncrement();
    final String reportName = String.format(PATH_PATTERN, formattedDate, reportIndex, ext);
    final File file = new File(parent, reportName);
    if (file.exists() && !file.delete()) {
      throw new IOException("Can't replace existing file " + reportName);
    }
    if (!file.createNewFile()) {
      throw new IOException("Can't create report file " + reportName + " under " + parentPath);
    }
    return file;
  }
}
