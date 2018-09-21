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

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.configuration.FieldConfig;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.configuration.MultifieldEntry;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
//TODO
@PageObject
public class MultifieldItem implements DialogField {

  //@Inject
  //private DialogConfigurer dialogConfigurer;

  @Inject
  @CurrentScope
  private WebElement item;

  @FindBy(css = "button.coral-Multifield-remove")
  private WebElement deleteButton;

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.MULTIFIELD_ITEM.name();
  }

  @Override
  public void setValue(Object value) {
    MultifieldEntry entry = (MultifieldEntry) value;
    entry.getItem().stream().forEach(this::setFieldInMultifield);
  }

  public void deleteItem() {
    deleteButton.click();
  }

  /**
   * Sets field of MultifieldItem
   *
   * @param fieldType class of field to be filled
   * @param value     value to be inserted
   * @return this instance for chaining operations
   */
  public MultifieldItem setFieldInMultifield(String fieldType, Object value) {
    //TODO
    //dialogConfigurer.getDialogField(item, fieldType).setValue(value);
    return this;
  }

  private void setFieldInMultifield(FieldConfig fieldConfig) {
    //TODO
    //dialogConfigurer.getDialogField(item, fieldConfig.getType())
    //    .setValue(fieldConfig.getValue());
  }
}
