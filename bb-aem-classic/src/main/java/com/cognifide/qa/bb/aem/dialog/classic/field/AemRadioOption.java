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

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;

/**
 * Single option in AemRadioGroup.
 */
@PageObject
@Frame("$cq")
public class AemRadioOption {

  @FindBy(css = "label.x-form-cb-label-hidden,label.x-form-item-label")
  private List<WebElement> labels;

  @FindBy(xpath = ".//input[contains(@class,'x-form-radio')]")
  private WebElement input;

  /**
   * Support for both widgets (xtype=radiogroup) and (xtype=selection, type=radio). Depends on which widget
   * is used the first or second label is filled.
   *
   * @return label of element or "" in case of no label
   */
  public String getLabel() {
    for (WebElement element : labels) {
      String label = getUnescapedText(element.getText());
      if (StringUtils.isNotBlank(label)) {
        return label;
      }
    }
    return "";
  }

  /**
   * @return True if the option is selected. False if not selected.
   */
  public boolean isSelected() {
    return input.isSelected();
  }

  /**
   * Selects the option.
   *
   * @return This AemRadioOption instance.
   */
  public AemRadioOption setSelected() {
    input.click();
    return this;
  }

  /**
   * @return Value of the option.
   */
  public String getValue() {
    return input.getAttribute("value");
  }

  /**
   * Replace html entities to unicode characters e.g. &nbsp; to space character
   *
   * @param text
   * @return String after replacement
   */
  private static String getUnescapedText(String text) {
    return StringEscapeUtils.unescapeHtml4(text);
  }

}
