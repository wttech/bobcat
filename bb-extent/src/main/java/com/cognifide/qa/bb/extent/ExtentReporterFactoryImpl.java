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
package com.cognifide.qa.bb.extent;

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

import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.cognifide.qa.bb.extent.constants.ExtentConfigKeys;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * Default implementation of {@link ExtentReporterFactory} Created by daniel.madejek on 2017-05-26.
 */
public class ExtentReporterFactoryImpl implements ExtentReporterFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ExtentReporterFactoryImpl.class);

  private static final String HTML_REPORTER = "html";

  private static final String SERVER_REPORTER = "server";

  private static final String SEPARATOR = ",";

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_PATH)
  private String parentPath;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_NAME)
  private String reportFileName;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_REPORTERS)
  private String reporters;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_PROJECT_NAME)
  private String projectName;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_SERVER)
  private String server;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_HOST)
  private String host;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_PORT)
  private Integer port;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_REPORT_NAME)
  private String reportName;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_LOGIN)
  private String login;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_DB_NAME)
  private String dbName;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_PASSWORD)
  private String password;

  @Inject
  @Named(ExtentConfigKeys.BOBCAT_REPORT_EXTENT_USE_AUTHENTICATION)
  private Boolean useAuth;

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
      ExtentHtmlReporter toReturn = new ExtentHtmlReporter(parentPath + "/" + reportFileName);
      toReturn.config().setReportName(reportName);
      return toReturn;
    } catch (IOException e) {
      LOG.info("Error when creating reporter: ", e);
    }
    return null;
  }

  private ExtentXReporter getExtentXReporter() {
    ExtentXReporter toReturn = null;
    if (useAuth) {
      List<MongoCredential> mongoCredentials = new ArrayList<>();
      mongoCredentials.add(MongoCredential.createCredential(login, dbName, password.toCharArray()));
      toReturn = new ExtentXReporter(new ServerAddress(host, port), mongoCredentials);
    } else {
      toReturn = new ExtentXReporter(host, port);
    }
    toReturn.config().setProjectName(projectName);
    toReturn.config().setServerUrl(server);
    toReturn.config().setReportName(reportName);
    return toReturn;
  }
}
