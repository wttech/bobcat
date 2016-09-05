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
package com.cognifide.qa.bb.aem.touch.siteadmin.aem61;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class CoralWizard {

  @FindBy(css = "button.coral-Wizard-nextButton[type='submit']")
  protected WebElement submitButton;

  @FindBy(css = "button.coral-Wizard-nextButton[data-foundation-wizard-control-action='next']")
  protected WebElement nextButton;

  public void submit() {
    submitButton.click();
  }

  public void next() {
    nextButton.click();
  }

}
