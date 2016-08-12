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
package com.cognifide.bdd.demo.expectedConditions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.publish.pages.MensPage;
import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.expectedconditions.UrlExpectedConditions;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class UrlExpectedConditionsTest {

  private static final String INCORRECT_URL = "incorrect url";

  @Inject
  private MensPage menPage;

  @Inject
  private WebElementUtils webElementUtils;

  @Inject
  @Named(AemConfigKeys.PUBLISH_URL)
  private String publishUrl;

  @Before
  public void goToProductPage() {
    menPage.open();
    assertTrue(menPage.isDisplayed());
  }

  @Test
  public void pageUrlIsConditionPositiveTest() {
    String testedUrl = publishUrl.concat(menPage.getContentPath());
    webElementUtils.isConditionMet(UrlExpectedConditions.pageUrlIs(testedUrl), Timeouts.MINIMAL);
  }

  @Test
  public void pageUrlIsConditionNegativeTest() {
    boolean result = webElementUtils
        .isConditionMet(UrlExpectedConditions.pageUrlIs(publishUrl), Timeouts.MINIMAL);
    assertFalse("Condition is positive", result);
  }

  @Test
  public void pageUrlContainsConditionPositiveTest() {
    webElementUtils.isConditionMet(UrlExpectedConditions.pageUrlContains(publishUrl),
        Timeouts.MINIMAL);
  }

  @Test
  public void pageUrlContainsConditionNegativeTest() {
    boolean result = webElementUtils
        .isConditionMet(UrlExpectedConditions.pageUrlContains(INCORRECT_URL), Timeouts.MINIMAL);
    assertFalse("Condition is positive", result);
  }
}
