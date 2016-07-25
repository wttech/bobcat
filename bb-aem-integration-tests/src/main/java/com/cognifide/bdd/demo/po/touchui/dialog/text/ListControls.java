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
package com.cognifide.bdd.demo.po.touchui.dialog.text;

import com.cognifide.qa.bb.qualifier.PageObject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class ListControls {

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='lists#unordered']")
  private WebElement bulletListBtn;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='lists#ordered']")
  private WebElement numberListBtn;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='lists#outdent']")
  private WebElement outdentListBtn;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='lists#indent']")
  private WebElement indentListBtn;

  public WebElement getBulletListBtn() {
    return bulletListBtn;
  }

  public WebElement getNumberListBtn() {
    return numberListBtn;
  }

  public WebElement getOutdentListBtn() {
    return outdentListBtn;
  }

  public WebElement getIndentListBtn() {
    return indentListBtn;
  }

}
