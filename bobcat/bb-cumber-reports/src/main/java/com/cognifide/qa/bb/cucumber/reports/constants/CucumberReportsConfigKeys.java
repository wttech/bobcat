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
package com.cognifide.qa.bb.cucumber.reports.constants;

public final class CucumberReportsConfigKeys {

  public static final String BOBCAT_REPORT_OUTPUT_DIRECTORY = "bobcat.report.output.directory";
  public static final String BOBCAT_REPORT_JSON_DIRECTORY = "bobcat.report.json.directory";
  public static final String BOBCAT_REPORT_PROJECT_NAME = "bobcat.report.project.name";
  public static final String BOBCAT_REPORT_SKIPPED_FAILS = "bobcat.report.skipped.fails";
  public static final String BOBCAT_REPORT_PENDING_FAILS = "bobcat.report.pending.fails";
  public static final String BOBCAT_REPORT_UNDEFINED_FAILS = "bobcat.report.undefined.fails";
  public static final String BOBCAT_REPORT_RUN_WITH_JENKINS = "bobcat.report.run.with.jenkins";
  public static final String BOBCAT_REPORT_PARALLEL_TESTING = "bobcat.report.parallel.testing";
  public static final String BOBCAT_JENKINS_BUILD = "bobcat.jenkins.build";

  private CucumberReportsConfigKeys() {}

}
