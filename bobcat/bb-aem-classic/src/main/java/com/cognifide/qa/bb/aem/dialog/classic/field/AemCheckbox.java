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
package com.cognifide.qa.bb.aem.dialog.classic.field;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.DialogComponent;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;

/**
 * This class represents checkbox in a AemDialog.
 */
@PageObject
@Frame("$cq")
@DialogComponent("checkbox")
public class AemCheckbox implements Configurable {

  @FindBy(xpath = ".//input")
  private WebElement thisElement;

  /**
   * Sets the checkbox state to the value indicated by the parameter.
   *
   * @param shouldBeChecked value to be set
   * @return This checkbox instance.
   */
  public AemCheckbox setCheck(Boolean shouldBeChecked) {
    if (shouldBeChecked != null) {
      boolean shouldToggle = !shouldBeChecked.equals(isChecked());
      if (shouldToggle) {
        thisElement.click();
      }
    }
    return this;
  }

  /**
   * @return State of the checkbox. True means checked, false means unchecked.
   */
  public boolean isChecked() {
    return thisElement.isSelected();
  }

  @Override
  public void setValue(String value) {
    setCheck(Boolean.valueOf(value));
  }
}
