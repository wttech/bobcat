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
package com.cognifide.qa.bb.aem64sp2.core.component.dialog;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.aem.core.component.dialog.DialogFieldRetriever;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.Fields;
import com.cognifide.qa.bb.utils.AopUtil;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;

/**
 * Default AEM 6.4 SP2 Bobcat implementation of the {@link DialogFieldRetriever}
 */
public class DialogFieldRetrieverImpl implements DialogFieldRetriever {

  private static final By FIELD_LOCATOR = By.cssSelector(".coral-Form-fieldwrapper");

  private static final By LABEL_SELECTOR = By
      .cssSelector("label.coral-Form-fieldlabel, label.coral-Form-field");

  private static final By CHECKBOX_LABEL_SELECTOR = By
      .cssSelector("label.coral3-Checkbox-description");
  private static final By IMAGE_LOCATOR = By.cssSelector(".coral-Form-field.cq-FileUpload");
  private static final By CHECKBOX_LOCATOR = By.cssSelector(".coral-Form-field.coral3-Checkbox");
  private static final By RADIO_GROUP_LOCATOR =
      By.cssSelector(".coral-Form-field.coral-RadioGroup");
  private static final By RICHTEXT_LOCATOR = By.cssSelector(".coral-Form-field.cq-RichText");
  private static final By RICHTEXT_FONT_FORMAT_LOCATOR = By.cssSelector(".rte-ui");

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
    List<WebElement> fields = getFields(parentElement, type);

    if (fields.isEmpty()) {
      throw new IllegalStateException(String.format(
          "There are no fields in the tab matching the following: type=%s, label='%s', parent element=%s",
          type, label, parentElement));
    }

    WebElement scope = StringUtils.isEmpty(label) ? fields.get(0) : fields.stream() //
        .filter(field -> containsIgnoreCase(getFieldLabel(field, type), label)) //
        .findFirst() //
        .orElseThrow(() -> new IllegalStateException(
            "Dialog field not found with the provided label: " + label));

    return getFieldObject(scope, type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DialogField getDialogField(WebElement parentElement, String type) {
    WebElement scope = parentElement.findElement(By.tagName("input"));
    return getFieldObject(scope, type);
  }

  private List<WebElement> getFields(WebElement parentElement, String type) {
    List<WebElement> toReturn;
    switch (type) {
      case Fields.IMAGE:
        toReturn = parentElement.findElements(IMAGE_LOCATOR);
        break;
      case Fields.CHECKBOX:
        toReturn = parentElement.findElements(CHECKBOX_LOCATOR);
        break;
      case Fields.RADIO_GROUP_MULTI:
        toReturn = parentElement.findElements(RADIO_GROUP_LOCATOR);
        break;
      case Fields.RICHTEXT:
        toReturn = parentElement.findElements(RICHTEXT_LOCATOR);
        break;
      case Fields.RICHTEXT_FONT_FORMAT:
      case Fields.RICHTEXT_LIST:
      case Fields.RICHTEXT_JUSTIFY:
        toReturn = parentElement.findElements(RICHTEXT_FONT_FORMAT_LOCATOR);
        break;
      default:
        toReturn = parentElement.findElements(FIELD_LOCATOR);
        break;
    }

    return toReturn;
  }

  /**
   * Returns the label of given field. Label may not be present in the field, thus a workaround
   * using list is introduced here.
   *
   * @param field WebElement corresponding to the given field
   * @return label of the field or {@code StringUtils.Empty} when there is none
   */
  private String getFieldLabel(WebElement field, String type) {
    List<WebElement> labelField =
        type.equals(Fields.CHECKBOX) ? field.findElements(CHECKBOX_LABEL_SELECTOR)
            : field.findElements(LABEL_SELECTOR);
    return labelField.isEmpty() ? StringUtils.EMPTY : labelField.get(0).getText();
  }

  private DialogField getFieldObject(WebElement scope, String type) {
    DialogField dialogField = fieldTypeRegistry.get(type);
    return (DialogField) pageObjectInjector
        .inject(AopUtil.getBaseClassForAopObject(dialogField.getClass()), scope);
  }
}
