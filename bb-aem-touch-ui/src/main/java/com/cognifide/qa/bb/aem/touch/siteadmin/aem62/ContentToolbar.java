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
package com.cognifide.qa.bb.aem.touch.siteadmin.aem62;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class ContentToolbar {

  private static final String CREATE_BTN_LABEL = "Create";

  @FindBy(css = "a.cq-siteadmin-admin-createpage")
  private WebElement createPageButton;

  @FindBy(css = "button.coral-Button--graniteActionBar[icon='paste']")
  private WebElement pasteButton;

  @Inject
  private WebDriver driver;

  @FindBy(css = "form.cq-siteadmin-admin-createpage")
  @Global
  private CreatePageWizard createPageWizard;

  public CreatePageWizard getCreatePageWizard() {
    return createPageWizard;
  }

  public WebElement getCreateButton() {
    List<WebElement> buttonsLabels = driver.findElements(
        By.cssSelector("button.coral-Button--primary coral-button-label.coral-Button-label"));
    return buttonsLabels.stream().filter(label -> CREATE_BTN_LABEL.equals(label.getText()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Expected element is not found"));
  }

  public WebElement getCreatePageButton() {
    return createPageButton;
  }

  public ContentToolbar pastePage() {
    pasteButton.click();
    return this;
  }
}
