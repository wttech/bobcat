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
package com.cognifide.qa.bb.webelement;

import java.util.ArrayDeque;
import java.util.Deque;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains utility methods for checking with waits if WebElements are meeting different
 * conditions.
 */
public final class WebElementConditions {

  private static final Logger LOG = LoggerFactory.getLogger(WebElementConditions.class);

  /**
   * Condition that checks if specified WebElement has the provided attribute.
   *
   * @param element   WebElement to be checked.
   * @param attribute name of the attribute which presence is to be checked.
   * @return ExpectedCondtion representing the above check
   */
  public static ExpectedCondition<WebElement> elementHasAttribute(WebElement element,
      String attribute) {
    return webDriver -> (element.getAttribute(attribute) != null) ? element : null;
  }

  /**
   * Condition that checks if given attribute has the provided value in the specified WebElement.
   *
   * @param element   WebElement to be checked.
   * @param attribute name of the attribute which presence is to be checked.
   * @param value     value which presence in attribute is to be checked.
   * @return ExpectedCondition representing the above check
   */
  public static ExpectedCondition<WebElement> elementHasAttributeWithValue(final WebElement element,
      final String attribute,
      final String value) {
    return webDriver -> element.getAttribute(attribute).contains(value) ? element : null;
  }

  /**
   * Condition that checks if animation of provided WebElement finished.
   *
   * @param element WebElement to be checked
   * @return ExpectedCondition representing the above check
   */
  public ExpectedCondition<WebElement> hasAnimationFinished(final WebElement element) {
    final Deque<Point> locations = new ArrayDeque<>();
    return webDriver -> {
      Point currentLocation = element.getLocation();
      boolean animationStopped = false;
      if (!locations.isEmpty()) {
        animationStopped = locations.peekFirst().equals(currentLocation);
      }

      locations.addFirst(currentLocation);
      return animationStopped ? element : null;
    };
  }

  /**
   * Condition that checks if provided WebElement is ready to be operated on, ie. is visible and not stale.
   *
   * @param element list of WebElements within which the specified WebElement is searched
   * @return ExpectedCondition representing the above check
   */
  public static ExpectedCondition<WebElement> isReady(final WebElement element) {
    return webDriver -> {
      try {
        return element.isDisplayed() ? element : null;
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        LOG.debug("Element {} not present: {}", element, e);
        return null;
      }
    };
  }
}
