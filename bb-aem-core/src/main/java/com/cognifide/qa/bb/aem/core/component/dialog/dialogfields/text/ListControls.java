/*
 * Copyright 2018 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.text;

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.qualifier.PageObjectInterface;

/**
 * Interface that represents list formatting options of a Rich Text editor.
 */
@PageObjectInterface
public interface ListControls {

  /**
   * @return {@link WebElement} representing the Bullet List button
   */
  WebElement getBulletListBtn();

  /**
   * @return {@link WebElement} representing the Number List button
   */
  WebElement getNumberListBtn();

  /**
   * @return {@link WebElement} representing the Outdent List button
   */
  WebElement getOutdentListBtn();

  /**
   * @return {@link WebElement} representing the Indent List button
   */
  WebElement getIndentListBtn();
}
