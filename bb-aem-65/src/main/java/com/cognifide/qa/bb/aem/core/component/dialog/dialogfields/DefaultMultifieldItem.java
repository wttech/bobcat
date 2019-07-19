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

import com.cognifide.qa.bb.aem.core.component.configuration.FieldConfig;
import com.cognifide.qa.bb.aem.core.component.configuration.MultifieldEntry;
import com.cognifide.qa.bb.aem.core.component.dialog.DialogFieldRetriever;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Default implementation of {@link MultifieldItem}
 */
@PageObject(css = ".coral3-Multifield-item")
public class DefaultMultifieldItem implements MultifieldItem {

  @Inject
  private DialogFieldRetriever dialogFieldRetriever;

  @Inject
  @CurrentScope
  private WebElement item;

  @FindBy(css = "button.coral3-Multifield-remove")
  private WebElement deleteButton;

  @FindBy(css = Locators.LABEL_CSS)
  private List<WebElement> label;

  @Override
  public void setValue(Object value) {
    MultifieldEntry entry = (MultifieldEntry) value;
    entry.getItem().forEach(this::setFieldInMultifield);
  }

  @Override
  public void deleteItem() {
    deleteButton.click();
  }

  private void setFieldInMultifield(FieldConfig fieldConfig) {
    dialogFieldRetriever.getDialogField(item, fieldConfig.getLabel(), fieldConfig.getType())
        .setValue(fieldConfig.getValue());
  }

  @Override
  public String getLabel() {
    return label.isEmpty() ? "" : label.get(0).getText();
  }
}
