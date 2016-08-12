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
package com.cognifide.qa.bobcumber.aem.login;

import org.junit.runner.RunWith;

import com.cognifide.qa.bb.cumber.Bobcumber;
import com.cognifide.qa.bb.cumber.StoreFailedResults;

import cucumber.api.CucumberOptions;

@RunWith(Bobcumber.class)
@StoreFailedResults("target/failedTests.properties")
@CucumberOptions(
    features = "src/test/resources/",
    plugin = {"pretty", "html:target/cucumber-html-report/aem-login",
        "json:target/aemLoginTest.json"},
    tags = {"@aemLogin", "~@disabled"},
    glue = "com.cognifide.qa"
)
public class AemLoginTest {
  /*
  This class is empty on  purpose - it's only a runner for cucumber tests.
  All properties for cucumber runner are in @CucumberOptions:

    features - place where feature files are stored,
    format - how the output log should be produced (json for https://github.com/jenkinsci/cucumber-reports-plugin)
    tags - specifies which tests will be run
   */
}
