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
package com.cognifide.qa.bb.aem.ui.wcm;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.ui.AemContentTree;
import com.cognifide.qa.bb.aem.ui.wcm.constants.ActivationStatus;
import com.cognifide.qa.bb.aem.ui.wcm.constants.PageStatus;
import com.cognifide.qa.bb.aem.ui.wcm.constants.SiteAdminButtons;
import com.cognifide.qa.bb.aem.ui.wcm.elements.SiteAdminActionBar;
import com.cognifide.qa.bb.aem.ui.wcm.elements.SiteAdminGrid;
import com.cognifide.qa.bb.aem.ui.wcm.windows.CreatePageWindow;
import com.cognifide.qa.bb.aem.ui.wcm.windows.CreateSiteWindow;
import com.cognifide.qa.bb.aem.ui.wcm.windows.MovePageWindow;
import com.cognifide.qa.bb.aem.ui.wcm.windows.PastePageWindow;
import com.cognifide.qa.bb.aem.ui.wcm.windows.ReplicateLaterWindow;
import com.cognifide.qa.bb.aem.ui.wcm.windows.SiteAdminConfirmationWindow;
import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Exposes functionality of whole Siteadmin page.
 */
@PageObject
public class SiteAdminPage {

  private static final Logger LOG = LoggerFactory.getLogger(SiteAdminPage.class);

  private static final String WCM_PATH = "/siteadmin";

  private static final String AEM_WCM_TITLE = "AEM WCM";

  @Inject
  private WebDriver webDriver;

  @Inject
  private BobcatWait bobcatWait;

  @FindPageObject
  private AemContentTree contentTree;

  @Inject
  @Named(AemConfigKeys.AUTHOR_URL)
  private String authorDomain;

  @FindPageObject
  private SiteAdminGrid grid;

  @FindPageObject
  private CreatePageWindow createPageWindow;

  @FindPageObject
  private SiteAdminConfirmationWindow siteAdminConfirmationWindow;

  @FindPageObject
  private MovePageWindow movePageWindow;

  @FindPageObject
  private PastePageWindow pastePageWindow;

  @FindBy(xpath = "//*[@id='cq-deactivate-later-dialog-0' or @id='cq-activate-later-dialog-0']")
  private ReplicateLaterWindow replicateLaterWindow;

  @FindBy(id = CreateSiteWindow.ID)
  private CreateSiteWindow createSiteWindow;

  /**
   * @return Site Admin grid
   */
  public SiteAdminGrid getGrid() {
    return grid;
  }

  /**
   * Opens Site Admin at specified node: http://domain/siteadmin#/nodePath
   *
   * @param nodePath path to the node
   * @return Site Admin grid
   */
  public SiteAdminPage open(String nodePath) {
    webDriver.get(authorDomain + WCM_PATH + "#" + nodePath);
    String wcmTitle = getWcmTitle();
    if (StringUtils.isNotBlank(nodePath)) {
      bobcatWait.withTimeout(Timeouts.BIG).until(not(titleIs(wcmTitle)));
    } else {
      bobcatWait.withTimeout(Timeouts.BIG).until(titleIs(wcmTitle));
    }
    grid.getActionBar().waitToBeDisplayed();
    grid.waitForLoaderNotPresent();
    return this;
  }

  /**
   * Opens Site Admin main page: http://domain/siteadmin#.
   *
   * @return This SiteAdminPage
   */
  public SiteAdminPage open() {
    return open("");
  }

  /* Actions on Grid */

  /**
   * Creates a new page based on provided the values. During creation specified template is used in
   * CreatePageWindow.
   *
   * @param title title of the created page
   * @param name name of the created page
   * @param templateName template of the created page
   * @return this SiteadminPage
   */
  public SiteAdminPage createNewPage(String title, String name, String templateName) {
    openCreatePageWindow();
    createPageWindow.createPage(title, name, templateName);
    grid.waitForLoaderNotPresent();
    return this;
  }

  /**
   * Assumes that the "Create Page" dialog has been already opened and creates a new page based on
   * provided the values. During creation specified template is used in CreatePageWindow.
   *
   * @param title title of the created page
   * @param name name of the created page
   * @param templateName template of the created page
   * @return this SiteadminPage
   */
  public SiteAdminPage createNewPageReusingDialog(String title, String name, String templateName) {
    createPageWindow.createPage(title, name, templateName);
    grid.waitForLoaderNotPresent();
    return this;
  }

  /**
   * Creates a new page based on the provided values. name is omitted (default one is set by AEM).
   * During creation specified template is used in CreatePageWindow
   *
   * @param title title of the created page
   * @param templateName template of the created page
   * @return this SiteadminPage
   */
  public SiteAdminPage createNewPage(String title, String templateName) {
    openCreatePageWindow();
    createPageWindow.createPage(title, templateName);
    grid.waitForLoaderNotPresent();
    return this;
  }

