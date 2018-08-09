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
package com.cognifide.qa.bb.aem.ui.wcm.elements;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.ui.wcm.SiteAdminPage;
import com.cognifide.qa.bb.aem.ui.wcm.constants.ActivationStatus;
import com.cognifide.qa.bb.aem.ui.wcm.constants.PageStatus;
import com.cognifide.qa.bb.aem.ui.wcm.constants.SiteAdminButtons;
import com.cognifide.qa.bb.aem.ui.wcm.windows.ActivateReferencesWindow;
import com.cognifide.qa.bb.aem.ui.wcm.windows.SiteAdminConfirmationWindow;
import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.expectedconditions.CommonExpectedConditions;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.CurrentScopeHelper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Exposes functionality of Site Admin's grid
 */
@PageObject(css = "#cq-siteadmin-grid")
public class SiteAdminGrid {

  private static final Logger LOG = LoggerFactory.getLogger(SiteAdminGrid.class);

  private static final By LOADER_LOCATOR = By.cssSelector("div.ext-el-mask-msg.x-mask-loading");

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private WebDriver webDriver;

  @Inject
  private CurrentScopeHelper webElementHelper;

  @Inject
  @Named(ConfigKeys.WEBDRIVER_DEFAULT_TIMEOUT)
  private int defaultTimeout;

  @FindPageObject
  private List<SiteAdminGridRow> gridRows;

  @FindPageObject
  private SiteAdminActionBar actionBar;

  @Global
  @FindPageObject
  private SiteAdminConfirmationWindow siteAdminConfirmationWindow;

  @Global
  @FindPageObject
  private ActivateReferencesWindow activateReferencesWindow;

  @FindBy(css = "div.x-grid3-scroller")
  private WebElement scrollPane;

  /**
   * Custom {@link org.openqa.selenium.support.ui.ExpectedCondition} that checks
   * {@link ActivationStatus} of page by provided {@link SiteAdminPage} and page title
   *
   * @param status expected status
   * @param page SiteadminPage to be checked
   * @param pageTitle page title to be checked
   * @return true if actual status matches the expected one
   */
  public ExpectedCondition<Boolean> pageActivationStatusIs(final ActivationStatus status,
      final SiteAdminPage page, final String pageTitle) {
    return driver -> {
      if (pageActivationStatusMatches(status, page, pageTitle)) {
        return Boolean.TRUE;
      }
      page.getGrid().getActionBar().clickOnButton(SiteAdminButtons.REFRESH);
      page.getGrid().waitForLoaderNotPresent();
      return pageActivationStatusMatches(status, page, pageTitle);
    };
  }

  /**
   * Custom {@link org.openqa.selenium.support.ui.ExpectedCondition} that checks {@link PageStatus}
   * of page by provided {@link SiteAdminPage} and page title
   *
   * @param status expected status
   * @param page SiteadminPage to be checked
   * @param pageTitle page title to be checked
   * @return true if actual status matches the expected one
   */
  public ExpectedCondition<Object> pageStatusIs(PageStatus status, SiteAdminPage page,
      String pageTitle) {
    return driver -> {
      if (pageStatusMatches(status, page, pageTitle)) {
        return Boolean.TRUE;
      }
      page.getGrid().getActionBar().clickOnButton(SiteAdminButtons.REFRESH);
      page.getGrid().waitForLoaderNotPresent();
      return pageStatusMatches(status, page, pageTitle);
    };
  }

  private boolean pageActivationStatusMatches(ActivationStatus status, SiteAdminPage page,
      String pageTitle) {
    return status == page.getGrid().selectPageByTitle(pageTitle).getActivationStatus();
  }

  private boolean pageStatusMatches(PageStatus status, SiteAdminPage page,
      String pageTitle) {
    bobcatWait.withTimeout(Timeouts.SMALL)
        .until(ignored -> page.getGrid().isPageOnTheList(pageTitle));
    SiteAdminGridRow row = page.getGrid().selectPageByTitle(pageTitle);
    return row.getPageStatusToolTip().contains(status.getStatusCss());
  }

  public ActivateReferencesWindow getActivateReferencesWindow() {
    return activateReferencesWindow;
  }

  /**
   * Selects a row by clicking on the one with matching title.
   *
   * @param title title of the page
   * @return SiteadminGridRow
   */
  public SiteAdminGridRow selectPageByTitle(String title) {
    final SiteAdminGridRow row = getRowByTitle(title);
    if (row != null) {
      selectRow(row);
    } else {
      String msg = String.format("Could not find a page titled %s", title);
      throw new IllegalArgumentException(msg);
    }
    return row;
  }

  /**
   * Opens the page with provided title by double-clicking on its row.
   *
   * @param title title of the page
   */
  public void openPageByTitle(String title) {
    SiteAdminGridRow row = getRowByTitle(title);
    if (row != null) {
      row.open();
    } else {
      String msg = String.format("Could not find a page titled %s", title);
      throw new IllegalArgumentException(msg);
    }
  }

