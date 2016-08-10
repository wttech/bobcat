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
package com.cognifide.qa.bb.aem.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.expectedconditions.ContentFinderActions;
import com.cognifide.qa.bb.aem.ui.AemContentFinder.ResultsView;
import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.expectedconditions.CommonExpectedConditions;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentFrame;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Single tab in a content finder.
 */
@PageObject
@Frame("/")
public class AemContentFinderTab {

  private static final String TEXT_DISPLAYED_IF_NO_ITEMS_FOUND = "No content items to display";

  @Inject
  private WebDriver driver;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  @Named(ConfigKeys.WEBDRIVER_DEFAULT_TIMEOUT)
  private int defaultTimeout;

  @Inject
  @CurrentScope
  private WebElement element;

  @FindBy(css = ".x-form-text.x-form-field[name='query']")
  @Global
  private WebElement searchField;

  @FindBy(className = "cq-siteadmin-refresh")
  private WebElement refreshButton;

  @FindBy(className = "cq-cft-dataview-list")
  private List<WebElement> listViewButton;

  @FindBy(className = "cq-cft-dataview-mosaic")
  private List<WebElement> mosaicViewButton;

  @FindBy(className = "cq-cft-dataview")
  private WebElement resultsPanel;

  @CurrentFrame
  private FramePath framePath;

  @Inject
  private DragAndDropFactory factory;

  /**
   * @return True if the tab is active (displayed). False otherwise.
   */
  public boolean isActive() {
    return element.isDisplayed();
  }

  /**
   * See {@link AemContentFinder#getCurrentView}
   *
   * @return the current view on the tab
   */
  public ResultsView getCurrentView() {
    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    try {
      if (!listViewButton.isEmpty() && isSelected(listViewButton.get(0))) {
        return ResultsView.LIST;
      } else if (!mosaicViewButton.isEmpty() && isSelected(mosaicViewButton.get(0))) {
        return ResultsView.MOSAIC;
      }
    } finally {
      driver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
    }
    return ResultsView.DEFAULT;
  }

  /**
   * See {@link AemContentFinder#clickMosaicView}
   */
  public void clickMosaicView() {
    if (getCurrentView() == ResultsView.DEFAULT) {
      throw new IllegalStateException();
    }
    bobcatWait.withTimeout(Timeouts.MEDIUM)
        .until(ContentFinderActions.showContentFinderView(mosaicViewButton.get(0)));
  }

  /**
   * See {@link AemContentFinder#clickMosaicView}
   */
  public void clickListView() {
    if (getCurrentView() == ResultsView.DEFAULT) {
      throw new IllegalStateException();
    }
    bobcatWait.withTimeout(Timeouts.MEDIUM)
        .until(ContentFinderActions.showContentFinderView(listViewButton.get(0)));
  }

  /**
   * See {@link AemContentFinder#refreshResults}
   */
  public void refreshResults() {
    refreshButton.click();
  }

  /**
   * See {@link AemContentFinder#hasResults}
   *
   * @return true if there are some results in content finder tab
   */
  public boolean hasResults() {
    return !TEXT_DISPLAYED_IF_NO_ITEMS_FOUND.equals(resultsPanel.getText());
  }

  /**
   * See {@link AemContentFinder#getElementByIndex}
   *
   * @param index of an element (starts from 0)
   * @return draggable element for the item
   */
  public Draggable getElementByIndex(int index) {
    WebElement findElement = getImageWebElementByIndex(index);
    return factory.createDraggable(findElement, framePath);
  }

  /**
   * See {@link AemContentFinder#getElementByName}
   *
   * @param name of an element
   * @return draggable element for the item
   */
  public Draggable getElementByName(String name) {
    WebElement findElement = getImageWebElementByName(name);
    return factory.createDraggable(findElement, framePath);
  }

  /**
   * See {@link AemContentFinder#search}
   *
   * @param query search query
   */
  public void search(final String query) {
    searchField.clear();
    searchField.sendKeys(query);
    searchField.findElement(By.xpath("./..")).findElement(By.cssSelector("img")).click();
    waitForSearchToEnd();
  }

  /**
   * See {@link AemContentFinder#getResults}
   *
   * @return elements showed in the tab
   */
  public List<String> getResults() {
    List<WebElement> list = resultsPanel.findElements(By
        .xpath(".//div[contains(@class,'cq-cft-search-title')]"));
    List<String> results = new ArrayList<>(list.size());
    for (WebElement webElement : list) {
      results.add(webElement.getText());
    }
    return results;
  }

  public WebElement getImageWebElementByName(String name) {
    return resultsPanel.findElement(By.xpath(String.format(
        ".//div[contains(@class,'cq-cft-search-title') and text()=%s]/../..",
        XpathUtils.quote(name))));
  }

  public WebElement getImageWebElementByIndex(int index) {
    return resultsPanel.findElement(By.xpath(String.format(
        "(.//div[contains(@class,'cq-cft-search-title')])[%d]/../..", index + 1)));
  }

  /**
   * Waits until search query ends and returns results, i.e checks if loader is not present.
   */
  private void waitForSearchToEnd() {
    By loaderLocator =
        By.xpath(".//div[@class='loading-indicator' and text()='Loading content...']");
    bobcatWait.withTimeout(Timeouts.BIG).until(
        CommonExpectedConditions.scopedElementLocatedByNotPresent(resultsPanel, loaderLocator));
  }

  private boolean isSelected(WebElement button) {
    WebElement table = button.findElement(By.xpath("./ancestor::table[1]"));
    return table.getAttribute(HtmlTags.Attributes.CLASS).contains("x-btn-pressed");
  }
}
