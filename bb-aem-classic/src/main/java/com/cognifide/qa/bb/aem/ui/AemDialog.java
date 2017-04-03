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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.ui.menu.AemContextMenu;
import com.cognifide.qa.bb.aem.ui.menu.MenuOption;
import com.cognifide.qa.bb.aem.ui.window.ValidationWindow;
import com.cognifide.qa.bb.aem.utils.FieldValuesSetter;
import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.expectedconditions.CommonExpectedConditions;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.CurrentScopeHelper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This class represents dialog window. It has methods for opening and closing the dialog and access
 * its fields.
 */
@PageObject
@Frame("$cq")
public class AemDialog {

  /**
   * Xpath expression for selecting the dialog element from the page.
   */
  public static final String DIALOG_XPATH = "//div[contains(@id, 'ext-comp-')"
      + " and contains(@style, 'display: block') and contains(@style, 'visibility: visible')]";

  public static final String ID_CQ_GEN = "[id*=cq-gen]";

  private static final Logger LOG = LoggerFactory.getLogger(AemDialog.class);

  private static final String FOOTER_BUTTON_XPATH_FORMATTED =
      "//div[contains(@class, 'x-window-footer')]//button[text()='%s']";

  private static final String CONFIG_WINDOW_TITLE_XPATH =
      "//span[contains(@class, 'x-window-header-text')]";

  private static final String CANCEL_BUTTON_TEXT = "Cancel";

  private static final String OK_BUTTON_TEXT = "OK";

  private static final By DIALOG_XPATH_COMPILED = By.xpath(DIALOG_XPATH);

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Inject
  private WebDriver webDriver;

  @Inject
  private AemDialogFieldResolver fieldResolver;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private Actions actions;

  @Global
  @FindPageObject
  private ValidationWindow validationWindow;

  @Inject
  @Named(ConfigKeys.WEBDRIVER_DEFAULT_TIMEOUT)
  private int defaultTimeout;

  @Inject
  private AemContextMenu aemContextMenu;

  @Inject
  private CurrentScopeHelper webElementHelper;

  /**
   * Opens the dialog by double-clicking the component.
   *
   * @return Returns this dialog instance.
   */
  public AemDialog open() {
    open(currentScope);
    return this;
  }

  /**
   * Opens the dialog by double-clicking the component using clickable area.
   *
   * @param clickableArea area to click
   * @return Returns this dialog instance.
   */
  public AemDialog open(final By clickableArea) {
    open(currentScope.findElement(clickableArea));
    return this;
  }

  /**
   * Opens the dialog by edit option in context menu.
   *
   * @return Returns this dialog instance.
   */
  public AemDialog openByContextMenu() {
    openByContextMenu(currentScope);
    return this;
  }

  /**
   * Opens the dialog by edit option in context menu using clickable area.
   *
   * @param clickableArea area to click
   * @return Returns this dialog instance.
   */
  public AemDialog openByContextMenu(final By clickableArea) {
    openByContextMenu(currentScope.findElement(clickableArea));
    return this;
  }

  /**
   * Clicks OK button at the bottom of edit Window. Saves changes and closes the window (waits for
   * the dialog to disappear).
   *
   * @return Returns this dialog instance.
   */
  public AemDialog ok() {
    return clickDialogFooterButton(OK_BUTTON_TEXT);
  }

  /**
   * Finds the cancel button in the dialog and clicks it. All changes introduced in dialog's fields
   * are ignored.
   *
   * @return This dialog instance.
   */
  public AemDialog cancel() {
    return clickDialogFooterButton(CANCEL_BUTTON_TEXT);
  }

  /**
   * Clicks the specified tab in component's Edit Window.
   *
   * @param label Tab's label as seen by the user.
   * @return Returns this dialog instance.
   */
  public AemDialog clickTab(final String label) {
    bobcatWait.withTimeout(Timeouts.BIG).until((ExpectedCondition<Object>) input -> {
      getTabMap().get(label).click();
      return label.equals(getActiveTabName());
    }, 5);
    return this;
  }

