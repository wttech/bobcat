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
package com.cognifide.qa.bb.aem.core.component.dialog;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.FieldType;
import com.cognifide.qa.bb.utils.AopUtil;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * This class represents TouchUI components dialog configurer.
 */
public class DialogConfigurerImpl implements DialogConfigurer {

  private static final By FIELD_LOCATOR = By.cssSelector(".coral-Form-fieldwrapper");

  private static final By LABEL_SELECTOR = By
      .cssSelector("label.coral-Form-fieldlabel, label.coral-Form-field");

  private static final By CHECKBOX_LABEL_SELECTOR = By
      .cssSelector("label.coral3-Checkbox-description");

  @Inject
  private Map<String, DialogField> fieldTypeRegistry;

  @Inject
  private PageObjectInjector pageObjectInjector;

  /**
   * Finds the dialog field of given type within a WebElement based on the provided label. If label is not
   * present, returns the first field from the tab.
   *
   * @param parentElement parent element from which DialogField will be retrieved
   * @param label         of the requested field
   * @param type          of the requested field
   * @return DialogField of the given type based on the provided info
   */
  @Override
  public DialogField getDialogField(WebElement parentElement, String label, String type) {
    List<WebElement> fields = parentElement.findElements(FIELD_LOCATOR);

    if (fields.isEmpty()) {
      throw new IllegalStateException("There are no fields in the tab");
    }

    WebElement scope = StringUtils.isEmpty(label) ? fields.get(0) : fields.stream() //
        .filter(field -> containsIgnoreCase(getFieldLabel(field,type), label)) //
        .findFirst() //
        .orElseThrow(() -> new IllegalStateException("Dialog field not found"));

    return getFieldObject(scope, type);
  }

  /**
   * Find the dialog input field of given type within a parent WebElement.
   *
   * @param parentElement parent element from which DialogField will be retrieved.
   * @param type          of the requested field.
   * @return DialogField of the given type based on the provided info.
   */
  @Override
  public DialogField getDialogField(WebElement parentElement, String type) {
    WebElement scope = parentElement.findElement(By.tagName("input"));
    return getFieldObject(scope, type);
  }

  /**
   * Returns the label of given field.
   * Label may not be present in the field, thus a workaround using list is introduced here.
   *
   * @param field WebElement corresponding to the given field
   * @param type
   * @return label of the field or {@code StringUtils.Empty} when there is none
   */
  private String getFieldLabel(WebElement field, String type) {
    List<WebElement> labelField = type.equals(FieldType.CHECKBOX.name()) ? field.findElements(CHECKBOX_LABEL_SELECTOR) : field.findElements(LABEL_SELECTOR);
    return labelField.isEmpty() ? StringUtils.EMPTY : labelField.get(0).getText();
  }

  private DialogField getFieldObject(WebElement scope, String type) {
    DialogField dialogField = fieldTypeRegistry.get(type);
    return (DialogField) pageObjectInjector.inject(AopUtil.getBaseClassForAopObject(dialogField.getClass()), scope);
  }
}
