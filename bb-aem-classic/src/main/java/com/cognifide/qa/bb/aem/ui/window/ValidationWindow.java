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

import static com.cognifide.qa.bb.expectedconditions.CommonExpectedConditions.elementNotPresentOrVisible;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Exposes functionality of the Validation Window that appears after submitting a AemDialog with
 * invalid data. When locating this window in a page object use {@link #CSS} with a
 * {@link com.cognifide.qa.bb .qualifier.Global} annotation.
 */
@PageObject(css = "div[id^='cq-msgbox-'][style*='visibility: visible;']")
@Frame("$cq")
public class ValidationWindow {

  private static final String CSS = "div[id^='cq-msgbox-'][style*='visibility: visible;']";

  @Inject
  private BobcatWait bobcatWait;

  @FindBy(css = "span.x-window-header-text")
  private WebElement headerText;

  @FindBy(css = "span.ext-mb-text")
  private WebElement validationMessage;

  @FindBy(xpath = ".//button[text()='OK']")
  private WebElement okButton;

  /**
   * Returns text of the window header.
   *
   * @return header text
   */
  public String getHeaderText() {
    return headerText.getText();
  }

  /**
   * Returns validation message.
   *
   * @return validation message
   */
  public String getValidationMessage() {
    return validationMessage.getText();
  }

  /**
   * Confirms the window by clicking OK button, waits until it's not visible.
   */
  public void confirm() {
    okButton.click();
    bobcatWait.withTimeout(Timeouts.SMALL).until(elementNotPresentOrVisible(By.cssSelector(CSS)));
  }
}
