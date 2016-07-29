/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb.aem.touch.util;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.AuthorLoader;
import com.google.inject.Inject;

public class Conditions {

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private AuthorLoader authorLoader;

  public boolean hasAttributeWithValue(final WebElement element, final String attribute,
      final String value) {
    boolean result = true;
    try {
      bobcatWait.withTimeout(Timeouts.SMALL)
          .until(input -> element.getAttribute(attribute).contains(value));
    } catch (TimeoutException e) {
      result = false;
    }
    return result;
  }

  public boolean classContains(WebElement element, String value) {
    return hasAttributeWithValue(element, HtmlTags.Attributes.CLASS, value);
  }

  public boolean isConditionMet(ExpectedCondition condition) {
    return isConditionMet(condition, Timeouts.SMALL);
  }

  public boolean isConditionMet(ExpectedCondition condition, int timeout) {
    boolean result = true;
    try {
      verify(condition, timeout);
    } catch (TimeoutException | StaleElementReferenceException e) {
      result = false;
    }
    return result;
  }

  public <T> T verify(ExpectedCondition<T> condition) {
    return bobcatWait.withTimeout(Timeouts.SMALL).until(condition);
  }

  public <T> T verify(ExpectedCondition<T> condition, int timeout) {
    return bobcatWait.withTimeout(timeout).until(condition);
  }

  public void verifyPostAjax(ExpectedCondition condition) {
    authorLoader.verifyIsHidden();
    verify(condition, Timeouts.MEDIUM);
  }

  public Object optionalWait(ExpectedCondition<WebElement> condition) {
    Object result = null;
    try {
      result = bobcatWait.withTimeout(Timeouts.SMALL).until(condition);
    } catch (TimeoutException ignored) {
    }
    return result;
  }

  /**
   * Checks if a WebElement is ready to be operated on, ie. is visible and not stale and returns that
   * element.
   *
   * @param element WebElement to be checked
   * @return checked element
   */
  public WebElement elementReady(WebElement element) {
    return bobcatWait.withTimeout(Timeouts.MEDIUM).until(ignored -> {
      try {
        return element.isDisplayed() ? element : null;
      } catch (StaleElementReferenceException e) {
        return null;
      }
    });
  }

  /**
   * Wraps a WebElement's method - executes it in a StaleReferenceElementException-safe way.
   *
   * @param element         element from which method will be invoked
   * @param elementCallable method to be wrapped
   * @param <T>             type of the returned value
   * @return result from the called method
   */
  public <T> T staleSafe(WebElement element, WebElementCallable<T> elementCallable) {
    return verify(ignored -> {
      try {
        return elementCallable.call(element);
      } catch (StaleElementReferenceException e) {
        return null;
      }
    }, Timeouts.MEDIUM);
  }

}
