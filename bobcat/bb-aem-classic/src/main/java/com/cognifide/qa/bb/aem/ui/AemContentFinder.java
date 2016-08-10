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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.expectedconditions.ContentFinderActions;
import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentFrame;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.google.inject.Inject;

/**
 * Represents content finder in a AEM page in authoring mode.
 */
@PageObject
@Frame("/")
public class AemContentFinder {

  private static final String SHOWN_DURING_MOUSE_DRAG_DIV =
      ".//div[contains(@class,'x-splitbar-proxy x-splitbar-proxy-h') and contains(@style,'left:')]";

  @FindBy(css = "#cq-cf-tabpanel div.x-tab-panel-body > div")
  private List<AemContentFinderTab> tabs;

  @FindBy(css = "#cq-cf-tabpanel ul")
  private WebElement iconBar;

  @FindBy(css = "#cq-cf-west-xsplit div")
  private WebElement collapseButton;

  @FindBy(css = "#cq-cf-west-xcollapsed div")
  private WebElement expandButton;

  @FindBy(id = "cq-cf-west-xsplit")
  private WebElement resizerDiv;

  @Inject
  private DragAndDropFactory factory;

  @CurrentFrame
  private FramePath framePath;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Searches for draggable items. Must be invoked before getElementByName and getElementByIndex
   * methods. Query parameter is case sensitive.
   *
   * @param query the text that will be typed into search input field
   * @return This AemContentFinder instance.
   */
  public AemContentFinder search(String query) {
    getCurrentTab().search(query);
    return this;
  }

  /**
   * Resizes contentFinder. If widthInPixels is lower or equal 0 then collapse() method is invoked.
   * It may be useful to select tab on icon bar. In Selenium only visible elements are clickable, so
   * sometimes user has to resize contentFinder before clicking the tab.
   *
   * @param widthInPixels new width
   * @return This AemContentFinder instance.
   */
  public AemContentFinder resize(int widthInPixels) {
    if (widthInPixels <= 0) {
      collapse();
    } else {
      if (isCollapsed()) {
        expand();
      }
      Draggable dragable = factory.createDraggable(resizerDiv, framePath);
      dragable.dropByOffset(widthInPixels - getCurrentWidth(), 0);

      bobcatWait.withTimeout(Timeouts.MEDIUM).until(
          ExpectedConditions.invisibilityOfElementLocated(By.xpath(SHOWN_DURING_MOUSE_DRAG_DIV)));
    }
    return this;
  }

  /**
   * @return Current width of contentFinder, in pixels.
   */
  public int getCurrentWidth() {
    if (isCollapsed()) {
      return 0;
    } else {
      return stripNonDecimalCharacters(resizerDiv.getCssValue("left"));
    }
  }

  /**
   * @return True if contentFinder is collapsed. False otherwise.
   */
  public boolean isCollapsed() {
    return "hidden".equals(resizerDiv.getCssValue("visibility"));
  }

  /**
   * @return True if contentFinder is expanded. False otherwise.
   */
  public boolean isExpanded() {
    return !isCollapsed();
  }

