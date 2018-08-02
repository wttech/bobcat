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
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.touch.siteadmin.aem61.CreatePageProperties;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class CreatePageWizard {

  private static final String NEXT_BUTTON_LABEL = "Next";
  private static final String CREATE_BUTTON_LABEL = "Create";

  @FindPageObject
  private TemplateList templateList;

  @Global
  @FindBy(css = "div.cq-siteadmin-admin-properties-tabs > div.coral-TabPanel-content")
  private CreatePageProperties createPageProperties;

  @Global
  @FindBy(css = "button.coral-Button")
  private List<WebElement> buttons;

  @Global
  @FindBy(css = ".coral-Modal")
  private WebElement pageCreatedModal;

  @Inject
  protected BobcatWait wait;

  public CreatePageWizard selectTemplate(String templateName) {
    templateList.selectTemplate(templateName);
    getNextButton().click();
    return this;
  }

  public CreatePageWizard provideName(String name) {
    createPageProperties.getNameTextField().sendKeys(name);
    return this;
  }

  public CreatePageWizard provideTitle(String title) {
    createPageProperties.getTitleTextField().sendKeys(title);
    return this;
  }

  public void submit() {
    wait.withTimeout(Timeouts.SMALL).until(input -> getCreateButton().isEnabled(), 3);
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
