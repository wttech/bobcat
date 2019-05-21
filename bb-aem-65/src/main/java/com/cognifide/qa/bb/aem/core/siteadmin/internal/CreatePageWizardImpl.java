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
package com.cognifide.qa.bb.aem.core.siteadmin.internal;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

import io.qameta.allure.Step;

/**
 * Default Bobcat implementation of {@link CreatePageWizard} for AEM 6.5
 */
@PageObject(css = "form.cq-siteadmin-admin-createpage")
public class CreatePageWizardImpl implements CreatePageWizard {

  private static final String NEXT_BUTTON_LABEL = "Next";
  private static final String CREATE_BUTTON_LABEL = "Create";

  @FindPageObject
  private TemplateList templateList;

  @Global
  @FindPageObject
  private CreatePageProperties createPageProperties;

  @Global
  @FindBy(css = "button.coral3-Button")
  private List<WebElement> buttons;

  @Global
  @FindBy(css = ".coral3-Dialog-wrapper")
  private WebElement pageCreatedModal;

  @Inject
  protected BobcatWait wait;

  @Override
  @Step("Select template {templateName}")
  public CreatePageWizard selectTemplate(String templateName) {
    templateList.selectTemplate(templateName);
    getNextButton().click();
    return this;
  }

  @Override
  @Step("Provide name {name}")
  public CreatePageWizard provideName(String name) {
    createPageProperties.getNameTextField().sendKeys(name);
    return this;
  }

  @Override
  @Step("Provide title {title}")
  public CreatePageWizard provideTitle(String title) {
    createPageProperties.getTitleTextField().sendKeys(title);
    return this;
  }

  @Override
  @Step("Submit page for creation")
  public void submit() {
    wait.until(input -> getCreateButton().isEnabled());
    getCreateButton().click();
    List<WebElement> elements =
        pageCreatedModal.findElements(By.xpath("//coral-button-label[contains(text(),'Done')]"));

    elements.stream()
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("\"Done\" button not found"))
        .click();
  }

  private WebElement getNextButton() {
    return getButtonByLabel(NEXT_BUTTON_LABEL);
  }

  private WebElement getCreateButton() {
    return getButtonByLabel(CREATE_BUTTON_LABEL);
  }

  private WebElement getButtonByLabel(String label) {
    return buttons.stream()
        .filter(
            n -> n.findElement(By.cssSelector("coral-button-label")).getText()
                .equals(label)
        )
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException(
            String.format("Button with label \"%s\" not found", label)));
  }

}
