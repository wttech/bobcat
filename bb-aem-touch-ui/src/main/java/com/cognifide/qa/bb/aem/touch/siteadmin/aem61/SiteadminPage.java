/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.aem.touch.siteadmin.aem61;

import java.time.LocalDateTime;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.touch.siteadmin.SiteadminActions;
import com.cognifide.qa.bb.aem.touch.siteadmin.aem61.list.ChildPageRow;
import com.cognifide.qa.bb.aem.touch.siteadmin.aem61.list.ChildPageWindow;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.ActivationStatus;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.IsLoadedCondition;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.Loadable;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.PageActivationStatus;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.SiteadminLayout;
import com.cognifide.qa.bb.aem.touch.util.Conditions;
import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@PageObject
public class SiteadminPage implements Loadable, SiteadminActions {

  private static final String SITEADMIN_PATH = "/sites.html";
  private static final String CHILD_PAGE_WINDOW_SELECTOR = ".cq-siteadmin-admin-childpages";

  @Inject
  @Named(AemConfigKeys.AUTHOR_URL)
  String url;

  @Inject
  private WebElementUtils webElementUtils;

  @Inject
  private Conditions conditions;

  @Inject
  private WebDriver driver;

  @Inject
  private BobcatWait wait;

  @Inject
  private PageObjectInjector pageObjectInjector;

  @Global
  @FindBy(css = ".cq-siteadmin-admin-childpages")
  @LoadableComponent(condClass = IsLoadedCondition.class)
  private ChildPageWindow childPageWindow;

  @FindBy(
      css = "nav.foundation-layout-mode2-item.endor-ActionBar.js-granite-endor-ActionBar > div.endor-ActionBar-left")
  private SiteadminToolbar toolbar;

  @Override
  public boolean isLoaded() {
    boolean isLoaded = isLoadedCondition();
    if (!isLoaded) {
      retryLoad();
    }
    return isLoaded;
  }

  @Override
  public SiteadminActions publishPage(String title) {
    selectPage(title);
    toolbar.publishPage();
    wait.withTimeout(Timeouts.SMALL).until(input -> isLoaded(), Timeouts.MINIMAL);
    waitForExpectedStatus(title, ActivationStatus.PUBLISHED);
    return this;
  }

  @Override
  public SiteadminActions publishPageLater(String title, LocalDateTime localDateTime) {
    selectPage(title);
    toolbar.publishPageLater(localDateTime);
    driver.navigate().back();
    waitForExpectedStatus(title, ActivationStatus.SCHEDULED);
    return this;
  }

  @Override
  public SiteadminActions copyPage(String title, String destination) {
    selectPage(title);
    toolbar.copyPage();
    open(destination);
    int pageCount = getChildPageWindow().getPageCount();
    toolbar.pastePage();
    waitForPageCount(pageCount + 1);
    waitForPageToAppearOnTheList(title);
    return this;
  }

  @Override
  public SiteadminActions waitForPageCount(int pageCount) {
    boolean conditionNotMet = !webElementUtils.isConditionMet(webDriver -> {
      try {
        return pageCount == getChildPageWindow().getPageCount();
      } catch (StaleElementReferenceException e) {
        webDriver.navigate().refresh();
        return false;
      }
    }, Timeouts.SMALL);
    if (conditionNotMet) {
      throw new IllegalStateException("Timeout when waiting for page count: " + pageCount);
    }
    return this;
  }

  @Override
  public SiteadminActions createNewPage(String title, String name, String templateName) {
    toolbar.createPage(title, name, templateName);
    return this;
  }

  @Override
  public SiteadminActions createNewPage(String title, String templateName) {
    toolbar.createPage(title, templateName);
    return this;
  }

  @Override
  public SiteadminActions unpublishPage(String title) {
    selectPage(title);
    toolbar.unpublishPage();
    waitForExpectedStatus(title, ActivationStatus.NOT_PUBLISHED);
    return this;
  }

  @Override
  public SiteadminActions unpublishPageLater(String title, LocalDateTime localDateAndTime) {
    selectPage(title);
    toolbar.unpublishPageLater(localDateAndTime);
    wait.withTimeout(Timeouts.SMALL).until(input -> isLoaded());
    waitForExpectedStatus(title, ActivationStatus.SCHEDULED);
    return this;
  }

  @Override
  public SiteadminActions deletePage(String title) {
    selectPage(title);
    toolbar.deletePage();
    return this;
  }

  @Override
  public SiteadminActions deleteSubPages() {
    if (childPageWindow.hasSubPages()) {
      childPageWindow.pressSelectAllPages();
      toolbar.deletePage();
    }
    return this;
  }

  @Override
  public boolean isPagePresent(String title) {
    return childPageWindow.containsPage(title);
  }

  @Override
  public SiteadminActions movePage(String title, String destinationPath) {
    selectPage(title);
    toolbar.movePage(destinationPath);
    return this;
  }

  @Override
  public SiteadminActions open(String nodePath) {
    driver.manage().addCookie(SiteadminLayout.LIST.getCookie());
    driver.get(getSiteAdminUrl() + nodePath);
    wait.withTimeout(Timeouts.SMALL).until(input -> isLoaded(), Timeouts.MINIMAL);
    return this;
  }

  public void refresh() {
    driver.navigate().refresh();
  }

  @Override
  public SiteadminActions open() {
    driver.manage().addCookie(SiteadminLayout.LIST.getCookie());
    driver.get(getSiteAdminUrl());
    return this;
  }

  @Override
  public boolean hasChildPages() {
    return childPageWindow.hasSubPages();
  }

  @Override
  public ChildPageRow getPageFromList(String title) {
    return childPageWindow.getChildPageRow(title);
  }

  private void selectPage(String title) {
    childPageWindow.selectPage(title);
  }

  private String getSiteAdminUrl() {
    return url + SITEADMIN_PATH;
  }

  private void waitForExpectedStatus(final String title, ActivationStatus status) {
    wait.withTimeout(Timeouts.MEDIUM).until(webDriver -> {
      webDriver.navigate().refresh();
      ChildPageRow childPage = getChildPageWindow().getChildPageRow(title);
      PageActivationStatus pageActivationStatusCell = childPage.getPageActivationStatus();
      ActivationStatus activationStatus = pageActivationStatusCell.getActivationStatus();
      return activationStatus.equals(status);
    }, Timeouts.SMALL);
  }

  private void waitForPageToAppearOnTheList(final String title) {
    wait.withTimeout(Timeouts.MEDIUM).until(webDriver -> getChildPageWindow().containsPage(title),
        Timeouts.SMALL);
  }

  private ChildPageWindow getChildPageWindow() {
    WebElement childPagesElement =
        driver.findElement(By.cssSelector(CHILD_PAGE_WINDOW_SELECTOR));
    return pageObjectInjector.inject(ChildPageWindow.class, childPagesElement);
  }

  private void retryLoad() {
    conditions.verify(webDriver -> {
      if (!isLoadedCondition()) {
        webDriver.navigate().refresh();
      }
      return isLoadedCondition();
    }, Timeouts.MEDIUM);
  }

  private boolean isLoadedCondition() {
    return conditions.isConditionMet(ignored -> childPageWindow.isLoaded());
  }

}
