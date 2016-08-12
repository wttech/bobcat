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
package com.cognifide.qa.bb.aem.dialog.classic.field.lookup;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.ui.AemContentTree;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Path selection window that opens after clicking "search" button in the lookup field.
 */
@PageObject
@Frame("$cq")
public class AemPathWindow {

  @Inject
  private AemContentTree contentTree;

  @FindBy(xpath = ".//div[contains(@class, 'x-window-footer')]//button[text()='OK']")
  private WebElement okButton;

  @FindBy(xpath = ".//div[contains(@class, 'x-window-footer')]//button[text()='Cancel']")
  private WebElement cancelButton;

  /**
   * Clicks "ok" button.
   *
   * @return This AemPathWindow instance.
   */
  public AemPathWindow clickOk() {
    okButton.click();
    return this;
  }

  /**
   * Clicks "cancel" button.
   *
   * @return This AemPathWindow instance.
   */
  public AemPathWindow clickCancel() {
    cancelButton.click();
    return this;
  }

  public AemContentTree getContentTree() {
    return contentTree;
  }
}
