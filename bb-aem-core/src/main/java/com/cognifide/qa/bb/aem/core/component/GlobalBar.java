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
package com.cognifide.qa.bb.aem.core.component;

import org.openqa.selenium.WebElement;

/**
 * Interface represents authors mode global bar.
 */
public interface GlobalBar {

  /**
   * Performs click() action on toggleSidePanelButton {@link WebElement}.
   */
  void toggleSidePanel();

  /**
   * Checks if edit mode isn't already on, and if not then performs click() action on editModeButton
   * {@link WebElement} and refreshes page.
   */
  void switchToEditMode();

  /**
   * Checks if preview mode isn't already on, and if not then performs click() action on previewModeButton
   * {@link WebElement} and refreshes page.
   */
  void switchToPreviewMode();

  boolean isInPreviewMode();

  boolean isInEditMode();
}
