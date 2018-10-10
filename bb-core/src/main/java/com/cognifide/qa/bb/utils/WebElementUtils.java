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
package com.cognifide.qa.bb.utils;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.webelement.WebElementConditions;
import com.google.inject.Inject;

/**
 * This class contains utility methods for checking with waits if WebElements are meeting different
 * conditions.
 *
 * @deprecated use {@link WebElementConditions} together with {@link com.cognifide.qa.bb.wait.BobcatWait}
 */
@ThreadScoped
@Deprecated
public final class WebElementUtils {

  private static final Logger LOG = LoggerFactory.getLogger(WebElementUtils.class);

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Checks if generic condition is met within default timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param condition condition to be checked.
   * @return value indicating if condition is met within timeout.
   */
  public boolean isConditionMet(final ExpectedCondition<?> condition) {
    return isConditionMet(condition, Timeouts.BIG);
  }

  /**
   * Checks if generic condition is met within specified timeout.
   *
   * @param condition condition to be checked.
   * @param timeout   timeout in seconds within which the condition has to be met.
   * @return value indicating if condition is met within timeout.
   */
  public boolean isConditionMet(final ExpectedCondition<?> condition, int timeout) {
    try {
      bobcatWait.withTimeout(timeout).until(condition);
    } catch (TimeoutException | StaleElementReferenceException e) {
      LOG.debug("Condition has not been made before timeout: ", e);
      return false;
    }
    return true;
  }

  /**
   * Checks if specified WebElement is displayed within default timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param element WebElement to be checked.
   * @return value indicating if WebElement is displayed within timeout.
   */
  public boolean isDisplayed(final WebElement element) {
    return isDisplayed(element, Timeouts.BIG);
  }

  /**
   * Checks if specified WebElement is displayed within specified timeout.
   *
   * @param element WebElement to be checked.
   * @param timeout timeout in seconds within which the WebElement has to be displayed.
   * @return value indicating if WebElement is displayed within timeout.
   */
  public boolean isDisplayed(final WebElement element, int timeout) {
    return isConditionMet(visibilityOf(element), timeout);
  }

  /**
   * Checks if all specified WebElements are displayed within default timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param elements WebElements to be checked.
   * @return value indicating if all WebElements are displayed within timeout.
   */
  public boolean isDisplayed(final List<WebElement> elements) {
    return isDisplayed(elements, Timeouts.BIG);
  }

  /**
   * Checks if all specified WebElements are displayed within specified timeout.
   *
   * @param elements WebElements to be checked.
   * @param timeout  timeout in seconds within which the WebElements have to be displayed.
   * @return value indicating if all WebElements are displayed within timeout.
   */
  public boolean isDisplayed(final List<WebElement> elements, int timeout) {
    return isConditionMet(visibilityOfAllElements(elements), timeout);
  }

  /**
   * Checks if WebElements specified by CSS selector are displayed within default timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param bySelector CSS selector of WebElements to be checked.
   * @return value indicating if all WebElements are displayed within timeout.
   */
  public boolean isDisplayed(final String bySelector) {
    return isDisplayed(bySelector, Timeouts.BIG);
  }

  /**
   * Checks if WebElements specified by CSS selector are displayed within specified timeout.
   *
   * @param bySelector CSS selector of WebElements to be checked.
   * @param timeout    timeout in seconds within which the WebElements have to be displayed.
   * @return value indicating if all WebElements are displayed within timeout.
   */
  public boolean isDisplayed(final String bySelector, int timeout) {
    return isConditionMet(visibilityOfElementLocated(By.cssSelector(bySelector)), timeout);
  }

  /**
   * Checks if specified WebElement is hidden within default timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param element WebElement to be checked.
   * @return value indicating if WebElement is hidden within timeout.
   */
  public boolean isHidden(final WebElement element) {
    return isHidden(element, Timeouts.BIG);
  }

  /**
   * Checks if specified WebElement is hidden within specified timeout.
   *
   * @param element WebElement to be checked.
   * @param timeout timeout in seconds within which the WebElement has to be hidden.
   * @return value indicating if WebElement is hidden within timeout.
   */
  public boolean isHidden(final WebElement element, int timeout) {
    return isConditionMet(not(visibilityOf(element)), timeout);
  }

  /**
   * Checks if all specified WebElements are hidden within default timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param elements WebElements to be checked.
   * @return value indicating if all WebElements are hidden within timeout.
   */
  public boolean isHidden(final List<WebElement> elements) {
    return isHidden(elements, Timeouts.BIG);
  }

  /**
   * Checks if all specified WebElements are hidden within specified timeout.
   *
   * @param elements WebElements to be checked.
   * @param timeout  timeout in seconds within which the WebElements have to be hidden.
   * @return value indicating if all WebElements are hidden within timeout.
   */
  public boolean isHidden(final List<WebElement> elements, int timeout) {
    return isConditionMet(not(visibilityOfAllElements(elements)), timeout);
  }