  /**
   * @return Edit Window's title.
   */
  public String getTitle() {
    WebElement windowTitle =
        webDriver.findElement(By.xpath(DIALOG_XPATH + CONFIG_WINDOW_TITLE_XPATH));
    return windowTitle.getText();
  }

  /**
   * Clicks OK button at the bottom of edit Window. Assumes invalid data was entered and waits for
   * the validation message to appear.
   *
   * @return Returns instance of ValidationWindow.
   */
  public ValidationWindow okExpectingValidation() {
    final WebElement okButton = getFooterButtonWebElement(OK_BUTTON_TEXT);

    bobcatWait.withTimeout(Timeouts.BIG).until((ExpectedCondition<Object>) input -> {
      try {
        okButton.click();
        return webElementHelper.isCurrentScopeVisible(validationWindow);
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        LOG.debug("Dialog footer button is not available: {}", e);
        return Boolean.FALSE;
      }
    }, 2);
    return validationWindow;
  }

  /**
   * @return Label of the active tab.
   */
  public String getActiveTabName() {
    return getActiveTab().getText();
  }

  /**
   * @return true if Dialog is visible, false otherwise.
   */
  public boolean isVisible() {
    webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    boolean isDialogVisible = !webDriver.findElements(DIALOG_XPATH_COMPILED).isEmpty();
    webDriver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
    return isDialogVisible;
  }

  /**
   * Checks if the dialog is visible. If yes, closes it by clicking "cancel" button.
   *
   * @return This AemDialog instance.
   */
  public AemDialog closeIfVisible() {
    if (isVisible()) {
      cancel();
    }
    return this;
  }

  /**
   * Searches for the field of the given type, located on the given tab, identified by its index.
   *
   * @param tabName         Label of the tab where the field is located.
   * @param widgetIndex     Index of the widget within the given tab.
   * @param dialogFieldType Class that represents dialog's field.
   * @param <T>             dialog field class
   * @return A dialogFieldType's instance that represents dialog field.
   */
  public <T> T getField(String tabName, int widgetIndex, Class<T> dialogFieldType) {
    String widgetLabel = getWidgetLabelFromTab(tabName, widgetIndex);
    return fieldResolver.getField(widgetLabel, dialogFieldType);
  }

  /**
   * Searches for the field of the given type by its label, on the current tab.
   *
   * @param label           field label
   * @param dialogFieldType type of dialog field
   * @param <T>             dialog field class
   * @return A dialogFieldType's instance that represents dialog field.
   */
  public <T> T getFieldFromCurrentTab(String label, Class<T> dialogFieldType) {
    return fieldResolver.getFieldInActiveTab(label, dialogFieldType);
  }

  /**
   * Finds the dialog tab by its index and returns its name (label).
   *
   * @param index of the tab (starts from 0)
   * @return Tab's label.
   */
  public String getLabelFromTab(int index) {
    WebElement dialog = webDriver.findElement(DIALOG_XPATH_COMPILED);
    List<WebElement> items = dialog.findElements(By.className("x-tab-item"));
    WebElement widget = items.get(index);
    WebElement label = widget.findElement(By.xpath(".//label"));
    return label.getText();
  }

  /**
   * Clicks button at the bottom of edit Window and expect for dialog to disappear.
   *
   * @param buttonText button label
   * @return Returns this dialog instance.
   */
  public AemDialog clickDialogFooterButton(final String buttonText) {
    final WebElement footerButton = getFooterButtonWebElement(buttonText);

    bobcatWait.withTimeout(Timeouts.BIG).until((ExpectedCondition<Object>) input -> {
      try {
        footerButton.click();
        footerButton.isDisplayed();
        return Boolean.FALSE;
      } catch (NoSuchElementException | StaleElementReferenceException
          | ElementNotVisibleException e) {
        LOG.debug("Dialog footer button is not available: {}", e);
        return Boolean.TRUE;
      }
    }, 2);
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(CommonExpectedConditions.noAemAjax());
    return this;
  }

