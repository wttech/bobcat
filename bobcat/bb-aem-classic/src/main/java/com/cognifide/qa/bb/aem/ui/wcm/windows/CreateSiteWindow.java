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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.aem.dialog.classic.field.AemDropdown;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemTextField;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Class responsible for handling Create site dialog.
 */
@PageObject
public class CreateSiteWindow {

  public static final String ID = "cq-createsitewizard";

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  @CurrentScope
  private WebElement currentWindow;

  @CacheLookup
  @FindBy(xpath = ".//input[@name='./jcr:title']/..")
  private AemTextField titleField;

  @CacheLookup
  @FindBy(xpath = ".//input[@name='label']/..")
  private AemTextField labelField;

  @FindBy(css = "div.cq-template-view div.template-item")
  private List<WebElement> templatesList;

  @FindBy(xpath = ".//button[text()='Next']")
  private WebElement nextButton;

  @FindBy(xpath = ".//button[text()='Prev']")
  private WebElement previewButton;

  @FindBy(xpath = ".//button[text()='Cancel']")
  private WebElement cancelButton;

  @FindBy(xpath = ".//button[text()='Create Site']")
  private WebElement creteSiteButton;

  @FindBy(className = "cq-_46_47cq_58siteOwner")
  private AemDropdown ownerDropDown;

  /**
   * @return true if window is displayed.
   */
  public boolean isDisplayed() {
    return currentWindow.isDisplayed();
  }

  /**
   * Waits for the window to be displayed.
   *
   * @return this CreateSiteWindow
   */
  public CreateSiteWindow waitToBeDisplayed() {
    bobcatWait.withTimeout(Timeouts.BIG).until(ExpectedConditions.visibilityOf(currentWindow));
    return this;
  }

  /**
   * Types the title of the created page.
   *
   * @param title title of the created page
   * @return this CreateSiteWindow
   */
  public CreateSiteWindow typeTitle(String title) {
    titleField.setValue(title);
    return this;
  }

  /**
   * Types the name of the created page.
   *
   * @param name of the created page
   * @return this CreateSiteWindow
   */
  public CreateSiteWindow typeName(String name) {
    labelField.setValue(name);
    return this;
  }

  /**
   * Selects the template containing provided name.
   *
   * @param templateName partial template name
   * @return this CreatePageWindow
   */
  public CreateSiteWindow selectTemplateContaining(String templateName) {
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
  public CreateSiteWindow selectTemplate(String exactTemplateName) {
    WebElement template = currentWindow.findElement(By.xpath(".//div[contains(@class, " +
        "'template-title') and text() = \"" + exactTemplateName + "\"]"));
    template.click();
    return this;
  }

  /**
   * Goes to the next step.
   */
  public void next() {
    nextButton.click();
  }

  /**
   * Goes to the preview step.
   */
  public void preview() {
      previewButton.click();
  }

  /**
   * Cancels the dialog
   */
  public void cancel() {
    cancelButton.click();
  }

  /**
   * Confirms last step
   */
  public void createSite() {
    creteSiteButton.click();
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
   * Selects nth template from the list in CreatePageWindow
   *
   * @param i index of the template
   * @return this CreatePageWindow
   */
  public CreateSiteWindow selectNthTemplate(int i) {
    templatesList.get(i).click();
    return this;
  }

  /**
   * Fill data on BluePrint view
   *
   * @param createdSiteTitle site title
   * @param createdSiteName site name
   * @param createdSiteTemplate site template
   * @return this CreateSiteWindow
   */
  public CreateSiteWindow selectBlueprint(String createdSiteTitle, String createdSiteName,
      String createdSiteTemplate) {
    typeTitle(createdSiteTitle);
    typeName(createdSiteName);
    selectTemplate(createdSiteTemplate);
    return this;
  }
  /**
   * Selects languages on Languages view
   *
   * @param languages list of languages
   * @return this CreateSiteWindow
   */
  public CreateSiteWindow selectLanguages(List<String> languages) {
    clickLanguageCheckBox("English (United Kingdom)");
    languages.forEach(this::clickLanguageCheckBox);
    return this;
  }
  /**
   * Selects chapters on Chapters view
   *
   * @param chapters list of languages
   * @return this CreateSiteWindow
   */
  public CreateSiteWindow selectChapters(List<String> chapters) {
    List<WebElement> items = currentWindow.findElements(By.cssSelector(".cq-msm_58chapterPages .x-form-check-wrap"));
    for (WebElement item : items) {
        WebElement label = item.findElement(By.tagName("label"));
        if (!chapters.contains(label.getText())) {
            item.findElement(By.tagName("input")).click();
        }
    }
    return this;
  }

  /**
   * Fill data on Live copy view
   *
   * @param siteOwner site owner name
   * @param liveCopy live copy
   * @param rollOutConfigs list of roll out configurations
   * @return this CreateSiteWindow
   */
  public CreateSiteWindow fillLiveCopy(String siteOwner, boolean liveCopy, List<String> rollOutConfigs) {
      ownerDropDown.selectByText(siteOwner);
      WebElement checkBox = currentWindow.findElement(By.name("isLiveCopy"));
      if (!liveCopy) {
          checkBox.click();
      }

      for (String config : rollOutConfigs) {
          currentWindow.findElement(By.xpath("//span[text()='Add Item']")).click();
          List<WebElement> inputs = currentWindow.findElements(By.xpath("//img[@src='/libs/cq/ui/resources/0.gif']"));
          inputs.get(inputs.size()-1).click();
          List<WebElement> options = currentWindow.findElements(By.xpath("//div[text()='" + config + "']"));
          options.stream().filter(WebElement::isDisplayed).forEach(org.openqa.selenium.WebElement::click);
      }
      return this;
  }

  private void clickLanguageCheckBox(String language) {
    WebElement span = currentWindow.findElement(By.xpath("//span[contains(text(),'" + language + "')]"));
    WebElement label = span.findElement(By.xpath(".."));
    WebElement checkBox = currentWindow.findElement(By.id(label.getAttribute("for")));
    checkBox.click();
  }
}
