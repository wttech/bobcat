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
package com.cognifide.qa.bb.cucumber.reports.configuration;

import java.io.File;

import net.masterthought.cucumber.Configuration;

/**
 * Created by daniel.madejek on 2016-09-01.
 */
public class ConfigurationBuilder {

  private ConfigurationBuilder() {
  }

  public static Configuration build() {
    File reportOutputDirectory = new File("./target");

    String jenkinsBasePath = ".";
    String buildNumber = "1";
    String projectName = "cucumber-jvm";
    boolean skippedFails = true;
    boolean pendingFails = false;
    boolean undefinedFails = true;
    boolean runWithJenkins = false;
    boolean parallelTesting = false;

    Configuration configuration = new Configuration(reportOutputDirectory, projectName);
    configuration.setStatusFlags(skippedFails, pendingFails, undefinedFails);
    configuration.setParallelTesting(parallelTesting);
    configuration.setJenkinsBasePath(jenkinsBasePath);
    configuration.setRunWithJenkins(runWithJenkins);
    configuration.setBuildNumber(buildNumber);

    return configuration;
  }

}
