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

import com.cognifide.qa.bb.aem.ui.FieldContainer;
import com.cognifide.qa.bb.qualifier.CurrentFrame;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;

/**
 * Represents single item in the AemList.
 */
@PageObject
@Frame("$cq")
public class AemListItem implements FieldContainer {

  @FindBy(css = "button.cq-multifield-up, button.up-btn")
  private WebElement buttonUp;

  @FindBy(css = "button.cq-multifield-down, button.down-btn")
  private WebElement buttonDown;

  @FindBy(css = "button.cq-multifield-remove, button.delete-btn")
  private WebElement buttonRemove;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @CurrentFrame
  private FramePath framePath;

  @Inject
  private PageObjectInjector injector;

  /**
   * Clicks the "up" button.
   *
   * @return This AemListItem.
   */
  public AemListItem up() {
    buttonUp.click();
    return this;
  }

  /**
   * Clicks the "down" button.
   *
   * @return This AemListItem.
   */
  public AemListItem down() {
    buttonDown.click();
    return this;
  }

  /**
   * Clicks the "remove" button.
   */
  public void remove() {
    buttonRemove.click();
  }

  /**
   * Use with classic Multifield dialog widget
   *
   * @param fieldType - field type
   * @param <T> filed type class
   * @return field
   */
  public <T> T getField(Class<T> fieldType) {
    return injector.inject(fieldType, currentScope, framePath);
  }

  /**
   * Use with Complex Multifield dialog widget
   *
   * @param fieldType - field type
   * @param label     - label visible next to field
   * @param <T> filed type class
   * @return field
   */
  @Override
  public <T> T getField(String label, Class<T> fieldType) {
    WebElement element;
    if (!"no label".equals(label)) {
      element = currentScope.findElement(By.xpath(String.format(".//label[text()=%s]/..",
          XpathUtils.quote(label))));
    } else {
      element = currentScope;
    }

    return injector.inject(fieldType, element, framePath);
  }

}
