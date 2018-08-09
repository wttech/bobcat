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
package com.cognifide.qa.bb.aem.touch.siteadmin.aem62;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.cognifide.qa.bb.aem.touch.siteadmin.common.Loadable;
import com.cognifide.qa.bb.aem.touch.siteadmin.SiteadminActions;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.ActivationStatus;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.IsLoadedCondition;
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
public class SiteadminPage implements SiteadminActions, Loadable {

  private static final String SITEADMIN_PATH = "/sites.html";
  private static final String CHILD_PAGE_WINDOW_SELECTOR = ".cq-siteadmin-admin-childpages";

  @Inject
  @Named(AemConfigKeys.AUTHOR_URL)
  private String url;

  @Inject
  private Conditions conditions;

  @Inject
  private WebDriver driver;

  @Inject
  private WebElementUtils webElementUtils;

  @Global
  @FindBy(css = ".cq-siteadmin-admin-childpages")
  @LoadableComponent(condClass = IsLoadedCondition.class)
  private ChildPageWindow childPageWindow;

  @Inject
  private NavigatorDropdown navigatorDropdown;

  @FindBy(css = ".granite-collection-selectionbar .granite-selectionbar .coral-ActionBar-container")
  private SiteadminToolbar siteadminToolbar;

  @FindBy(css = "#granite-shell-actionbar")
  private ContentToolbar contentToolbar;

  @Inject
  private BobcatWait wait;

  @Inject
  private PageObjectInjector pageObjectInjector;

  @Override
  public SiteadminActions open(String nodePath) {
    driver.manage().addCookie(SiteadminLayout.LIST.getCookie62());
    driver.get(getSiteAdminUrl() + nodePath);
    return this;
  }

  private String getSiteAdminUrl() {
    return url + SITEADMIN_PATH;
  }

  @Override
  public SiteadminActions open() {
    driver.manage().addCookie(SiteadminLayout.LIST.getCookie());
    driver.get(getSiteAdminUrl());
    driver.navigate().refresh();
    return this;
  }

  @Override
  public SiteadminActions createNewPage(String title, String templateName) {
    contentToolbar.getCreateButton().click();
    contentToolbar.getCreatePageButton().click();
    contentToolbar.getCreatePageWizard()
        .selectTemplate(templateName)
        .provideTitle(title).submit();
    return this;
  }

  @Override
  public SiteadminActions createNewPage(String title, String name, String templateName) {
    contentToolbar.getCreateButton().click();
    contentToolbar.getCreatePageButton().click();
    contentToolbar.getCreatePageWizard()
        .selectTemplate(templateName)
        .provideName(name)
        .provideTitle(title).submit();
    return this;
  }

  @Override
  public SiteadminActions publishPage(String title) {
    childPageWindow.selectPage(title);
    siteadminToolbar.publishPageNow();
    waitForExpectedStatus(title, ActivationStatus.PUBLISHED);
    return this;
  }

  @Override
  public SiteadminActions unpublishPage(String title) {
    childPageWindow.selectPage(title);
    siteadminToolbar.unpublishPageNow();
    waitForExpectedStatus(title, ActivationStatus.NOT_PUBLISHED);
    return this;
  }

  @Override
  public SiteadminActions publishPageLater(String title, LocalDateTime scheduledDateTime) {
    childPageWindow.selectPage(title);
    siteadminToolbar.publishPageLater(scheduledDateTime);
    wait.withTimeout(Timeouts.SMALL).until(input -> isPagePresent(title));
    waitForExpectedStatus(title, ActivationStatus.SCHEDULED);
    return this;
  }

  @Override
  public SiteadminActions unpublishPageLater(String title, LocalDateTime scheduledDateTime) {
    childPageWindow.selectPage(title);
    siteadminToolbar.unpublishPageLater(scheduledDateTime);
    wait.withTimeout(Timeouts.SMALL).until(input -> isPagePresent(title));
    waitForExpectedStatus(title, ActivationStatus.SCHEDULED);
    return this;
  }

  @Override
  public SiteadminActions deletePage(String title) {
    childPageWindow.selectPage(title);
    int pageCount = childPageWindow.getPageCount();
    siteadminToolbar.deleteSelectedPages();
    waitForPageCount(pageCount - 1);
    return this;
  }

  @Override
  public SiteadminActions deleteSubPages() {
    if (childPageWindow.hasSubPages()) {
      childPageWindow.pressSelectAllPages();
      siteadminToolbar.deleteSelectedPages();
    }
    return this;
  }

  @Override
  public SiteadminActions copyPage(String title, String destination) {
    childPageWindow.selectPage(title);
    siteadminToolbar.copyPage();
    navigateInteractively(destination);
    int pageCount = childPageWindow.getPageCount();
    contentToolbar.pastePage();
    waitForPageCount(pageCount + 1);
    return this;
  }

