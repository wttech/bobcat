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

import java.time.LocalDateTime;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;

@PageObject
public class SiteadminToolbar {

  private static final String PUBLISH_LABEL = "Publish";

  private static final String UNPUBLISH_LABEL = "Unpublish";

  private static final String DELETE_LABEL = "Delete";

  private static final String BUTTON_LIST_SELECTOR =
      "div.coral-Popover-content > div.endor-List > a";

  private static final String TOOLBAR_CREATE_BUTTON_SELECTOR = "a.coral-Button[title='Create']";

  private static final String POPOVER_CREATE_PAGE_BUTTON_SELECTOR =
      ".endor-List-item.cq-siteadmin-admin-createpage";

  private static final String MORE_BUTTON_SELECTOR = "i.coral-Icon.coral-Icon--more";

  private static final String PUBLISH_BUTTON_SELECTOR =
      "a.cq-siteadmin-admin-actions-publish-activator[title='Publish']";

  private static final String UNPUBLISH_BUTTON_SELECTOR =
      "a.cq-siteadmin-admin-actions-unpublish-activator[title='Unpublish']";

  private static final String MOVE_BUTTON_SELECTOR = "button.coral-Button[title='Move']";

  @Inject
  private WebDriver driver;

  @Inject
  private WebElementUtils webElementUtils;

  @Inject
  private BobcatWait wait;

  @FindBy(css = "form.cq-siteadmin-admin-createpage")
  private CreatePageWizard createPageWizard;

  @FindBy(css = "button.coral-Button--graniteActionBar.cq-wcm-paste-activator")
  private WebElement pastePageButton;

  @Global
  @FindBy(css = "button.coral-Button[title='Copy']")
  private WebElement copyPageButton;

  @Global
  @FindBy(css = "i.coral-Icon.coral-Icon--delete")
  private WebElement deleteButton;

  @FindBy(css = "div.foundation-ui-notification.coral-Alert.coral-Alert--info")
  private WebElement successInfo;

  @Global
  @FindBy(css = "form.activation-wizard")
  private ReplicateLaterWindow replicateLaterWindow;

  @Global
  @FindBy(css = "form.move-page-wizard")
  private MovePageWizard movePageWizard;

  @Global
  @FindBy(css = "div.coral-Modal.coral-Modal--notice")
  private ConfirmationModal confirmationModal;

  public void publishPage() {
    getMoreButton().click();
    getPublishButton().click();
    clickButtonFromMoreSection(PUBLISH_LABEL);
  }

  public void unpublishPage() {
    getMoreButton().click();
    getUnpublishButton().click();
    clickButtonFromMoreSection(UNPUBLISH_LABEL);
    if (!isSuccessInfoDisplayed() && confirmationModal.isDisplayed()) {
      clickButtonInConfirmationModal("Continue");
    }
  }

  public void copyPage() {
    copyPageButton.click();
  }

  public void pastePage() {
    pastePageButton.click();
  }

  public void movePage(String destination) {
    WebElement moveButton = getMoveButton();
    if(!moveButton.isDisplayed()) {
      getMoreButton().click();
    }
    moveButton.click();
    movePageWizard.moveToDestination(destination);
  }

  public SiteadminToolbar createPage(String title, String templateName) {
    clickCreatePageButton();
    createPageWizard
        .selectTemplate(templateName)
        .provideTitle(title)
        .submit();
    return this;
  }

  public SiteadminToolbar createPage(String title, String name, String templateName) {
    clickCreatePageButton();
    createPageWizard
        .selectTemplate(templateName)
        .provideTitle(title)
        .provideName(name)
        .submit();
    return this;
  }

  public void deletePage() {
    getDeleteButton().click();
    clickButtonInConfirmationModal(DELETE_LABEL);
  }

  public void unpublishPageLater(LocalDateTime localDateTime) {
    getMoreButton().click();
    getUnpublishButton().click();
    clickButtonFromMoreSection("Unpublish later");
    replicateLaterWindow.selectDateAndTime(localDateTime);
    replicateLaterWindow.submit();
  }

  public void publishPageLater(LocalDateTime localDateTime) {
    getMoreButton().click();
    getPublishButton().click();
    clickButtonFromMoreSection("Publish later");
    replicateLaterWindow.selectDateAndTime(localDateTime);
    replicateLaterWindow.submit();
  }

  private boolean isSuccessInfoDisplayed() {
    return webElementUtils.isDisplayed(successInfo, Timeouts.MINIMAL);
  }

  private WebElement getMoreButton() {
    return driver.findElement(By.cssSelector(MORE_BUTTON_SELECTOR));
  }

  private WebElement getMoveButton() {
    return driver.findElement(By.cssSelector(MOVE_BUTTON_SELECTOR));
  }

  private WebElement getPublishButton() {
    return driver.findElement(By.
        cssSelector(PUBLISH_BUTTON_SELECTOR));
  }

  private WebElement getDeleteButton() {
    if (!deleteButton.isDisplayed()) {
      getMoreButton().click();
      wait.withTimeout(1);
    }
    return deleteButton;
  }

  private WebElement getUnpublishButton() {
    return driver.findElement(By.
        cssSelector(UNPUBLISH_BUTTON_SELECTOR));
  }

  private void clickCreatePageButton() {
    driver.findElement(By.cssSelector(TOOLBAR_CREATE_BUTTON_SELECTOR)).click();
    driver.findElement(By.cssSelector(POPOVER_CREATE_PAGE_BUTTON_SELECTOR)).click();
  }

  private void clickButtonFromMoreSection(String buttonLabel) {
    List<WebElement> elements = driver.findElements(By.cssSelector(BUTTON_LIST_SELECTOR));
    elements.stream()
        .filter(t -> t.getText().equals(buttonLabel))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Specified element is not present"))
        .click();
  }

  private void clickButtonInConfirmationModal(String buttonLabel) {
    confirmationModal.clickButton(buttonLabel);
  }

}
