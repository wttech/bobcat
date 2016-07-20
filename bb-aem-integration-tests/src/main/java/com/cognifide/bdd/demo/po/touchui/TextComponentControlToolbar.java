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
package com.cognifide.bdd.demo.po.touchui;

import com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.DialogField;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

@PageObject
public class TextComponentControlToolbar implements DialogField {

  @Inject
  private Actions actions;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='format#bold]")
  private WebElement toggleBoldButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='format#italic']")
  private WebElement toggleItalicButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='format#underline']")
  private WebElement toggleUnderlineButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='#justify]")
  private WebElement toggleJustifyButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='#lists]")
  private WebElement toggleListButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='links#modifylink]")
  private WebElement toggleLinkButton;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='links#unlink]")
  private WebElement toggleUnLinkButton;

  @Override
  public void setValue(Object value) {
    String action = (String) value;
    switch (action) {
      case "BOLD":
        selectText();
        toggleBoldButton.click();
        break;
      case "ITALIC":
        selectText();
        toggleItalicButton.click();
        break;
      case "UNDERLINE":
        selectText();
        toggleUnderlineButton.click();
        break;
      case "JUSTIFY":
        selectText();
        toggleJustifyButton.click();
        break;
      case "LIST":
        selectText();
        toggleListButton.click();
        break;
      case "LINK":
        selectText();
        toggleLinkButton.click();
        break;
      case "UNLINK":
        selectText();
        toggleUnLinkButton.click();
        break;
      default:
        throw new IllegalArgumentException("There is no action defined for " + action);
    }
  }

  @Override
  public String getType() {
    return GeometrixxFieldTypes.TEXT_TOOLBAR.name();
  }

  private void selectText() {
    actions.keyDown(Keys.CONTROL)
            .sendKeys("a")
            .perform();
  }

}
