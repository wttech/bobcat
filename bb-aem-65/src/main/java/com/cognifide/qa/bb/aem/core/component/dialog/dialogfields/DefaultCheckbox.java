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

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

/**
 * Default implementation of {@link Checkbox}
 */
@PageObject(xpath = "//*[contains(@class,'coral3-Checkbox')]/..")
public class DefaultCheckbox implements Checkbox {

  @FindBy(css = ".coral3-Checkbox-input")
  private WebElement checkboxElement;

  @FindBy(css = "label.coral3-Checkbox-description")
  private List<WebElement> label;

  @Override
  public void select() {
    checkboxElement.click();
  }

  @Override
  public void setValue(Object value) {
    if (Boolean.valueOf(String.valueOf(value)) != checkboxElement.isSelected()) {
      select();
    }
  }

  @Override
  public String getLabel() {
    return label.isEmpty() ? "" : label.get(0).getText();
  }
}
