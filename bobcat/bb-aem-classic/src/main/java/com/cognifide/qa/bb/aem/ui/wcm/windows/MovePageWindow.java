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

import com.cognifide.qa.bb.qualifier.FindPageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.aem.dialog.classic.field.lookup.AemPathWindow;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.dialog.classic.field.lookup.AemLookupField;
import com.cognifide.qa.bb.aem.expectedconditions.WindowActions;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.CurrentScopeHelper;
import com.google.inject.Inject;

/**
 * Class responsible for handling move page dialog.
 */
@PageObject(css = "div[style*='visible'] div.x-window-bwrap")
public class MovePageWindow implements DecisionWindow {

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private CurrentScopeHelper webElementHelper;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(xpath = ".//label[text()='Destination']/..")
  private AemLookupField lookupField;

  @FindBy(xpath = ".//button[contains(text(), 'Move')]")
  private WebElement moveButton;

  @FindBy(xpath = ".//button[contains(text(), 'Cancel')]")
  private WebElement cancelButton;

  @Global
  @FindPageObject
  private SiteAdminConfirmationWindow siteAdminConfirmationWindow;

  /**
   * Waits for the window to be displayed.
   *
   * @return This MovePageWindow
   */
  public MovePageWindow waitToBeDisplayed() {
    bobcatWait.withTimeout(Timeouts.BIG).until(ExpectedConditions.visibilityOf(currentScope));
    return this;
  }

  /**
   * Sets destination page path by opening the content tree and selecting indicated path.
   *
   * @param destinationPath destination page path.
   * @return this MovePageWindow
   */
  public MovePageWindow typeDestinationPath(String destinationPath) {
    AemPathWindow pathWindow = lookupField.openPathWindow();
    pathWindow.getContentTree().selectPath(destinationPath);
    pathWindow.clickOk();
    return this;
  }

  /**
   * Confirms the dialog.
   */
  @Override
  public void confirm() {
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      moveButton.click();
      return webElementHelper.isCurrentScopeVisible(siteAdminConfirmationWindow);
    }, 2);
  }

  /**
   * Cancels the dialog.
   */
  @Override
  public void cancel() {
    bobcatWait.withTimeout(Timeouts.BIG).until(WindowActions.clickButton(cancelButton));
  }
}
