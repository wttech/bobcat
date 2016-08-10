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
package com.cognifide.qa.bb.aem.ui.window;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class ConfirmationWindow {

  private static final Logger LOG = LoggerFactory.getLogger(ConfirmationWindow.class);

  private static final String BUTTON_XPATH_FORMATTED = ".//button[contains(text(),'%s')]";

  private static final String YES_BUTTON_TEXT = "Yes";

  private static final String NO_BUTTON_TEXT = "No";

  @Inject
  private BobcatWait bobcatWait;

  @FindBy(xpath = "//div[contains(@id, 'cq-msgbox-') and contains(@style, 'visibility: visible')]")
  private WebElement window;

  /**
   * @return True if the confirmation window is displayed, false otherwise.
   */
  public boolean isDisplayed() {
    try {
      return window.isDisplayed();
    } catch (NoSuchElementException e) {
      LOG.info("Confirmation window is unavailable", e);
      return false;
    }
  }

  /**
   * Finds the "yes" button in the confirmation window, clicks it and waits until the confirmation
   * window disappears.
   */
  public void acceptConfirmationWindow() {
    clickButton(YES_BUTTON_TEXT);
  }

  /**
   * Finds the "no" button in the confirmation window, clicks it and waits until the confirmation
   * window disappears.
   */
  public void cancelConfirmationWindow() {
    clickButton(NO_BUTTON_TEXT);
  }

  private void clickButton(final String buttonLabel) {
    final WebElement button =
        bobcatWait.withTimeout(Timeouts.BIG).until(input -> window.findElement(
            By.xpath(String.format(BUTTON_XPATH_FORMATTED, buttonLabel))));

    bobcatWait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.elementToBeClickable(button));

    bobcatWait.withTimeout(Timeouts.BIG).until(input -> {
      boolean confirmationWindowClosed;
      try {
        button.click();
        confirmationWindowClosed = !window.isDisplayed();
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        LOG.debug("Confirmation window is not available", e);
        confirmationWindowClosed = true;
      }
      return confirmationWindowClosed;
    });
  }
}
