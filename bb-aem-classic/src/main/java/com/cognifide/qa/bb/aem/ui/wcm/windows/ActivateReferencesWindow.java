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
package com.cognifide.qa.bb.aem.ui.wcm.windows;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.expectedconditions.WindowActions;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Class responsible for handling activate references dialog.
 */
@PageObject(css = "div[id^='cq-asset-reference-search-dialog-'][style*='visibility: visible']")
@Frame("/")
public class ActivateReferencesWindow implements DecisionWindow {

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(xpath = ".//button[contains(text(), 'Activate')]")
  private WebElement activateButton;

  @FindBy(xpath = ".//button[contains(text(), 'Cancel')]")
  private WebElement cancelButton;

  /**
   * Waits for the window to be displayed.
   *
   * @return this ActivateReferencesWindow
   */
  public ActivateReferencesWindow waitToBeDisplayed() {
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.visibilityOf(currentScope));
    return this;
  }

  /**
   * Confirms the dialog.
   */
  @Override
  public void confirm() {
    bobcatWait.withTimeout(Timeouts.BIG).until(WindowActions.clickButton(activateButton));
  }

  /**
   * Cancels the dialog.
   */
  @Override
  public void cancel() {
    bobcatWait.withTimeout(Timeouts.BIG).until(WindowActions.clickButton(cancelButton));
  }
}