  /**
   * Opens CreateSiteWindow
   *
   * @return CreateSiteWindow
   */
  public CreateSiteWindow openCreateSiteWindow() {
    grid.getActionBar().expandDropDown(SiteAdminButtons.NEW);
    grid.getActionBar().clickDropDownOption(SiteAdminButtons.NEW_SITE);
    createSiteWindow.waitToBeDisplayed();
    return createSiteWindow;
  }

  /**
   * Assumes that the "Create Page" dialog has been already opened and creates a new page based on
   * provided the values. During creation specified template is used in CreatePageWindow.
   *
   * @param title title of the created page
   * @param templateName template of the created page
   * @return this SiteAdminPage
   */
  public SiteAdminPage createNewPageReusingDialog(String title, String templateName) {
    createPageWindow.createPage(title, templateName);
    grid.waitForLoaderNotPresent();
    return this;
  }

  /**
   * Activates the page with provided title by selecting it and pressing Activate button on Action
   * Bar.
   *
   * @param title title of the page
   * @return this SiteadminPage
   */
  public SiteAdminPage activatePage(String title) {
    grid.activatePage(title);
    waitForPageActivationStatus(title, ActivationStatus.ACTIVATED);
    return this;
  }

  /**
   * Activates the page with provided title by selecting it and pressing Activate Later button in
   * Activate drop down.
   *
   * @param title title of the page
   * @param day day in format dd/mm/yy
   * @param time time in format hh:mm a (10:12 AM)
   * @return this SiteadminPage
   */
  public SiteAdminPage activatePageLater(String title, String day, String time) {
    grid.selectPageByTitle(title);
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(driver -> {
      grid.getActionBar().expandDropDown(SiteAdminButtons.ACTIVATE);
      WebElement button = webDriver.findElement(SiteAdminButtons.ACTIVATE_LATER.getLocator());
      return button.isDisplayed() && !button.findElement(By.xpath(".."))
          .getAttribute(HtmlTags.Attributes.CLASS)
          .contains("x-item-disabled");
    });
    grid.getActionBar().clickDropDownOption(SiteAdminButtons.ACTIVATE_LATER);
    replicateLaterWindow.fillDay(day);
    replicateLaterWindow.fillTime(time);
    replicateLaterWindow.confirm();
    grid.waitForLoaderNotPresent();
    waitForPageStatus(title, PageStatus.SCHEDULED_ACTIVATION);
    return this;
  }

  /**
   * Deactivates the page with provided title by selecting it and pressing Deactivate Later button
   * in Deactivate drop down.
   *
   * @param title title of the page
   * @param day day in format dd/mm/yy
   * @param time time in format hh:mm a (10:12 AM)
   * @return this SiteadminPage
   */
  public SiteAdminPage deactivatePageLater(String title, String day, String time) {
    grid.selectPageByTitle(title);
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(driver -> {
      grid.getActionBar().expandDropDown(SiteAdminButtons.DEACTIVATE);
      WebElement button = webDriver.findElement(SiteAdminButtons.DEACTIVATE_LATER.getLocator());
      return button.isDisplayed() && !button.findElement(By.xpath(".."))
          .getAttribute(HtmlTags.Attributes.CLASS)
          .contains("x-item-disabled");
    });
    grid.getActionBar().clickDropDownOption(SiteAdminButtons.DEACTIVATE_LATER);
    replicateLaterWindow.fillDay(day);
    replicateLaterWindow.fillTime(time);
    replicateLaterWindow.confirm();
    grid.waitForLoaderNotPresent();
    waitForPageStatus(title, PageStatus.SCHEDULED_DEACTIVATION);
    return this;
  }

  /**
   * Deactivates the page with provided title by selecting it and pressing Dectivate button on
   * Action Bar.
   *
   * @param title title of the page
   * @return this SiteAdminPage
   */
  public SiteAdminPage deactivatePage(String title) {
    grid.deactivatePage(title);
    waitForPageActivationStatus(title, ActivationStatus.DEACTIVATED);
    return this;
  }