  @Override
  public SiteadminActions movePage(String title, String destinationPath) {
    childPageWindow.selectPage(title);
    siteadminToolbar.movePage(destinationPath);
    return this;
  }

  @Override
  public boolean isPagePresent(String title) {
    return childPageWindow.containsPage(title);
  }

  @Override
  public boolean hasChildPages() {
    return childPageWindow.hasSubPages();
  }

  @Override
  public ChildPageRow getPageFromList(String title) {
    return childPageWindow.getChildPageRow(title);
  }

  public void refresh() {
    driver.navigate().refresh();
  }

  @Override
  public boolean isLoaded() {
    boolean isLoaded = isLoadedCondition();
    if (!isLoaded) {
      retryLoad();
    }
    return isLoaded;
  }

  @Override
  public SiteadminActions waitForPageCount(int pageCount) {
    boolean conditionNotMet = !webElementUtils.isConditionMet(webDriver -> {
      try {
        return pageCount == getChildPageWindow().getPageCount();
      } catch (StaleElementReferenceException e) {
        Objects.requireNonNull(webDriver).navigate().refresh();
        return false;
      }
    }, Timeouts.SMALL);
    if (conditionNotMet) {
      throw new IllegalStateException("Timeout when waiting for page count: " + pageCount);
    }
    return this;
  }

  private void waitForExpectedStatus(final String title, ActivationStatus status) {
    wait.withTimeout(Timeouts.MEDIUM).until(webDriver -> {
      Objects.requireNonNull(webDriver).navigate().refresh();
      ChildPageRow childPageRow = getChildPageWindow().getChildPageRow(title);
      PageActivationStatus pageActivationStatusCell = childPageRow.getPageActivationStatus();
      ActivationStatus activationStatus = pageActivationStatusCell.getActivationStatus();
      return activationStatus == status;
    }, Timeouts.MINIMAL);
  }

  private ChildPageWindow getChildPageWindow() {
    WebElement chldPageWindow = driver.findElement(By.cssSelector(CHILD_PAGE_WINDOW_SELECTOR));
    return pageObjectInjector.inject(ChildPageWindow.class, chldPageWindow);
  }

  private void retryLoad() {
    conditions.verify(webDriver -> {
      Objects.requireNonNull(webDriver).navigate().refresh();
      return isLoadedCondition();
    }, Timeouts.MEDIUM);
  }

  private boolean isLoadedCondition() {
    return conditions.isConditionMet(ignored -> childPageWindow.isLoaded());
  }

  private void navigateInteractively(String destination) {
    String currentUrl = StringUtils.substringAfter(driver.getCurrentUrl(), "sites.html");
    if (!currentUrl.endsWith(destination)) {
      boolean isGoBackNeeded = !destination.startsWith(currentUrl);
      if (isGoBackNeeded) {
        goBackUsingNavigator(destination, currentUrl);
      } else {
        goForwardToDestination(currentUrl, destination);
      }
      wait.withTimeout(Timeouts.SMALL).until((ExpectedCondition<Object>) input ->
          "0".equals(((JavascriptExecutor) driver).executeScript("return $.active").toString())
      );
      navigateInteractively(destination);
    }
  }

  private void goForwardToDestination(String currentUrl, String destination) {
    ChildPageRow closestPage =
        getChildPageWindow().getChildPageRows().stream()
            .filter(childPage -> childPage.getHref().equals(destination))
            .findFirst()
            .orElseGet(() ->
                getChildPageWindow().getChildPageRows().stream()
                    .collect(Collectors.toMap(Function.identity(),
                        childPageRow -> StringUtils.difference(currentUrl, childPageRow.getHref())
                    ))
                    .entrySet()
                    .stream()
                    .min(Comparator.comparingInt(a -> a.getValue().length()))
                    .orElseThrow(() -> new NoSuchElementException(
                        String.format(
                            "Failed to find a child page while trying to reach %s", destination)))
                    .getKey());
    closestPage.click();
  }

  private void goBackUsingNavigator(String destination, String currentUrl) {
    String closestPath =
        navigatorDropdown.getAvailablePaths().stream().distinct()
            .filter(path -> !currentUrl.equals(path))
            .collect(Collectors.toMap(
                Function.identity(), path -> StringUtils.difference(path, destination)
            ))
            .entrySet()
            .stream()
            .min(Comparator.comparingInt(a -> a.getValue().length()))
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Unable to find a path to destination \"%s\"", destination)))
            .getKey();
    navigatorDropdown.selectByPath(closestPath);
  }
}
