/*
 * Copyright 2016 Cognifide Ltd..
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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.javascriptexecutor.JsScripts;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Implementation of {@link ControlToolbar} for AEM 6.4
 */
@PageObject(css = ".coral3-ButtonGroup.rte-toolbar.is-active")
public class ControlToolbarImpl implements ControlToolbar {

  static final String TOOLBAR_ITEM_SELECTOR = ".rte-toolbar-item";

  @Inject
  @CurrentScope
  private WebElement scope;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='format#bold']")
  private WebElement toggleBoldButton;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='format#italic']")
  private WebElement toggleItalicButton;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='format#underline']")
  private WebElement toggleUnderlineButton;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='#justify']")
  private WebElement toggleJustifyButton;

  @FindBy(css = TOOLBAR_ITEM_SELECTOR + "[data-action='#lists']")
  private WebElement toggleListButton;

  @Inject
  private JavascriptExecutor javascriptExecutor;

  @Override
  public void selectText() {
    javascriptExecutor.executeScript(JsScripts.SELECT_ALL);
  }

  @Override
  public WebElement getScope() {
    return scope;
  }

  @Override
  public WebElement getToggleBoldButton() {
    return toggleBoldButton;
  }

  @Override
  public WebElement getToggleItalicButton() {
    return toggleItalicButton;
  }

  @Override
  public WebElement getToggleUnderlineButton() {
    return toggleUnderlineButton;
  }

  @Override
  public WebElement getToggleJustifyButton() {
    return toggleJustifyButton;
  }

  @Override
  public WebElement getToggleListButton() {
    return toggleListButton;
  }

}
