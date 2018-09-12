/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text;

import static com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text.ControlToolbarImpl.TOOLBAR_ITEM_SELECTOR;

import com.cognifide.qa.bb.qualifier.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Implementation of {@link ListControls} for AEM 6.4
 */
@PageObject
public class ListControlsImpl implements ListControls {

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='lists#unordered']")
  private WebElement bulletListBtn;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='lists#ordered']")
  private WebElement numberListBtn;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='lists#outdent']")
  private WebElement outdentListBtn;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='lists#indent']")
  private WebElement indentListBtn;

  @Override
  public WebElement getBulletListBtn() {
    return bulletListBtn;
  }

  @Override
  public WebElement getNumberListBtn() {
    return numberListBtn;
  }

  @Override
  public WebElement getOutdentListBtn() {
    return outdentListBtn;
  }

  @Override
  public WebElement getIndentListBtn() {
    return indentListBtn;
  }

}
