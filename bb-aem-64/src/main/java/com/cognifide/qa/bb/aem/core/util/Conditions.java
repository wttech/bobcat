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
package com.cognifide.qa.bb.aem.core.util;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.core.component.AuthorLoader;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Class contains conditions for web components.
 */
public class Conditions {

  private static final Logger LOG = LoggerFactory.getLogger(Conditions.class);

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private AuthorLoader authorLoader;

  /**
   * Checks if given web element css class contains given value.
   *
   * @param element {@link WebElement} which css class will be examined.
   * @param value   string value which method will look for.
   * @return true if css class contains given value.
   */
  public boolean classContains(WebElement element, String value) {
    return hasAttributeWithValue(element, HtmlTags.Attributes.CLASS, value);
  }

  /**
   * Checks if {@link ExpectedCondition} given in method parameter is met in small timeout (5
   * seconds)
   *
   * @param condition {@link ExpectedCondition} instance that will be examined.
   * @return true if the condition is met without violating the timeout.
   */
  public boolean isConditionMet(ExpectedCondition condition) {
    return isConditionMet(condition, Timeouts.SMALL);
  }

  /**
   * Checks if {@link ExpectedCondition} given in method parameter is met in given timeout.
   *
   * @param condition {@link ExpectedCondition} instance that will be examined.
   * @param timeout   timeout limit for the condition examination.
   * @return true if condition is met in given timeout.
   */
  public boolean isConditionMet(ExpectedCondition condition, int timeout) {
    boolean result = true;
    try {
      verify(condition, timeout);
    } catch (TimeoutException | StaleElementReferenceException e) {
      result = false;
    }
    return result;
  }

  /**
   * Checks if {@link ExpectedCondition} given in method parameter is met in small timeout (5
   * seconds)
   *
   * @param condition {@link ExpectedCondition} instance  hat will be examined.
   * @return true if the condition is met without violating the timeout.
   */
  public <T> T verify(ExpectedCondition<T> condition) {
    return bobcatWait.until(condition);
  }

  /**
   * Checks if {@link ExpectedCondition} given in method parameter is met in given timeout.
   *
   * @param condition {@link ExpectedCondition} instance that will be examined.
   * @param timeout   timeout limit for the condition examination.
   * @return true if condition is met in given timeout.
   */
  public <T> T verify(ExpectedCondition<T> condition, int timeout) {
    return bobcatWait.until(condition);
  }

  /**
   * Verifies that author mode is loaded (author loader is hidden) and then verifies if given
   * condition is met in medium timeout (15 seconds)
   *
   * @param condition condition that has to be met
   */
  public void verifyPostAjax(ExpectedCondition condition) {
    authorLoader.verifyIsHidden();
    verify(condition, Timeouts.MEDIUM);
  }

  /**
   * Methods examine if condition is met in small timeout (5 seconds), if not nothing happens.
   *
   * @param condition condition that has to be met.
   * @return object or null.
   */
  public Object optionalWait(ExpectedCondition<WebElement> condition) {
    try {
      return bobcatWait.until(condition);
    } catch (TimeoutException ignored) {
      LOG.warn("Condition failed, returing null: {}", ignored);
      return null;
    }
  }

  /**
   * Checks if a WebElement is ready to be operated on, ie. is visible and not stale and returns
   * that element.
   *
   * @param element WebElement to be checked
   * @return checked element
   */
  public WebElement elementReady(WebElement element) {
    return bobcatWait.until(ignored -> {
      try {
        return element.isDisplayed() ? element : null;
      } catch (StaleElementReferenceException e) {
        LOG.warn("Condition failed, returning null: {}", e);
        return null;
      }
    });
  }

  /**
   * Examines if element has attribute value like one passed in parameter.
   *
   * @param element   {@link WebElement} instance that is going to be examined.
   * @param attribute attribute which value will be tested.
   * @param value     expected value of the element attribute
   * @return true if the element has attribute value like one passed in parameter.
   */
  public boolean hasAttributeWithValue(final WebElement element, final String attribute,
      final String value) {
    boolean result = true;
    try {
      bobcatWait.until(input -> element.getAttribute(attribute).contains(value));
    } catch (TimeoutException e) {
      result = false;
    }
    return result;
  }

}
