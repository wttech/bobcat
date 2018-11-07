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

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.FieldType;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Represents font format dialog
 */
@PageObject
public class FontFormat implements DialogField {

  @FindPageObject
  private ControlToolbar controlToolbar;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Performs one of formatting actions depending on passed parameter value:
   * <ul>
   * <li>BOLD</li>
   * <li>ITALIC</li>
   * <li>UNDERLINE</li>
   * </ul>
   *
   * @param value string representing desired action
   */
  @Override
  public void setValue(Object value) {
    String actionText = (String) value;
    TouchUIRtButton rtAction = TouchUIRtButton.valueOf(actionText);
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

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.RICHTEXT_FONT_FORMAT.name();
  }

  private void clickFormatButton(WebElement button) {
    controlToolbar.selectText();
    bobcatWait.until(input -> button.isEnabled());
    button.click();
  }

  private enum TouchUIRtButton {
    BOLD,
    UNDERLINE,
    ITALIC
  }

}
