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
package com.cognifide.qa.bb.expectedconditions;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.provider.selenium.BobcatWait;

/**
 * Class contains custom ExpectedConditions for explicit waiting (provided by BobcatWait)
 *
 * @deprecated to be removed in 2.0; use methods from {@link org.openqa.selenium.support.ui.ExpectedConditions} or {@link com.cognifide.qa.bb.webelement.WebElementConditions}
 */
@Deprecated
public final class CommonExpectedConditions {

  private static final Logger LOG = LoggerFactory.getLogger(CommonExpectedConditions.class);

  private CommonExpectedConditions() {
    throw new AssertionError();
  }

  /**
   * Check if element located by specified By locator exists in DOM
   *
   * @param bySelector By locator
   * @return false if element located by By selector does not exist or WebDriver is null
   */
  public static ExpectedCondition<Boolean> elementNotPresent(final By bySelector) {
    return driver -> driver.findElements(bySelector).isEmpty();
  }

  /**
   * Check if element has attribute with provided value
   *
   * @param attributeName  name of the attribute
   * @param attributeValue value of the attribute
   * @param element        WebElement to check
   * @return true if element has attribute with provided value
   */
  public static ExpectedCondition<Boolean> elementHasAttributeWithValue(final WebElement element,
      final String attributeName, final String attributeValue) {
    return driver -> StringUtils.defaultString(element.getAttribute(attributeName))
        .equals(attributeValue);
  }

  /**
   * Check if element located by specified By locator exists in DOM in an
   * element's context
   *
   * @param scope   scope in which element will be searched for
   * @param locator {@link By} locator of the searched element
   * @return false if element does not exist or WebDriver is null
   */
  public static ExpectedCondition<Boolean> scopedElementLocatedByNotPresent(final WebElement scope,
      final By locator) {
    return driver -> scope.findElements(locator).isEmpty();
  }

  /**
   * Check if element is neither present nor visible
   *
   * @param bySelector By locator
   * @return if element is present or visible, or WebDriver is null
   */
  public static ExpectedCondition<Boolean> elementNotPresentOrVisible(final By bySelector) {
    return driver -> {
      boolean result = true;
      try {
        result = driver.findElements(bySelector).isEmpty() || !driver.findElement(bySelector)
            .isDisplayed();
      } catch (StaleElementReferenceException | NoSuchElementException e) {
        LOG.debug("Exception while checking if element not present or visible: '{}'", e);
      }
      return result;
    };
  }

  /**
   * Check if provided element height is greater than expected height
   *
   * @param element        - WebElement to check
   * @param expectedHeight - expected height of an element
   * @return true if element height is greater than expected
   */
  public static ExpectedCondition<Boolean> heightOfElementGreaterThan(final WebElement element,
      final int expectedHeight) {
    return driver -> element.getSize().getHeight() > expectedHeight;
  }

  /**
   * List of WebElements found in provided scope using provided locator is
   * constant
   *
   * @param element   WebElement to set scope for elements finder
   * @param byElement By selector
   * @return true if list of WebElements is the same after one second
   */
  public static ExpectedCondition<Boolean> listSizeIsConstant(final WebElement element,
      final By byElement) {
    return driver -> {
      int before = element.findElements(byElement).size();
      BobcatWait.sleep(1);
      int after = element.findElements(byElement).size();
      return before == after;
    };
  }

  /**
   * Check if NO AEM ajax call is executed
   *
   * @return true if there are no jQuery Ajax active requests for Selenium
   */
  public static ExpectedCondition<Boolean> noAemAjax() {
    return driver -> {
      JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
      return (Boolean) jsExecutor.executeScript("return $CQ.active == 0");
    };
  }
}
