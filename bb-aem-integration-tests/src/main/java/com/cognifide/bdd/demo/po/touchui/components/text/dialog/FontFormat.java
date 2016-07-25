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
package com.cognifide.bdd.demo.po.touchui.components.text.dialog;

import com.cognifide.bdd.demo.po.touchui.GeometrixxFieldTypes;
import com.cognifide.qa.bb.aem.dialog.classic.field.RtButton;
import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.DialogField;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

@PageObject
public class FontFormat implements DialogField {

  @FindBy(css = ".coral-RichText-toolbar")
  private ControlToolbar controlToolbar;

  @Inject
  private BobcatWait bobcatWait;

  @Override
  public void setValue(Object value) {
    String actionText = (String) value;
    RtButton rtAction = RtButton.valueOf(actionText);
    switch (rtAction) {
      case BOLD:
        clickFormatButton(controlToolbar.getToggleBoldButton());
        break;
      case ITALIC:
        clickFormatButton(controlToolbar.getToggleItalicButton());
        break;
      case UNDERLINE:
        clickFormatButton(controlToolbar.getToggleUnderlineButton());
        break;
      default:
        throw new IllegalArgumentException("There is no action defined for " + rtAction);
    }
  }

  private void clickFormatButton(WebElement button) {
    controlToolbar.selectText();
    bobcatWait.withTimeout(Timeouts.SMALL).until((ExpectedCondition<Object>) input -> button.isEnabled());
    button.click();
  }

  @Override
  public String getType() {
    return GeometrixxFieldTypes.FONT_FORMAT.name();
  }

}
