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
package com.cognifide.bdd.demo.traffic;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.proxy.analyzer.TrafficAnalyzer;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class TrafficTest extends TrafficAbstractTest {

  @Inject
  private WebDriver webDriver;

  @Inject
  private AemLogin aemLogin;

  @Inject
  private TrafficAnalyzer analyzerUnderTest;

  @Before
  public void loginIntoAuthorInstance() {
    aemLogin.authorLogin();
  }

  @Test
  public void shouldReportThatExpectedCallWasPresent()
      throws ExecutionException, InterruptedException {
    Map<String, String> queryStringEntries = new HashMap<>();
    queryStringEntries.put("path", "/content/geometrixx/en/products/triangle");
    Future<Boolean> analysisResult
        = analyzerUnderTest.analyzeTraffic(AJAX_CALL_PATH, queryStringEntries, TIMEOUT);
    webDriver.get(url + PAGE_TO_OPEN);
    assertThat(analysisResult.get(), is(true));
  }

  @Test
  public void shouldReportThatExpectedCallWasNotPresent()
      throws ExecutionException, InterruptedException {
    Map<String, String> queryStringEntries = new HashMap<>();
    queryStringEntries.put("path", "/content/geometrixx/en/products/square");
    Future<Boolean> analysisResult
        = analyzerUnderTest.analyzeTraffic(AJAX_CALL_PATH, queryStringEntries, TIMEOUT);
    webDriver.get(url + PAGE_TO_OPEN);
    assertThat(analysisResult.get(), is(false));
  }

}
