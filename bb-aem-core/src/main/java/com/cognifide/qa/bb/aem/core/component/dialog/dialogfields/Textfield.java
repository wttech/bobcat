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
 * This class represents single line text dialog field.
 */
@PageObject
public class Textfield implements DialogField {

  @FindBy(css = ".coral-Textfield:not([type='hidden']")
  private WebElement input;

  /**
   * Sets text value of component.
   *
   * @param value desired string value.
   */
  @Override
  public void setValue(Object value) {
    input.clear();
    input.sendKeys(String.valueOf(value));
  }

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.TEXTFIELD.name();
  }
}
