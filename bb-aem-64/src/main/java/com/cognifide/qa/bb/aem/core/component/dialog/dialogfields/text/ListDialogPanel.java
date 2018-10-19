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
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.FieldType;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Represents list formatting dialog panel.
 */
@PageObject
public class ListDialogPanel implements DialogField {

  @FindPageObject
  private ControlToolbar controlToolbar;

  @FindBy(css = "div.coral-Popover[data-id='lists'] .coral-Popover-content")
  private ListControls listControl;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Performs one of list formatting actions depending on passed parameter value:
   * <ul>
   * <li>NUMBER</li>
   * <li>BULLET</li>
   * <li>INDENT</li>
   * <li>OUTDENT</li>
   * </ul>
   *
   * @param value string value representing desired action.
   */
  @Override
  public void setValue(Object value) {
    String actionText = (String) value;
    ListPanelActions action = ListPanelActions.valueOf(actionText.toUpperCase());

    switch (action) {
      case NUMBERED:
        openListPopover();
        listControl.getNumberListBtn().click();
        break;
      case BULLET:
        openListPopover();
        listControl.getBulletListBtn().click();
        break;
      case INDENT:
        openListPopover();
        listControl.getIndentListBtn().click();
        break;
      case OUTDENT:
        openListPopover();
        listControl.getOutdentListBtn().click();
        break;
      default:
        throw new IllegalArgumentException("There is no action defined for " + actionText);
    }
  }

  private void openListPopover() {
    controlToolbar.selectText();
    bobcatWait.until((ExpectedCondition<Object>) input -> controlToolbar.
        getToggleListButton().isEnabled());
    controlToolbar.getToggleListButton().click();
  }

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.RICHTEXT_LIST.name();
  }

  private enum ListPanelActions {
    NUMBERED,
    BULLET,
    INDENT,
    OUTDENT
  }

}