  /**
   * Checks if WebElements specified by CSS selector are hidden within default timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param bySelector CSS selector of WebElements to be checked.
   * @return value indicating if all WebElements are hidden within timeout.
   */
  public boolean isHidden(final String bySelector) {
    return isHidden(bySelector, Timeouts.BIG);
  }

  /**
   * Checks if WebElements specified by CSS selector are hidden within specified timeout.
   *
   * @param bySelector CSS selector of WebElements to be checked.
   * @param timeout    timeout in seconds within which the WebElements have to be hidden.
   * @return value indicating if all WebElements are hidden within timeout.
   */
  public boolean isHidden(final String bySelector, int timeout) {
    return isConditionMet(not(visibilityOfElementLocated(By.cssSelector(bySelector))), timeout);
  }

  /**
   * Checks if specified WebElement contains specified text within timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param element WebElement to be checked.
   * @param text    text to be present in WebElement.
   * @return value indicating if specified text is present in specified WebElement.
   */
  public boolean isTextPresentInWebElement(WebElement element, String text) {
    return isTextPresentInWebElement(element, text, Timeouts.BIG);
  }

  /**
   * Checks if specified WebElement contains specified text within specified timeout.
   *
   * @param element WebElement to be checked.
   * @param text    text to be present in WebElement.
   * @param timeout timeout in seconds within which the WebElement has to contain specified text.
   * @return value indicating if specified text is present in specified WebElement.
   */
  public boolean isTextPresentInWebElement(WebElement element, String text, int timeout) {
    return isConditionMet(textToBePresentInElement(element, text), timeout);
  }

  /**
   * Checks if specified WebElement has specified attribute within timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param element   WebElement to be checked.
   * @param attribute name of the attribute which presence is to be checked.
   * @return value indicating if specified WebElement has specified attribute.
   */
  public boolean hasAttribute(final WebElement element, final String attribute) {
    return hasAttribute(element, attribute, Timeouts.BIG);
  }

  /**
   * Checks if specified WebElement has specified attribute within specified timeout.
   *
   * @param element   WebElement to be checked.
   * @param attribute name of the attribute which presence is to be checked.
   * @param timeout   timeout in seconds within which the WebElement has to have specified
   *                  attribute.
   * @return value indicating if specified WebElement has specified attribute.
   */
  public boolean hasAttribute(final WebElement element, final String attribute, int timeout) {
    return isConditionMet(webDriver -> element.getAttribute(attribute) != null, timeout);
  }

  /**
   * Checks if specified WebElement has specified attribute with specified value within timeout
   * defined in {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param element   WebElement to be checked.
   * @param attribute name of the attribute which presence is to be checked.
   * @param value     value which presence in attribute is to be checked.
   * @return value indicating if specified WebElement has specified attribute with specified value.
   */
  public boolean hasAttributeWithValue(final WebElement element, final String attribute,
      final String value) {
    return hasAttributeWithValue(element, attribute, value, Timeouts.BIG);
  }

  /**
   * Checks if specified WebElement has specified attribute within specified timeout.
   *
   * @param element   WebElement to be checked.
   * @param attribute name of the attribute which presence is to be checked.
   * @param value     value which presence in attribute is to be checked.
   * @param timeout   timeout in seconds within which the WebElement has to have specified
   *                  attribute.
   * @return value indicating if specified WebElement has specified attribute with specified value.
   */
  public boolean hasAttributeWithValue(final WebElement element, final String attribute,
      final String value, int timeout) {
    return isConditionMet(webDriver -> element.getAttribute(attribute).contains(value), timeout);
  }

  /**
   * Waits for the animation of specified WebElement to be finished within default timeout defined
   * in {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param element WebElement to be checked.
   */
  public void waitForAnimationFinish(final WebElement element) {
    waitForAnimationFinish(element, Timeouts.BIG);
  }

  /**
   * Waits for the animation of specified WebElement to be finished within specified timeout.
   *
   * @param element WebElement to be checked.
   * @param timeout timeout in seconds within which the WebElement's animation has to be finished.
   */
  public void waitForAnimationFinish(final WebElement element, int timeout) {
    final Deque<Point> locations = new ArrayDeque<>();
    isConditionMet(webDriver -> {
      Point currentLocation = element.getLocation();
      boolean animationStopped = false;
      if (!locations.isEmpty()) {
        animationStopped = locations.peekFirst().equals(currentLocation);
      }

      locations.addFirst(currentLocation);
      return animationStopped;
    }, timeout);
  }

  /**
   * Checks if WebElement with specified name is present on the specified list and clicks it if
   * it is on the list.
   *
   * @param elements    list of WebElements within which the specified WebElement is searched.
   * @param elementName name of the WebElement to be clicked.
   * @return WebElement that has been clicked.
   * @throws IllegalArgumentException if element with specified name is not on the list.
   */
  public WebElement clickElementIfExists(final List<WebElement> elements,
      final String elementName) {
    for (WebElement element : elements) {
      if (elementName.equals(element.getText())) {
        element.click();
        return element;
      }
    }
    throw new IllegalArgumentException(String.format("There is no element named %s", elementName));
  }
}