  /**
   * Collapses contentFinder if it is not already collapsed.
   *
   * @return This AemContentFinder instance.
   */
  public AemContentFinder collapse() {
    if (isExpanded()) {
      bobcatWait.withTimeout(Timeouts.MEDIUM).until(ContentFinderActions.collapse());

      bobcatWait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.visibilityOf(expandButton));
    }
    return this;
  }

  /**
   * Expand contentFinder if it is not already expanded.
   *
   * @return This AemContentFinder instance.
   */
  public AemContentFinder expand() {
    if (isCollapsed()) {
      bobcatWait.withTimeout(Timeouts.MEDIUM).until(ContentFinderActions.expand());

      bobcatWait.withTimeout(Timeouts.MEDIUM)
          .until(ExpectedConditions.visibilityOf(collapseButton));
    }
    return this;
  }

  /**
   * Click the tab on icon bar. TabName parameter is case sensitive. Correct values usually start
   * from upper case letter e.g Images, Documents
   *
   * @param tabName case sensitive tab name
   * @return This AemContentFinder instance.
   */
  public AemContentFinder clickTab(String tabName) {
    WebElement findElement = iconBar.findElement(By.xpath(String.format("./li[contains(@id,'%s')]",
        tabName)));
    bobcatWait.withTimeout(Timeouts.MEDIUM)
        .until(ContentFinderActions.showContentFinderTab(findElement));
    return this;
  }

  /**
   * @return True if search method returned any results. False otherwise.
   */
  public boolean hasResults() {
    return getCurrentTab().hasResults();
  }

  /**
   * Get dragabbleElement by index. Indexing starts from 0, counting from the top of the page.
   * Mosaic view is not supported. Before returning the result, method automatically changes search
   * results view to list if needed.
   *
   * @param index of an element (starts from 0)
   * @return Found draggable element.
   */
  public Draggable getElementByIndex(int index) {
    AemContentFinderTab currentTab = getCurrentTab();
    changeToListViewIfNeeded(currentTab);
    return currentTab.getElementByIndex(index);
  }

  /**
   * Get dragabbleElement by name. Name parameter is case sensitive. Whitespaces are also taken into
   * account. Mosaic view is not supported. Before returning the result, method automatically
   * changes search results view to list if needed.
   *
   * @param name case sensitive name of element
   * @return Found draggable element.
   */
  public Draggable getElementByName(String name) {
    AemContentFinderTab currentTab = getCurrentTab();
    changeToListViewIfNeeded(currentTab);
    return currentTab.getElementByName(name);
  }

  public void changeToListViewIfNeeded(AemContentFinderTab currentTab) {
    if (getCurrentView() == ResultsView.MOSAIC) {
      currentTab.clickListView();
    }
  }

  /**
   * Changes how results are displayed. Performs click at the mosaic view button. At some tabs there
   * is no such button, in that case the IllegalStateException is thrown.
   *
   * @return This AemContentFinder instance.
   */
  public AemContentFinder clickMosaicView() {
    getCurrentTab().clickMosaicView();
    return this;
  }

  /**
   * Changes how results are displayed. Clicks the list view button. If the tab doesn't contain this
   * button, in that case IllegalStateException is thrown.
   *
   * @return This AemContentFinder instance.
   */
  public AemContentFinder clickListView() {
    getCurrentTab().clickListView();
    return this;
  }

  public AemContentFinderTab getCurrentTab() {
    if (isCollapsed()) {
      throw new IllegalStateException();
    }
    for (AemContentFinderTab tab : tabs) {
      if (tab.isActive()) {
        return tab;
      }
    }
    throw new IllegalStateException();
  }

  /**
   * Clicks the refresh results button.
   *
   * @return This AemContentFinder instance.
   */
  public AemContentFinder refreshResults() {
    getCurrentTab().refreshResults();
    return this;
  }

  /**
   * @return Name of the currently selected tab, e.g. Images, Documents
   */
  public String getCurrentTabName() {
    WebElement findElement = iconBar
        .findElement(By.xpath(".//li[contains(@class,'x-tab-strip-active')]"));
    String id = findElement.getAttribute("id");
    return StringUtils.substringAfterLast(id, "-");
  }

  /**
   * @return Current way of displaying search results. ResultsView.DEFAULT is returned if there is
   *         no options to chose from e.g Products tab.
   */
  public ResultsView getCurrentView() {
    return getCurrentTab().getCurrentView();
  }

  /**
   * @return Ordered search results, where the string value is the title of searched item. These
   *         values can be used to get Draggabble item by using {@link #getElementByName}.
   */
  public List<String> getResults() {
    AemContentFinderTab currentTab = getCurrentTab();
    changeToListViewIfNeeded(currentTab);
    return currentTab.getResults();
  }

  private static int stripNonDecimalCharacters(String cssValue) {
    return Integer.parseInt(cssValue.replaceAll("\\D", ""));
  }

  /**
   * This enum lists all available ways to display results in ContentFinder.
   */
  public enum ResultsView {
    MOSAIC, LIST, DEFAULT
  }

}
