/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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

import static com.cognifide.qa.bb.mapper.field.PageObjectProviderHelper.getSelectorFromClass;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Locators;
import com.cognifide.qa.bb.utils.AopUtil;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;

/**
 * Default AEM 6.5 Bobcat implementation of the {@link DialogFieldRetriever}
 */
public class DefaultDialogFieldRetriever implements DialogFieldRetriever {

  @Inject
  private Map<String, DialogField> fieldTypeRegistry;

  @Inject
  private PageObjectInjector pageObjectInjector;

  /**
   * Finds the dialog field of given type within a WebElement based on the provided label. If label
   * is not present, returns the first field from the tab.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public DialogField getDialogField(WebElement parentElement, String label, String type) {
    List<WebElement> fieldElements = getFieldElements(parentElement, type);

    if (fieldElements.isEmpty()) {
      throw new IllegalStateException(String.format(
          "There are no fields in the tab matching the following: type=%s, parent element=%s",
          type, parentElement));
    }

    return StringUtils.isEmpty(label) ? getFieldObject(fieldElements.get(0), type) :
        fieldElements.stream()
            .map(element -> getFieldObject(element, type))
            .filter(dialogField -> dialogField.getLabel().equals(label))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                String.format("No fields found of type '%s' with label '%s'", type, label)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DialogField getDialogField(WebElement parentElement, String type) {
    WebElement scope = parentElement.findElement(By.tagName("input"));
    return getFieldObject(scope, type);
  }

  private List<WebElement> getFieldElements(WebElement parentElement, String type) {
    By selector =
        getSelectorFromClass(getClassForType(type), pageObjectInjector.getOriginalInjector())
            .orElse(By.cssSelector(".coral-Form-fieldwrapper"));

    return parentElement.findElements(selector);
  }

  private DialogField getFieldObject(WebElement scope, String type) {
    return (DialogField) pageObjectInjector.inject(getClassForType(type), scope);
  }

  private Class<?> getClassForType(String type) {
    DialogField dialogField = fieldTypeRegistry.get(type);
    return AopUtil.getBaseClassForAopObject(dialogField.getClass());
  }
}
