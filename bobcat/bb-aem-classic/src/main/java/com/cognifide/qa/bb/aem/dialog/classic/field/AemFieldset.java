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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.ui.FieldContainer;
import com.cognifide.qa.bb.aem.utils.FieldValuesSetter;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentFrame;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;

/**
 * Represents fieldset in a AemDialog.
 */
@PageObject
@Frame("$cq")
public class AemFieldset implements FieldContainer {

  @FindBy(css = "div.x-tool-toggle")
  private WebElement foldButton;

  @FindBy(xpath = ".//div[contains(@class,'x-fieldset-bwrap')]")
  private WebElement containerDiv;

  @CurrentFrame
  private FramePath framePath;

  @Inject
  private PageObjectInjector injector;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * @return State of the fieldset. True means folded, false means expanded.
   */
  public boolean isFolded() {
    return "none".equals(containerDiv.getCssValue("display"));
  }

  /**
   * @return State of the fieldset. True means expanded, false means folded.
   */
  public boolean isExpanded() {
    return !isFolded();
  }

  /**
   * Folds the fieldset if not folded already.
   *
   * @return This AemFieldset.
   */
  public AemFieldset fold() {
    if (!isFolded()) {
      foldButton.click();
    }
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> isFolded(), Timeouts.MINIMAL);
    return this;
  }

  /**
   * Expands the fieldset if not expanded already.
   *
   * @return This AemFieldset.
   */
  public AemFieldset expand() {
    if (isFolded()) {
      foldButton.click();
    }
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> !isFolded(), Timeouts.MINIMAL);
    return this;
  }

  /**
   * @return AemElement that represents a field in this fieldset, identified by label.
   */
  @Override
  public <T> T getField(String label, Class<T> dialogFieldType) {
    WebElement element = containerDiv.findElement(By.xpath(String.format(".//label[text()=%s]/..",
        XpathUtils.quote(label))));
    return injector.inject(dialogFieldType, element, framePath);
  }

  /**
   * Set the value in one of the fields in this fieldset, identified by label.
   *
   * @param value           the configuration value to be set
   * @param fieldLabel      label to determine field
   * @param dialogFieldType type to determine field
   * @return This AemFieldset.
   */
  public AemFieldset setItemValue(String value, String fieldLabel, Class<?> dialogFieldType) {
    FieldValuesSetter.setFieldValue(getField(fieldLabel, dialogFieldType), value);
    return this;
  }

  /**
   * Get the value from one of the fields in this fieldset, identified by label
   *
   * @param fieldLabel      label to determine field
   * @param dialogFieldType type to determine field
   * @return field value as String
   */
  public String getItemValue(String fieldLabel, Class<?> dialogFieldType) {
    return FieldValuesSetter.getFieldValue(getField(fieldLabel, dialogFieldType));
  }
}
