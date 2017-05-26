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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.reporter.AbstractReporter;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.cognifide.qa.bb.logging.constants.ReportsConfigKeys;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Created by daniel.madejek on 2017-05-26.
 */
public class ExtentReporterFactoryImpl implements ExtentReporterFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ExtentReporterFactoryImpl.class);

  private static final String HTML_REPORTER = "html";

  private static final String SERVER_REPORTER = "server";

  private static final String SEPARATOR = ",";

  @Inject
  @Named(ReportsConfigKeys.BOBCAT_REPORT_EXTENT_PATH)
  private String parentPath;

  @Inject
  @Named(ReportsConfigKeys.BOBCAT_REPORT_EXTENT_NAME)
  private String reportName;

  @Inject
  @Named(ReportsConfigKeys.BOBCAT_REPORT_EXTENT_REPORTERS)
  private String reporters;

  @Inject
  @Named(ReportsConfigKeys.BOBCAT_REPORT_EXTENT_PROJECT_NAME)
  private String projectName;

  @Override
  public List<AbstractReporter> getExtentReporters() {
    List<AbstractReporter> toReturn = new ArrayList<>();
    for (String reporter : reporters.split(SEPARATOR)) {
      AbstractReporter newReporter = null;

      switch (reporter) {
        case HTML_REPORTER:
          newReporter = getExtentHtmlReporter();
          break;
        case SERVER_REPORTER:
          newReporter = getExtentXReporter();
          break;
      }

      if (null != newReporter) {
        toReturn.add(newReporter);
      }
    }
    return toReturn;
  }

  private ExtentHtmlReporter getExtentHtmlReporter() {
    try {
      FileUtils.forceMkdir(new File(parentPath));
      return new ExtentHtmlReporter(parentPath + "/" + reportName);
    } catch (IOException e) {
      LOG.info("Error when creating reporter: ", e);
    }
    return null;
  }

  private ExtentXReporter getExtentXReporter() {
    ExtentXReporter toReturn = new ExtentXReporter("localhost", 27017);
    toReturn.config().setProjectName(projectName);
    return toReturn;
  }
}