  /**
   * Finds a field by its label on the current tab and sets the value.
   *
   * @param value           field value
   * @param label           field label
   * @param dialogFieldType type of dialog field
   * @return This AemDialog instance.
   */
  public AemDialog setValueOfItemInCurrentTab(String value, String label,
      Class<?> dialogFieldType) {
    FieldValuesSetter.setFieldValue(getFieldFromCurrentTab(label, dialogFieldType), value);
    return this;

  }

  /**
   * Gets value from field in current tab with provided label and field type.
   *
   * @param label           field label
   * @param dialogFieldType type of dialog field
   * @return field value as String
   */
  public String getValueOfItemInCurrentTab(String label, Class<?> dialogFieldType) {
    return FieldValuesSetter.getFieldValue(getFieldFromCurrentTab(label, dialogFieldType));
  }

  /**
   * Fills field with data if (and only if) the field's initial value is null. In other words if the
   * field has already been initialized then it will not be altered.
   *
   * @return Returns a hashmap that maps labels to webelements representing tabs.
   */
  private Map<String, WebElement> getTabMap() {
    List<WebElement> tabs = getAllTabs();
    Map<String, WebElement> windowTabs = new HashMap<>(tabs.size());
    for (WebElement tab : tabs) {
      String tabLabel = tab.getText();
      windowTabs.put(tabLabel, tab);
    }
    return windowTabs;
  }

  private List<WebElement> getAllTabs() {
    bobcatWait.withTimeout(Timeouts.BIG).until(
        ExpectedConditions.visibilityOfElementLocated(DIALOG_XPATH_COMPILED));
    WebElement dialog = webDriver.findElement(DIALOG_XPATH_COMPILED);
    return dialog.findElements(By
        .cssSelector("div.x-tab-panel-header span.x-tab-strip-inner span.x-tab-strip-text"));
  }

  private WebElement getActiveTab() {
    List<WebElement> tabs = getAllTabs();

    for (WebElement tab : tabs) {
      WebElement tabState = tab.findElement(By.xpath(".//../../../.."));
      if (tabState.getAttribute(HtmlTags.Attributes.CLASS).contains("x-tab-strip-active")) {
        return tab;
      }
    }
    throw new IllegalStateException("There are no active tabs in the current dialog");
  }

  private void open(final WebElement element) {
    waitForComponentOnParsys();
    bobcatWait.withTimeout(Timeouts.BIG).until((ExpectedCondition<Object>) input -> {
      try {
        actions.doubleClick(element).perform();
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        LOG.debug("Dialog open element is not available", e);
      }
      return isVisible();
    }, 5);
  }

  private void openByContextMenu(final WebElement element) {
    waitForComponentOnParsys();
    bobcatWait.withTimeout(Timeouts.BIG).until((ExpectedCondition<Object>) input -> {
      try {
        aemContextMenu.open(element);
        aemContextMenu.clickOption(MenuOption.EDIT);
      } catch (NoSuchElementException | StaleElementReferenceException
          | ElementNotVisibleException e) {
        LOG.debug("Dialog open element is not available: {}", e);
      }
      return isVisible();
    }, 5);
  }

  private void waitForComponentOnParsys() {
    bobcatWait.withTimeout(Timeouts.BIG).until(
        CommonExpectedConditions.listSizeIsConstant(currentScope, By.cssSelector(ID_CQ_GEN)));
  }

  private String getWidgetLabelFromTab(String tabName, int widgetIndex) {
    clickTab(tabName);
    return getLabelFromTab(widgetIndex);
  }

  private WebElement getFooterButtonWebElement(String buttonText) {
    return webDriver.findElement(By.xpath(DIALOG_XPATH
        + String.format(FOOTER_BUTTON_XPATH_FORMATTED, buttonText)));
  }
}
