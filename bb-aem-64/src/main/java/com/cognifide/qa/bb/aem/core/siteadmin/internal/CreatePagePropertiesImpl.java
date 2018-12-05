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
package com.cognifide.qa.bb.aem.core.siteadmin.internal;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Default Bobcat implementation of {@link CreatePageProperties} for AEM 6.4
 */
@PageObject(css = "div.cq-dialog-content-page")
public class CreatePagePropertiesImpl implements CreatePageProperties {

  @FindBy(css = "input.coral-Form-field.coral3-Textfield[name='pageName']")
  private WebElement nameTextField;

  @FindBy(css = "input.coral-Form-field.coral3-Textfield[name='./jcr:title']")
  private WebElement titleTextField;

  @Inject
  private BobcatWait bobcatWait;

  @Override
  public WebElement getNameTextField() {
    return nameTextField;
  }

  @Override
  public WebElement getTitleTextField() {
    return bobcatWait.until(visibilityOf(titleTextField));
  }

}
