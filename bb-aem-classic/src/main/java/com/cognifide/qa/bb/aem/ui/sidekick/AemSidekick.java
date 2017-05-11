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
package com.cognifide.qa.bb.aem.ui.sidekick;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.aem.ui.component.AemComponentHandler;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.expectedconditions.SidekickActions;
import com.cognifide.qa.bb.aem.ui.wcm.windows.ActivateReferencesWindow;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentFrame;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.CurrentScopeHelper;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;

/**
 * Represents the Sidekick.
 */
@PageObject
@Frame("/")
public class AemSidekick {

  public static final String COMPONENT_BY_GROUP_XPATH = "./..//button";

  private static final String X_PANEL_COLLAPSED = "x-panel-collapsed";

  private static final String X_PANEL_ANIMATED = "x-panel-animated";

  private static final String PAGE_TAB_BUTTON_XPATH = "//div[@id='cq-sk']//button[text()=%s]";

  private static final String PAGE_CUSTOM_TAB_BUTTON_XPATH =
      "//div[@id='cq-sk']//button[text()='%s']";

  private static final String ICON_BOTTOM_XPATH =
      "//td[@class='x-toolbar-cell']//button[@class=' x-btn-text cq-sidekick-%s']";

  private static final String SECTION_XPATH =
      "//div[@id='cq-sk']//span[contains(text(), '%s')]/../..";

  private static final String SECTION_TOGGLE_CSS = ".x-tool-toggle";

  private static final String FIELDSET_XPATH = "//div[@id='cq-sk']//span[text() = '%s']/../..";

  private static final String AEM_SIDEKICK_TAB_ICON_PARTIAL_CLASS = "cq-sidekick-tab-icon-";

  private static final String GRID_XPATH = "//span[text() = '%s']/../..//div[@class='x-grid3']";

  private static final String ITEM_DISABLED = "x-item-disabled";

  private static final String OPERATION_TABLE_PARENT_XPATH = "./../../../../..";

  @Inject
  private WebDriver driver;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private PageObjectInjector injector;

  @Inject
  private CurrentScopeHelper webElementHelper;

  @Inject
  private DragAndDropFactory dragAndDropFactory;

  @FindBy(xpath = "//div[@id='cq-sk']")
  private WebElement sidekick;

  @FindBy(xpath = "//div[@id='cq-sk']//div[contains(@class, 'x-tool x-tool-toggle')]")
  private WebElement toggleButton;

  @FindBy(css = "#cq-sk .cq-sidekick-buttons[class*=cq-cmpts-] .x-panel-header")
  private List<WebElement> componentGroups;

  @FindBy(css = ".cq-sk-launchesstatus")
  private WebElement launchStatus;

  @FindPageObject
  private ActivateReferencesWindow activateReferencesWindow;

  @CurrentFrame
  private FramePath framePath;

  /**
   * Finds the icon element associated with the selected tab and clicks it.
   *
   * @param tab sidekick tab name
   * @return This AemSidekick instance.
   */
  public AemSidekick clickTab(SidekickTab tab) {
    final By locator = By.className(AEM_SIDEKICK_TAB_ICON_PARTIAL_CLASS + tab.getTabName());
    final WebElement element = driver.findElement(locator);
    bobcatWait.withTimeout(Timeouts.SMALL).until(SidekickActions.showSidekickTab(element));
    return this;
  }

  /**
   * Finds operation by name
   *
   * @param optionName page operation name
   * @return Operation WebElement
   */
  public WebElement getOperation(PageOperation optionName) {
    final String xpath = String.format(PAGE_TAB_BUTTON_XPATH,
        XpathUtils.quote(optionName.getOptionName().split("#")[0]));
    return driver.findElement(By.xpath(xpath));
  }

  /**
   * Finds custom operation by name
   *
   * @param optionName page operation name
   * @return Operation WebElement
   */
  public WebElement getCustomOperation(String optionName) {
    final String xpath = String.format(PAGE_CUSTOM_TAB_BUTTON_XPATH, optionName);
    return driver.findElement(By.xpath(xpath));
  }

