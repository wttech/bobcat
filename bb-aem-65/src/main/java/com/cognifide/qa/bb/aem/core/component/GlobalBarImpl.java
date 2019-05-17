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
package com.cognifide.qa.bb.aem.core.component;

import static com.cognifide.qa.bb.constants.HtmlTags.Attributes.CLASS;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Default Bobcat implementation of {@link GlobalBar} for AEM 6.4
 */
@PageObject
public class GlobalBarImpl implements GlobalBar {

  public static final String IS_SELECTED = "is-selected";

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private WebDriver webDriver;

  @FindBy(css = ".js-editor-SidePanel-toggle")
  private WebElement toggleSidePanelButton;

  @FindBy(css = "button[data-layer='Edit'][autocomplete='off']")
  private WebElement editModeButton;

  @FindBy(css = "button[data-layer='Preview']")
  private WebElement previewModeButton;

  @Override
  public void toggleSidePanel() {
    bobcatWait.until(visibilityOf(toggleSidePanelButton)).click();
  }

  @Override
  public void switchToEditMode() {
    if (!isInEditMode()) {
      editModeButton.click();
      webDriver.navigate().refresh();
    }
  }

  @Override
  public void switchToPreviewMode() {
    if (!isInPreviewMode()) {
      previewModeButton.click();
      webDriver.navigate().refresh();
    }
  }

  @Override
  public boolean isInPreviewMode() {
    return bobcatWait.isConditionMet(attributeContains(previewModeButton, CLASS, IS_SELECTED));
  }

  @Override
  public boolean isInEditMode() {
    return bobcatWait.isConditionMet(attributeContains(editModeButton, CLASS, IS_SELECTED));
  }
}
