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
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields;

import com.cognifide.qa.bb.qualifier.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents checkbox dialog field.
 */
@PageObject
public class Checkbox implements DialogField {

  @FindBy(css = ".coral-Checkbox-input")
  private WebElement checkboxElement;

  /**
   * Performs click action on the checkbox.
   */
  public void select() {
    checkboxElement.click();
  }

  /**
   * Performs click action on the checkbox if passed param is 'true' string.
   *
   * @param value string boolean representation
   */
  @Override
  public void setValue(Object value) {
    if (Boolean.valueOf(String.valueOf(value))) {
      select();
    }
  }

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.CHECKBOX.name();
  }
}