  /**
   * Copies the page with provided title by selecting it and pressing Copy button on Action Bar.
   *
   * @param title title of the page
   * @return this SiteAdminPage
   */
  public SiteAdminPage copyPage(String title) {
    grid.selectPageByTitle(title);
    final SiteAdminActionBar siteAdminActionBar = grid.getActionBar();
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      siteAdminActionBar.clickOnButton(SiteAdminButtons.COPY);
      return siteAdminActionBar.isButtonEnabled(SiteAdminButtons.PASTE);
    }, 2);
    grid.waitForLoaderNotPresent();
    return this;
  }

  /**
   * Opens SiteAdmin at specified node: http://domain/siteadmin#/nodePath and pastes the last copied
   * page
   *
   * @param parentPath parent path to the node
   * @return this SiteAdminPage
   */
  public SiteAdminPage pastePage(String parentPath) {
    open(parentPath);
    grid.getActionBar().clickOnButton(SiteAdminButtons.PASTE, pastePageWindow);
    pastePageWindow.confirm();
    grid.waitForLoaderNotPresent();
    return this;
  }

  /**
   * Deletes the page with provided title by selecting it and pressing Delete button on Action Bar.
   * Verifies that page is deleted.
   *
   * @param title title of the page
   * @return this SiteAdminPage
   */
  public SiteAdminPage deletePage(String title) {
    clickDeleteAndConfirm(title);
    grid.waitForLoaderNotPresent();
    waitForPageNotExists(title);
    return this;
  }

  /**
   * Waits for page not exists in site admin grid.
   *
   * @param title title of the page
   * @return this SiteAdminPage
   */
  public SiteAdminPage waitForPageNotExists(String title) {
    bobcatWait.withTimeout(Timeouts.BIG).until(not(pageExists(title)), Timeouts.SMALL);
    return this;
  }

  /**
   * Deletes the page with provided title by selecting it and pressing Delete button on Action Bar.
   *
   * @param title title of the page
   * @return this SiteAdminPage
   */
  public SiteAdminPage clickDeleteAndConfirm(String title) {
    grid.selectPageByTitle(title);
    grid.getActionBar().clickOnButton(SiteAdminButtons.DELETE, siteAdminConfirmationWindow);
    waitForConfirmationWindow();
    clickYesOnConfirmationWindow();
    grid.waitForLoaderNotPresent();

    if(siteAdminConfirmationWindow.isVisible()) {
      // "This page is referenced" confirmation window have popped up
      clickYesOnConfirmationWindow();
      grid.waitForLoaderNotPresent();
    }
    return this;
  }

  /**
   * Click yes on any confirmation window in siteadmin.
   *
   * @return this SiteAdminPage
   */
  public SiteAdminPage clickYesOnConfirmationWindow() {
    siteAdminConfirmationWindow.confirm();
    return this;
  }

  /**
   * Waits for confirmation window in siteadmin.
   *
   * @return this SiteAdminPage
   */
  public SiteAdminPage waitForConfirmationWindow() {
    siteAdminConfirmationWindow.waitToBeDisplayed();
    return this;
  }

  /**
   * Moves the page with provided title to provided destination path by selecting it and pressing
   * Move button on Action Bar
   *
   * @param title page title
   * @param destinationPath destination page path
   * @return this SiteadminPage
   */
  public SiteAdminPage movePage(String title, String destinationPath) {
    grid.selectPageByTitle(title);
    grid.getActionBar().clickOnButton(SiteAdminButtons.MOVE, movePageWindow);
    movePageWindow.typeDestinationPath(destinationPath);
    movePageWindow.confirm();
    clickYesOnConfirmationWindow();
    grid.waitForLoaderNotPresent();
    return this;
  }

  /**
   * Checks if a page with provided title is displayed in the SiteAdminGrid.
   *
   * @param title title of the page
   * @return true if page is present
   */
  public boolean isPagePresent(String title) {
    return grid.isPageOnTheList(title);
  }

  /**
   * Checks if a specified template with provided title is displayed in the SiteAdminGrid.
   *
   * @param title title of the template
   * @param template name of the template
   * @return true if template is present
   */
  public boolean isTemplateOnTheList(String title, String template) {
    return grid.isTemplateOnList(title, template);
  }

  public AemContentTree getContentTree() {
    return contentTree;
  }

  /**
   * @return Page title.
   */
  public String getWcmTitle() {
    return AEM_WCM_TITLE;
  }

  /**
   * Click button New on SiteAdminActionBar
   *
   * @return CreatePageWindow
   */
  public CreatePageWindow openCreatePageWindow() {
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      try {
        grid.getActionBar().clickOnButton(SiteAdminButtons.NEW);
        return createPageWindow.isDisplayed();
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        LOG.debug("Create page window is not available at the moment: {}", e);
        return Boolean.FALSE;
      }
    }, 2);
    return createPageWindow;
  }

  private ExpectedCondition<Boolean> pageExists(final String pageTitle) {
    return driver -> isPagePresent(pageTitle);
  }

  private void waitForPageActivationStatus(String title, ActivationStatus status) {
    try {
      bobcatWait.withTimeout(Timeouts.BIG)
          .until(grid.pageActivationStatusIs(status, this, title));
    } catch (TimeoutException te) {
      LOG.error("Error while activating page");
      throw te;
    }
  }

  private void waitForPageStatus(String title, PageStatus status) {
    try {
      bobcatWait.withTimeout(Timeouts.BIG).until(grid.pageStatusIs(status, this, title));
    } catch (TimeoutException te) {
      LOG.error("Error while activating page");
      throw te;
    }
  }

}