  /**
   * Finds the button associated with the selected custom page operation and clicks it.
   *
   * @param optionName page operation name
   * @return This AemSidekick instance.
   */
  public AemSidekick clickCustomOperation(String optionName) {
    getCustomOperation(optionName).click();
    return this;
  }

  /**
   * Checks if the button associated with the selected page operation is enabled
   *
   * @param optionName page operation name
   * @return if button is available
   */
  public boolean isPageOperationEnabled(PageOperation optionName){
    return isOperationEnabled(getOperation(optionName));
  }

  /**
   * Checks if the button associated with the selected custom page operation is enabled
   *
   * @param optionName page operation name
   * @return if button is available
   */
  public boolean isCustomOperationEnabled(String optionName){
    return isOperationEnabled(getCustomOperation(optionName));
  }

  /**
   * Finds the button associated with the selected page operation and clicks it.
   *
   * @param optionName page operation name
   * @return This AemSidekick instance.
   */
  public AemSidekick clickOperation(PageOperation optionName) {
    getOperation(optionName).click();
    return this;
  }

  /**
   * Finds the button associated with the selected view mode and clicks it.
   *
   * @param iconName sidekick view modes
   * @return This AemSidekick instance.
   */
  public AemSidekick clickModeIcon(ModeIcon iconName) {
    final String xpath = String.format(ICON_BOTTOM_XPATH, iconName.getIconName());
    final WebElement iconBottom = driver.findElement(By.xpath(xpath));
    iconBottom.click();

    return this;
  }

  /**
   * Clicks the toggle button.
   *
   * @return This AemSidekick instance.
   */
  public AemSidekick toggle() {
    final boolean originalToggleState = isCollapsed();
    bobcatWait.withTimeout(Timeouts.BIG).until(webDriver -> {
      toggleButton.click();
      return isCollapsed() == !originalToggleState;
    }, 5);
    return this;
  }

  /**
   * @return Returns list of group names.
   */
  public List<String> getComponentGroupNames() {
    return new ArrayList<>(getGroupsByNames().keySet());
  }

  /**
   * This method checks if specified component group is present in Sidekick
   *
   * @param groupName name of the group
   * @return true if component group is present in Sidekick
   */
  public boolean isComponentGroupPresent(String groupName) {
    return getComponentGroupNames().contains(groupName);
  }

  /**
   * This method checks if specified component is present selected group in Sidekick
   *
   * @param componentName component name
   * @param groupName     component group name
   * @return true if component is present in sidekick
   */
  public boolean isComponentPresent(String componentName, String groupName) {
    if (isComponentGroupPresent(groupName)) {
      if (!SidekickActions.isSectionExpanded(getSectionByName(groupName))) {
        clickComponentGroupToggle(groupName);
      }
      return getComponentNames(groupName).contains(componentName);
    }
    throw new IllegalArgumentException("Group name " + groupName + " does not exist");
  }

  /**
   * This method expands/collapses specified component group
   *
   * @param groupName name of the group
   * @return AemSidekick sidekick
   */
  public AemSidekick clickComponentGroupToggle(String groupName) {
    final Map<String, WebElement> groupByNames = getGroupsByNames();
    if (groupByNames.containsKey(groupName)) {
      groupByNames.get(groupName).findElement(By.cssSelector(SECTION_TOGGLE_CSS)).click();
    }
    return this;
  }

  /**
   * This method can be used to activate the page. You can write your own methods to the other
   * functions within your test based on this one. Clicking methods are the most important there.
   *
   * @return AemSidekick sidekick
   */
  public AemSidekick activatePage() {
    clickTab(SidekickTab.PAGE);
    clickOperation(PageOperation.ACTIVATE_PAGE);
    activateReferencesIfNeeded();
    return this;
  }

  /**
   * Get section WebElement by it's name
   *
   * @param sectionName name of section
   * @return WebElement section
   */
  public WebElement getSectionByName(String sectionName) {
    final String xpath = String.format(SECTION_XPATH, sectionName);
    return driver.findElement(By.xpath(xpath));
  }

