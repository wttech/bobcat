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
package com.cognifide.bdd.demo.po.touchui.components.text.dialog;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

@PageObject
public class ControlToolbar {

  @Inject
  private Actions actions;

  @Inject
  @CurrentScope
  private WebElement scope;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='format#bold']")
  private WebElement toggleBoldButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='format#italic']")
  private WebElement toggleItalicButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='format#underline']")
  private WebElement toggleUnderlineButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='#justify']")
  private WebElement toggleJustifyButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='#lists']")
  private WebElement toggleListButton;

  public void selectText() {
    actions.keyDown(Keys.CONTROL)
            .sendKeys("a")
            .perform();
  }

  public WebElement getScope() {
    return scope;
  }

  public WebElement getToggleBoldButton() {
    return toggleBoldButton;
  }

  public WebElement getToggleItalicButton() {
    return toggleItalicButton;
  }

  public WebElement getToggleUnderlineButton() {
    return toggleUnderlineButton;
  }

  public WebElement getToggleJustifyButton() {
    return toggleJustifyButton;
  }

  public WebElement getToggleListButton() {
    return toggleListButton;
  }

}
