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
package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields.text;

import com.cognifide.qa.bb.qualifier.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class JustifyControls {

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='justify#justifyleft']")
  private WebElement justifyLeftBtn;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='justify#justifycenter']")
  private WebElement justifyCenterBtn;

  @FindBy(css = ".coral-RichText-toolbar-item[data-action='justify#justifyright']")
  private WebElement justifyRightBtn;

  public WebElement getJustifyLeftBtn() {
    return justifyLeftBtn;
  }

  public WebElement getJustifyCenterBtn() {
    return justifyCenterBtn;
  }

  public WebElement getJustifyRightBtn() {
    return justifyRightBtn;
  }

}
