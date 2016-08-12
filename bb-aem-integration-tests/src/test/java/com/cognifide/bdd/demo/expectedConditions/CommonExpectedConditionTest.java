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
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.bdd.demo.po.publish.components.TopNav;
import com.cognifide.bdd.demo.po.publish.pages.HomePage;
import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.expectedconditions.CommonExpectedConditions;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class CommonExpectedConditionTest {

  private static final Logger LOG = LoggerFactory.getLogger(CommonExpectedConditionTest.class);

  private static final String MENS_LINK = "/content/geometrixx-outdoors/en/men.html";

  @Inject
  @Named(AemConfigKeys.PUBLISH_URL)
  private String publishUrl;

  @Inject
  private WebElementUtils webElementUtils;

  @Inject
  private HomePage homePage;

  @Inject
  private WebDriver webDriver;

  private WebElement topNavElement;

  private WebElement firstChildDiv;

  private WebElement firstLinkInList;

  @Before
  public void openTestPageAndGetElements() {
    homePage.open();
    try {
      topNavElement = webDriver.findElement(By.cssSelector(TopNav.CSS_SELECTEOR));
      firstChildDiv = topNavElement.findElement(By.tagName(HtmlTags.DIV));
      firstLinkInList = topNavElement.findElement(By.cssSelector("nav ul li a"));
    } catch (NoSuchElementException nsee) {
      LOG.error("error while getting elements at {}", homePage.getContentPath());
      throw nsee;
    }
  }

  @Test
  public void elementHasAttribute_shouldMatchClassValue() {
    ExpectedCondition<Boolean> condition =
        CommonExpectedConditions
            .elementHasAttributeWithValue(firstChildDiv, HtmlTags.Attributes.CLASS, "nav-wrapper ");
    Boolean result = webElementUtils.isConditionMet(condition, Timeouts.MINIMAL);
    assertTrue("class attribute should be recognized properly", result);
  }

  @Test
  public void elementHasAttribute_shouldNotMatchClassValueTrimmed() {
    ExpectedCondition<Boolean> condition =
        CommonExpectedConditions
            .elementHasAttributeWithValue(firstChildDiv, HtmlTags.Attributes.CLASS, "nav-wrapper");
    Boolean result = webElementUtils.isConditionMet(condition, Timeouts.MINIMAL);
    assertFalse("class attribute substring should NOT be matched", result);
  }

  @Test
  public void elementHasAttribute_shouldNotMatchClassValueSubstring() {
    ExpectedCondition<Boolean> condition =
        CommonExpectedConditions
            .elementHasAttributeWithValue(firstChildDiv, HtmlTags.Attributes.CLASS, "nav-wrapp");
    Boolean result = webElementUtils.isConditionMet(condition, Timeouts.MINIMAL);
    assertFalse("class attribute substring should NOT be matched", result);
  }

  /**
   * As discussed at
   * <A href="https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/1943">Selenium Forum</A>
   * href attribute is returned with domain.
   */
  @Test
  public void elementHasAttribute_shouldMatchHrefValue() {
    String attributeValue = publishUrl + MENS_LINK;
    ExpectedCondition<Boolean> condition =
        CommonExpectedConditions
            .elementHasAttributeWithValue(firstLinkInList, HtmlTags.Attributes.HREF,
                attributeValue);
    Boolean result = webElementUtils.isConditionMet(condition, Timeouts.MINIMAL);
    assertTrue("href attribute should be recognized properly", result);
  }

  @Test
  public void elementHasAttribute_shouldNotMatchHrefValueSubstring() {
    String attributeValue = publishUrl + "/content";
    ExpectedCondition<Boolean> condition =
        CommonExpectedConditions
            .elementHasAttributeWithValue(firstLinkInList, HtmlTags.Attributes.HREF,
                attributeValue);
    Boolean result = webElementUtils.isConditionMet(condition, Timeouts.MINIMAL);
    assertFalse("href attribute should be recognized properly", result);
  }
}
