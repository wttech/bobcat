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
package com.cognifide.qa.bb.aem.ui.wcm.windows;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.aem.dialog.classic.field.AemTextField;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.expectedconditions.WindowActions;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Class responsible for handling Create page dialog.
 */
@PageObject (css = "div[id^='cq-createdialog'][style*='visibility: visible'][style*='display: block']")
public class CreatePageWindow implements DecisionWindow {

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  @CurrentScope
  private WebElement currentWindow;

  @CacheLookup
  @FindBy(xpath = ".//input[@name='title']/..")
  private AemTextField titleField;

  @CacheLookup
  @FindBy(xpath = ".//input[@name='label']/..")
  private AemTextField nameField;

  @FindBy(css = "div.cq-template-view div.template-item")
  private List<WebElement> templatesList;

  @FindBy(xpath = ".//button[text()='Create']")
  private WebElement createButton;

  @FindBy(xpath = ".//button[text()='Cancel']")
  private WebElement cancelButton;

  /**
   * @return true if window is displayed.
   */
  public boolean isDisplayed() {
    return currentWindow.isDisplayed();
  }

  /**
   * Waits for the window to be displayed.
   *
   * @return this CreatePageWindow
   */
  public CreatePageWindow waitToBeDisplayed() {
    bobcatWait.withTimeout(Timeouts.BIG).until(ExpectedConditions.visibilityOf(currentWindow));
    return this;
  }

  /**
   * Types the title of the created page.
   *
   * @param title title of the created page
   * @return this CreatePageWindow
   */
  public CreatePageWindow typeTitle(String title) {
    titleField.setValue(title);
    return this;
  }

  /**
   * Types the name of the created page.
   *
   * @param name of the created page
   * @return this CreatePageWindow
   */
  public CreatePageWindow typeName(String name) {
    nameField.setValue(name);
    return this;
  }

  /**
   * Selects the template containing provided name.
   *
   * @param templateName partial template name
   * @return this CreatePageWindow
   */
  public CreatePageWindow selectTemplateContaining(String templateName) {
    WebElement template = currentWindow.findElement(By.xpath(".//div[contains(@class, " +
        "'template-title') and contains(text(), '" + templateName + "')]"));
    template.click();
    return this;
  }

  /**
   * Selects the template that matches exactly the provided value.
   *
   * @param exactTemplateName exact template name
   * @return this CreatePageWindow
   */
  public CreatePageWindow selectTemplate(String exactTemplateName) {
    WebElement template = currentWindow.findElement(By.xpath(".//div[contains(@class, " +
        "'template-title') and text() = \"" + exactTemplateName + "\"]"));
    template.click();
    return this;
  }

  /**
   * Confirms the dialog and waits for the dialog to be closed.
   */
  @Override
  public void confirm() {
    bobcatWait.withTimeout(Timeouts.BIG).until(WindowActions.clickButton(createButton));
  }

  /**
   * Cancels the dialog and waits for the dialog to be closed.
   */
  @Override
  public void cancel() {
    bobcatWait.withTimeout(Timeouts.BIG).until(WindowActions.clickButton(cancelButton));
  }

  /**
   * Fills the CreatePageWindow's fields, selects the template and confirms the dialog.
   *
   * @param title title of the created page
   * @param name name of the created page
   * @param exactTemplateName exact template name
   */
  public void createPage(String title, String name, String exactTemplateName) {
    typeTitle(title);
    typeName(name);
    selectTemplate(exactTemplateName);
    confirm();
  }

  /**
   * Fills the title field,
   *
   * @param title title of the created page
   * @param exactTemplateName exact template name
   */
  public void createPage(String title, String exactTemplateName) {
    typeTitle(title);
    selectTemplate(exactTemplateName);
    confirm();
  }

  /**
   * Returns the number of templates listed in CreatePageWindow.
   *
   * @return number of templates in the dialog
   */
  public int getTemplatesNumber() {
    return templatesList.size();
  }

  /**
   * @return list of available template names
   */
  public List<String> getTemplatesNames() {
    List<String> names = new ArrayList<>();
    for (WebElement template : templatesList) {
      names.add(template.findElement(By.className("template-title")).getText());
    }
    return names;
  }

  /**
   * Selects nth template from the list in CreatePageWindow
   *
   * @param i index of the template
   * @return this CreatePageWindow
   */
  public CreatePageWindow selectNthTemplate(int i) {
    templatesList.get(i).click();
    return this;
  }
}
