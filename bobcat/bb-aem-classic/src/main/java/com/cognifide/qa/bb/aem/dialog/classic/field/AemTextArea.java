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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.DialogComponent;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;

/**
 * Text area in a AemDialog.
 */
@PageObject
@Frame("$cq")
@DialogComponent("text area")
public class AemTextArea extends AbstractTextInput<AemTextArea> implements Configurable {

  private static final String NEW_LINE_CHARACTER = "\n";

  @FindBy(xpath = ".//textarea")
  private WebElement thisAemElement;

  /**
   * Types new line character.
   *
   * @return This AemTextArea instance.
   */
  public AemTextArea typeNewLine() {
    return type(NEW_LINE_CHARACTER);
  }

  /**
   * Types a line of text into the text area.
   *
   * @param text a line of text
   * @return This AemTextArea instance.
   */
  public AemTextArea typeLine(CharSequence text) {
    return type(text).typeNewLine();
  }

  @Override
  public void setValue(String value) {
    clear().type(value);
  }

  @Override
  protected WebElement getField() {
    return thisAemElement;
  }

  /**
   * Clears the text area.
   *
   * @return This AemTextArea instance.
   */
  @Override
  public AemTextArea clear() {
    thisAemElement.clear();
    return this;
  }
}
