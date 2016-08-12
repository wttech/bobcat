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
package com.cognifide.qa.bb.aem.touch.pageobjects.touchui;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.touch.util.Conditions;
import com.google.inject.Inject;

/**
 * Class represents dialog with component deleting option.
 */
@PageObject(css = "[class='coral-Modal']")
public class DeleteDialog {

  private static final String CSS = "[class='coral-Modal']";

  @Inject
  private Conditions conditions;

  @FindBy(css = "button.cq-deleteconfirm")
  private WebElement deleteButton;

  /**
   * Method verifies if the dialog is visible, then click delete button and verifies if dialog is not visible
   * anymore.
   */
  public void confirmDelete() {
    By dialogLocator = By.cssSelector(CSS);
    conditions.verifyPostAjax(visibilityOfElementLocated(dialogLocator));
    deleteButton.click();
    conditions.verifyPostAjax(invisibilityOfElementLocated(dialogLocator));
  }
}
