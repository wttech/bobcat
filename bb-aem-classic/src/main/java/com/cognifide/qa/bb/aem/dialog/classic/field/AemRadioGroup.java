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

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.DialogComponent;
import com.cognifide.qa.bb.qualifier.Cached;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;

/**
 * Represents radio group field in a AemDialog.
 */
@PageObject
@Frame("$cq")
@DialogComponent("radio group")
public class AemRadioGroup implements Iterable<AemRadioOption>, Configurable {

  @Cached
  @FindBy(css = ".x-form-item")
  private List<AemRadioOption> values;

  /**
   * @return Currently selected option. Null if no option is selected.
   */
  public AemRadioOption getSelected() {
    for (AemRadioOption option : values) {
      if (option.isSelected()) {
        return option;
      }
    }
    return null;
  }

  /**
   * @return True if any option is selected, false otherwise.
   */
  public boolean isAnySelected() {
    return getSelected() != null;
  }

  /**
   * Searches for the option by the label and selects it.
   *
   * @param label radio group label
   * @return This AemRadioGroup.
   */
  public AemRadioGroup selectByLabel(String label) {
    for (AemRadioOption option : values) {
      if (label.equals(option.getLabel())) {
        option.setSelected();
        break;
      }
    }
    return this;
  }

  /**
   * Searches for the option by the option's value and selects it.
   *
   * @param value option's value
   * @return This AemRadioGroup.
   */
  public AemRadioGroup selectByValue(String value) {
    for (AemRadioOption option : values) {
      if (value.equals(option.getValue())) {
        option.setSelected();
      }
    }
    return this;
  }

  /**
   * @return Value of the currently selected option. Empty string if nothing is selected.
   */
  public String getSelectedValue() {
    AemRadioOption option = getSelected();
    return option == null ? "" : option.getValue();
  }

  @Override
  public Iterator<AemRadioOption> iterator() {
    return values.iterator();
  }

  @Override
  public void setValue(String value) {
    selectByLabel(value);
  }

}
