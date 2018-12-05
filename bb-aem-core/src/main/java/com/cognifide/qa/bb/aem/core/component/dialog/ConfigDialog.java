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
package com.cognifide.qa.bb.aem.core.component.dialog;

import com.cognifide.qa.bb.aem.core.component.configuration.ComponentConfiguration;
import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.qualifier.PageObjectInterface;

/**
 * Represents the component's configuration dialog
 */
@PageObjectInterface
public interface ConfigDialog {

  /**
   * Verify if the dialog is displayed
   */
  void verifyIsDisplayed();

  /**
   * Verify if the dialog is hidden
   */
  void verifyIsHidden();

  /**
   * Verify if the dialog in in fullscreen mode
   */
  void verifyFullscreen();

  /**
   * Confirm the dialog
   */
  void confirm();

  /**
   * Toggles the fullscreen mode
   */
  void toggleFullscreen();

  /**
   * Configures the dialog with provided data.
   *
   * @param config an instance of {@link ComponentConfiguration} holding the desired data
   */
  void configureWith(ComponentConfiguration config);

  /**
   * Retrieves a field from the provided tab in the dialog based on its label and type
   *
   * @param label     label of the field
   * @param tab       tab in which the field is expected to be
   * @param fieldType type of field
   * @return a {@link DialogField} described by provided info
   */
  DialogField getFieldOnTab(String label, String tab, String fieldType);

  /**
   * Sets a value in a field described by provided data
   *
   * @param label     label of the field
   * @param fieldType type of the field
   * @param value     value to be set in the field
   * @return self-reference
   */
  ConfigDialog setField(String label, String fieldType, Object value);

  /**
   * Retrieves a field from the dialog based on its label and type
   *
   * @param label     label of the field
   * @param fieldType type of the field
   * @return a {@link DialogField} representing the requested field
   */
  DialogField getField(String label, String fieldType);

  /**
   * Switches to the provided tab
   *
   * @param tabLabel label of the tab to switch to
   * @return self-reference
   */
  ConfigDialog switchTab(String tabLabel);
}