  /**
   * Checks if a page with provided title is present in the grid.
   *
   * @param title title of the page
   * @return true if page is on the list
   */
  public boolean isPageOnTheList(String title) {
    boolean isOnTheList = getRowByTitle(title) != null;
    if (!isOnTheList) {
      LOG.debug("There were no page with title {} on the list", title);
    }
    return isOnTheList;
  }

  /**
   * Checks if a specified template with provided title is present in the grid.
   *
   * @param title title of the template
   * @param template name of the template
   * @return true if template is on the list
   */
  public boolean isTemplateOnList(String title, String template) {
    boolean isOnList;
    SiteAdminGridRow row = getRowByTitle(title);
    isOnList = row != null && template.equals(row.getTemplateName());
    if (!isOnList) {
      LOG.debug("there is no template {} with title {} on the list", template, title);
    }
    return isOnList;
  }

  /**
   * Activates the page by clicking 'Activate' button in the Action Bar. It checks if an Activate
   * References window pops up - in that case the window is confirmed. Waits for the 'Activated'
   * status.
   *
   * @param title title of the page to be activated
   * @return This SiteadminGrid
   */
  public SiteAdminGrid activatePage(String title) {
    selectPageByTitle(title);
    activateSelectedPage();
    return this;
  }

  /**
   * Deactivates the page by clicking 'Deactivate' button in the Action Bar confirming deactivation
   * on confirmation window.
   *
   * @param title title of the page to be activated
   * @return This SiteadminGrid
   */
  public SiteAdminGrid deactivatePage(String title) {
    selectPageByTitle(title);
    deactivateSelectedPage();
    return this;
  }

  /**
   * Waits for loader (mask that covers the grid after some actions) to disappear.
   *
   * @return This SiteadminGrid
   */
  public SiteAdminGrid waitForLoaderNotPresent() {
    bobcatWait.withTimeout(Timeouts.BIG).until(
        CommonExpectedConditions.elementNotPresentOrVisible(LOADER_LOCATOR));
    bobcatWait.withTimeout(Timeouts.SMALL)
        .until(ExpectedConditions.elementToBeClickable(scrollPane));
    return this;
  }

  /**
   * @return ActionBar
   */
  public SiteAdminActionBar getActionBar() {
    return actionBar;
  }

  /**
   * Getter for SiteAdminConfirmationWindow
   *
   * @return {@link SiteAdminConfirmationWindow}
   */
  public SiteAdminConfirmationWindow getSiteAdminConfirmationWindow() {
    return siteAdminConfirmationWindow;
  }

  /**
   * Iterates through list of rows and returns the last row with matching name or <code>null</code>
   * if not found
   *
   * @param name of the page
   * @return {@link SiteAdminGridRow} matching the specified name
   */
  public SiteAdminGridRow getRowByName(String name) {
    SiteAdminGridRow result = null;
    for (SiteAdminGridRow gridRow : gridRows) {
      if (name.equals(gridRow.getName())) {
        result = gridRow;
        break;
      }
    }
    if (result == null) {
      LOG.info("Could not find a page with name '{}'", name);
    }
    return result;
  }

  private boolean isLoaderPresent() {
    webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    boolean isPresent = false;
    try {
      isPresent = !webDriver.findElements(LOADER_LOCATOR).isEmpty() || !webDriver
          .findElement(LOADER_LOCATOR)
          .isDisplayed();
    } catch (StaleElementReferenceException | NoSuchElementException e) {
      LOG.debug("Loader is not available at the moment: {}", e);
    } finally {
      webDriver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
    }
    return isPresent;
  }

  private void selectRow(final SiteAdminGridRow row) {
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      row.select();
      return row.isSelected();
    }, 2);
  }

  private void activateSelectedPage() {
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      actionBar.clickOnButton(SiteAdminButtons.ACTIVATE);

      return isLoaderPresent() || webElementHelper.isCurrentScopeVisible(activateReferencesWindow);
    }, 5);
    if (webElementHelper.isCurrentScopeVisible(activateReferencesWindow)) {
      activateReferencesWindow.waitToBeDisplayed().confirm();
    }
    waitForLoaderNotPresent();
  }

  private void deactivateSelectedPage() {
    getActionBar().clickOnButton(SiteAdminButtons.DEACTIVATE, siteAdminConfirmationWindow);
    siteAdminConfirmationWindow.confirm();
    waitForLoaderNotPresent();
  }

  /**
   * Iterates through list of rows and returns the last row with matching title or <code>null</code>
   * if not found
   *
   * @param title title of the page
   * @return {@link SiteAdminGridRow} matching the specified title
   */
  private SiteAdminGridRow getRowByTitle(String title) {
    SiteAdminGridRow result = null;
    for (SiteAdminGridRow gridRow : gridRows) {
      if (title.equals(gridRow.getTitle())) {
        result = gridRow;
        break;
      }
    }
    if (result == null) {
      LOG.info("Could not find a page titled '{}'", title);
    }
    return result;
  }
}