  /**
   * Expands section if collapsed based on provided sidekick section
   *
   * @param sidekickSections sections of sidekick
   * @return This AemSidekick instance.
   */
  public AemSidekick expandSectionIfCollapsed(SidekickSection sidekickSections) {
    final WebElement section = getSectionByName(sidekickSections.getSectionName());
    bobcatWait.withTimeout(Timeouts.BIG)
        .until(SidekickActions.expandSection(section), Timeouts.SMALL);
    return this;
  }

  /**
   * Get fieldset WebElement by it's name
   *
   * @param fieldsetName name of field set
   * @return WebElement fieldset
   */
  public WebElement getFieldsetByName(String fieldsetName) {
    final String xpath = String.format(FIELDSET_XPATH, fieldsetName);
    return driver.findElement(By.xpath(xpath));
  }

  /**
   * Expands fieldset if collapsed based on fieldset name
   *
   * @param fieldsetName name of field set
   * @return This AemSidekick instance.
   */
  public AemSidekick expandFieldsetIfCollapsed(String fieldsetName) {
    final WebElement fieldset = getFieldsetByName(fieldsetName);
    bobcatWait.withTimeout(Timeouts.BIG)
        .until(SidekickActions.expandFieldset(fieldset), Timeouts.SMALL);
    return this;
  }

  /**
   * Waits for the launch status to be visible in sidekick
   *
   * @return launch status message
   */
  public String getLaunchStatusMessage() {
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.visibilityOf(launchStatus));
    return launchStatus.getText();
  }

  /**
   * Get grid based on provided sidekick section
   *
   * @param sidekickSections sections of sidekick
   * @return SidekickGrid
   */
  public SidekickGrid getGrid(SidekickSection sidekickSections) {
    return injector.inject(SidekickGrid.class,
        driver.findElement(By.xpath(String.format(GRID_XPATH, sidekickSections.getSectionName()))));
  }

  /**
   * Provides a draggable object that represents a component present in the sidekick.
   *
   * @param component the component
   * @return Draggable
   */
  public Draggable getDraggable(Class<?> component) {
    this.clickTab(SidekickTab.COMPONENTS);
    String cssSelector = new AemComponentHandler(component).getSidekickCssSelector();
    if (StringUtils.isEmpty(cssSelector)) {
      throw new IllegalArgumentException("Class " + component.getSimpleName()
          + " does not have sidekickCssSelector defined within @AemComponent annotation");
    }
    WebElement webElement = driver.findElement(By.cssSelector(cssSelector));
    return dragAndDropFactory.createDraggable(webElement, framePath);
  }

  public boolean isCollapsed() {
    bobcatWait.withTimeout(Timeouts.SMALL)
        .until(webDriver -> !sidekick.getAttribute(HtmlTags.Attributes.CLASS)
            .contains(X_PANEL_ANIMATED), 1);
    return sidekick.getAttribute(HtmlTags.Attributes.CLASS).contains(X_PANEL_COLLAPSED);
  }

  private Map<String, WebElement> getGroupsByNames() {
    final Map<String, WebElement> map = new LinkedHashMap<>();
    for (WebElement group : componentGroups) {
      String name = group.getText().trim();
      map.put(name, group);
    }
    return map;
  }

  private boolean isOperationEnabled(WebElement operation){
    WebElement operationTable = operation.findElement(By.xpath(OPERATION_TABLE_PARENT_XPATH));
    return !operationTable.getAttribute(HtmlTags.Attributes.CLASS).contains(ITEM_DISABLED);
  }

  private List<String> getComponentNames(String groupName) {
    final List<String> list = new ArrayList<>();
    for (WebElement element : getWebElementsByGroupName(groupName)) {
      list.add(element.getText());
    }
    return list;
  }

  private List<WebElement> getWebElementsByGroupName(String groupName) {
    return getGroupsByNames().get(groupName).findElements(By.xpath(COMPONENT_BY_GROUP_XPATH));
  }

  private void activateReferencesIfNeeded() {
    boolean visible = webElementHelper.isCurrentScopeVisible(activateReferencesWindow);
    if (visible) {
      activateReferencesWindow.confirm();
    }
  }
}
