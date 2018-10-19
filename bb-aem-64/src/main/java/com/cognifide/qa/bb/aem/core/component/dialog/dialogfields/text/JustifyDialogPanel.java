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

import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.FieldType;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Represents text justification dialog field.
 */
@PageObject
public class JustifyDialogPanel implements DialogField {

  @FindPageObject
  private ControlToolbar controlToolbar;

  @FindBy(css = ".coral-RichText-popover[data-id='justify']")
  private JustifyControls justifyControls;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Performs one of justification actions depending on passed parameter value:
   * <ul>
   * <li>JUSTIFY_LEFT</li>
   * <li>JUSTIFY_RIGHT</li>
   * <li>JUSTIFY_CENTER</li>
   * </ul>
   *
   * @param value string value representing desired action.
   */
  @Override
  public void setValue(Object value) {
    String actionText = (String) value;
    JustifyPanelActions action = JustifyPanelActions.valueOf(actionText.toUpperCase());

    switch (action) {
      case JUSTIFY_LEFT:
        openJustifyPopover();
        justifyControls.getJustifyLeftBtn().click();
        break;
      case JUSTIFY_CENTER:
        openJustifyPopover();
        justifyControls.getJustifyCenterBtn().click();
        break;
      case JUSTIFY_RIGHT:
        openJustifyPopover();
        justifyControls.getJustifyRightBtn().click();
        break;
      default:
        throw new IllegalArgumentException("There is no action defined for " + actionText);
    }
  }

  private void openJustifyPopover() {
    controlToolbar.selectText();
    bobcatWait.until(input -> controlToolbar.getToggleJustifyButton().isEnabled());
    controlToolbar.getToggleJustifyButton().click();
  }

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.RICHTEXT_JUSTIFY.name();
  }

  private enum JustifyPanelActions {
    JUSTIFY_LEFT,
    JUSTIFY_CENTER,
    JUSTIFY_RIGHT
  }
}
