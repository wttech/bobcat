/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text;

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.qualifier.PageObjectInterface;

/**
 * Interface that represents the controls toolbar of a RichText.
 */
@PageObjectInterface
public interface ControlToolbar {

  /**
   * Performs 'select all text' action.
   */
  void selectText();

  /**
   * This should return an element obtained from:
   * <p>
   * {@code @Inject @CurrentScope private WebElement currentScope}
   *
   * @return {@link WebElement} representing the current scope of this element
   */
  WebElement getScope();

  /**
   * @return {@link WebElement} representing the Bold button
   */
  WebElement getToggleBoldButton();

  /**
   * @return {@link WebElement} representing the Italic button
   */
  WebElement getToggleItalicButton();

  /**
   * @return {@link WebElement} representing the Underline button
   */
  WebElement getToggleUnderlineButton();

  /**
   * @return {@link WebElement} representing the Justify button
   */
  WebElement getToggleJustifyButton();

  /**
   * @return {@link WebElement} representing the List button
   */
  WebElement getToggleListButton();
}
