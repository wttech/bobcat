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
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * This class represents variant dialog field.
 */
@PageObject
public class Variant implements DialogField {

  @FindBy(css = ".coral-Select-select")
  private WebElement select;

  @FindBy(css = "ol.coral-TagList li button")
  private List<WebElement> tagButtons;

  /**
   * Method selects given options of variant component.
   *
   * @param value String value of comma delimited variants which will be selected.
   */
  @Override
  public void setValue(Object value) {
    org.openqa.selenium.support.ui.Select selectElement = new org.openqa.selenium.support.ui.Select(
        select);
    List<String> values = Arrays.asList(String.valueOf(value).split(","));

    tagButtons.stream().forEach(WebElement::click);
    values.stream().forEach(selectElement::selectByVisibleText);
  }

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.VARIANT.name();
  }
}
