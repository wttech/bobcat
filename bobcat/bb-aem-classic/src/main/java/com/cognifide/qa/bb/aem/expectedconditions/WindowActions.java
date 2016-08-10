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
package com.cognifide.qa.bb.aem.expectedconditions;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static methods that provides actions on Sidekick wrapped in ExpectedCondition.
 */
public final class WindowActions {

  private static final Logger LOG = LoggerFactory.getLogger(WindowActions.class);

  private WindowActions() { }

  /**
   * Clicks on button and check if its displayed.
   * <br>
   * When button is not available it returns true.
   *
   * @param button button to click on
   * @return display of button or true if provided web element is not available
   */
  public static ExpectedCondition<Boolean> clickButton(final WebElement button) {
    return input -> {
      try {
        button.click();
        return !button.isDisplayed();
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        LOG.debug("Button is not available at the moment: '{}'", e);
        return true;
      }
    };
  }

}
