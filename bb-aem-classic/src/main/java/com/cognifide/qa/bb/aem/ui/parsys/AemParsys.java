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
package com.cognifide.qa.bb.aem.ui.parsys;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.exception.NoSuchComponentException;
import com.cognifide.qa.bb.aem.ui.component.AemComponentHandler;
import com.cognifide.qa.bb.aem.ui.menu.AemContextMenu;
import com.cognifide.qa.bb.aem.ui.menu.MenuOption;
import com.cognifide.qa.bb.aem.ui.window.ConfirmationWindow;
import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Droppable;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentFrame;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.CurrentScopeHelper;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Represents a parsys instance on the page.
 */
@PageObject
@Frame("$cq")
public class AemParsys {

  private static final Logger LOG = LoggerFactory.getLogger(AemParsys.class);

  private static final String SELECTOR_FOR_COMPONENT_IN_PARSYS = "div.section:not(.new)";

  @Inject
  private BobcatWait wait;

  @Inject
  private PageObjectInjector pageObjectInjector;

  @FindBy(
      xpath = "//div[contains(@class, 'cq-insertdialog') and contains(@style, 'visibility: "
          + "visible')]")
  @Global
  private AemInsertWindow insertWindow;

  @FindBy(xpath = "//div[contains(@class, 'x-menu') and contains(@style, 'visibility: visible')]")
  @Global
  private AemContextMenu contextMenu;

  @FindBy(css = ".cq-editrollover-insert-container")
  @Global
  private List<WebElement> clickableParsyses;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @CurrentFrame
  private FramePath currentFrame;

  @FindBy(css = "div.new")
  private WebElement insertComponentArea;

  @Inject
  @Named(AemConfigKeys.COMPONENT_LOCATOR_FORMAT)
  private String componentLocatorFormat;

  @Inject
  private DragAndDropFactory factory;

  @Inject
  private CurrentScopeHelper webElementHelper;

  @Inject
  private Actions actions;

  @Inject
  private ConfirmationWindow confirmationWindow;

  @Inject
  @Named(AemConfigKeys.PARSYS_FINDER_OFFSET)
  private int parsysOffset;

  public void waitToBeReady() {
    wait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.visibilityOf(insertComponentArea));
  }

  /**
   * This should be overriden only once at the beginning. Method allows to control nested parsys.
   *
   * @param insertComponentArea selector to component area
   */
  public void setInsertComponentArea(By insertComponentArea) {
    this.insertComponentArea = currentScope.findElement(insertComponentArea);
  }

  /**
   * Opens insert window by double clicking the insert area.
   *
   * @return Insert window instance.
   */
  public AemInsertWindow openInsertWindow() {
    wait.withTimeout(Timeouts.BIG).until(webDriver -> {
      actions.doubleClick(getClickableParsys()).perform();
      return webElementHelper.isCurrentScopeVisible(insertWindow);
    }, 2);
    return insertWindow;
  }

  /**
   * Opens insert window by context clicking the insert area and picking New... option from
   * ContextMenu.
   *
   * @return Insert window instance.
   */
  public AemInsertWindow openInsertWindowByContextMenu() {
    wait.withTimeout(Timeouts.BIG).until(webDriver -> {
      contextMenu.open(getClickableParsys());
      contextMenu.clickOption(MenuOption.NEW);
      return webElementHelper.isCurrentScopeVisible(insertWindow);
    }, 2);
    return insertWindow.waitToBeDisplayed();
  }

  /**
   * Pastes component by context clicking the insert area and picking Paste option from ContextMenu.
   */
  public void pasteComponentByContextMenu() {
    final int componentsCount = componentsCount();
    wait.withTimeout(Timeouts.BIG).until(webDriver -> {
      contextMenu.open(getClickableParsys());
      contextMenu.clickOption(MenuOption.PASTE);
      return componentsCount + 1 == componentsCount();
    }, 2);
  }

  /**
   * Opens context menu for the n-th component of type componentClass. Indexing starts at 0.
   *
   * @param componentClass component class
   * @param n              component index
   * @return opened contextMenu
   */
  public AemContextMenu openContextMenuNthComponent(Class<?> componentClass, int n) {
    contextMenu.open(getComponentScope(componentClass, n));
    return contextMenu;
  }

  /**
   * Opens context menu for the first component of type componentClass.
   *
   * @param componentClass component class
   * @return opened contextMenu
   */
  public AemContextMenu openContextMenuFirstComponent(Class<?> componentClass) {
    return openContextMenuNthComponent(componentClass, 0);
  }

  /**
   * Opens context menu for n-th component, no matter what type it is. Indexing starts at 0.
   *
   * @param n option index
   * @return opened contextMenu
   */
  public AemContextMenu openContextMenu(int n) {
    contextMenu
        .open(currentScope.findElements(By.cssSelector(SELECTOR_FOR_COMPONENT_IN_PARSYS)).get(n));
    return contextMenu;
  }

  /**
   * Inserts a component of the type indicated by the parameter.
   *
   * @param componentClass component class name
   * @param <T>            component class
   * @return Object of a component class injected with a proper scope
   */
  public <T> T insertComponent(Class<T> componentClass) {
    By componentLocator = getComponentLocator(componentClass);
    int componentTypeCount = currentScope.findElements(componentLocator).size();

    openInsertWindow().insertComponent(componentClass);
    wait.withTimeout(Timeouts.SMALL).until(driver -> currentScope
        .findElements(componentLocator).size() > componentTypeCount);

    return pageObjectInjector
        .inject(componentClass, getComponentScope(componentClass, componentTypeCount),
            currentFrame);
  }

  /**
   * Inserts a component of the type indicated by the parameter to empty parsys.
   *
   * @param componentClass component class name
   * @param <T>            component class
   * @return Object of a component class injected with a proper scope
   */
  public <T> T insertFirstComponentType(Class<T> componentClass) {
    By componentLocator = getComponentLocator(componentClass);
    openInsertWindow().insertComponent(componentClass);
    wait.withTimeout(Timeouts.SMALL).until(driver -> !currentScope
        .findElements(componentLocator).isEmpty());

    return pageObjectInjector
        .inject(componentClass, getComponentScope(componentClass, 0),
            currentFrame);
  }

  /**
   * Inserts a component identified by the group and name.
   *
   * @param componentGroup component group name
   * @param componentName  component name
   * @return This AemParsys.
   */
  public AemParsys insertComponent(String componentGroup, String componentName) {
    openInsertWindow().insertComponent(componentGroup, componentName);
    return this;
  }

  /**
   * Simply check if any component with a specific type is in parsys. Do not use it with negation,
   * because of performance issues. In such cases use {#link isComponentNotPresent}.
   *
   * @param componentClass component class
   * @return true if component is present
   */
  public boolean isComponentPresent(Class<?> componentClass) {
    final String cssClassName = new AemComponentHandler(componentClass).getCssClassName();
    return isComponentPresent(cssClassName);
  }

  /**
   * Simply check if any component with a specific css class name is in parsys. Do not use it with
   * negation, because of performance issues. In such cases use {#link isComponentNotPresent}.
   *
   * @param cssClassName component css class
   * @return true if component is present
   */
  public boolean isComponentPresent(String cssClassName) {
    try {
      wait.withTimeout(Timeouts.SMALL).until(ExpectedConditions
          .visibilityOf(currentScope.findElement(getComponentLocator(cssClassName))));
      return true;
    } catch (NoSuchElementException e) {
      LOG.debug(String.format("component located by: %s is not present", cssClassName), e);
      return false;
    }
  }

  /**
   * Simply check if any component with a specific type is NOT in parsys. Do not use it with
   * negation, because of performance issues. In such cases use {#link isComponentPresent}.
   *
   * @param componentClass component class
   * @return true if component is NOT present
   */
  public boolean isComponentNotPresent(Class<?> componentClass) {
    final String cssClassName = new AemComponentHandler(componentClass).getCssClassName();
    return isComponentNotPresent(cssClassName);
  }

  /**
   * Simply check if any component with a specific css class name is NOT in parsys. Do not use it
   * with negation, because of performance issues. In such cases use {#link isComponentPresent}.
   *
   * @param cssClassName component css class
   * @return true if component is NOT present
   */
  public boolean isComponentNotPresent(final String cssClassName) {
    boolean missing = false;
    try {
      wait.withTimeout(Timeouts.SMALL)
          .until(driver -> currentScope.findElements(getComponentLocator(cssClassName)).isEmpty());
      missing = true;
    } catch (TimeoutException e) {
      LOG.debug(String.format("component located by: %s is not present", cssClassName), e);
    }
    return missing;
  }

  /**
   * Get the first element of type componentClass. This is alias for getNthComponentOfType(Class
   * componentClass, 0).
   *
   * @param componentClass component class name
   * @param <T>            component class
   * @return object of a componentClass injected with a proper scope
   */
  public <T> T getFirstComponentOfType(Class<T> componentClass) {
    return getNthComponentOfType(componentClass, 0);
  }

  /**
   * Get the n-th element of type componentClass. Indexing starts at 0.
   *
   * @param componentClass component class name
   * @param <T>            component class
   * @param n              component index
   * @return object of a componentClass injected with a proper scope
   */
  public <T> T getNthComponentOfType(Class<T> componentClass, int n) {
    return pageObjectInjector
        .inject(componentClass, getComponentScope(componentClass, n), currentFrame);
  }

  /**
   * Get the n-th component. Indexing starts at 0.
   *
   * @param componentClass component class name
   * @param <T>            component class
   * @param globalIndex    component global index
   * @return object of a componentClass injected with a proper scope
   */
  public <T> T getComponent(Class<T> componentClass, int globalIndex) {
    return pageObjectInjector.inject(componentClass,
        currentScope.findElements(By.cssSelector(SELECTOR_FOR_COMPONENT_IN_PARSYS))
            .get(globalIndex),
        currentFrame);
  }

  /**
   * Remove the n-th occurrence of componentClass. Indexing starts at 0.
   *
   * @param componentClass component class
   * @param n              component index
   * @return This AemParsys.
   */
  public AemParsys removeNthComponentOfType(Class<?> componentClass, int n) {
    WebElement webElement = getComponentScope(componentClass, n);
    removeComponentByContextMenu(webElement);
    waitForComponentToBeRemoved(webElement);
    return this;
  }

  /**
   * Remove the n-th occurrence of componentClass using custom clickable area. Indexing starts at 0.
   *
   * @param componentClass component class
   * @param n              component index
   * @return This AemParsys.
   */
  public AemParsys removeNthComponentOfType(Class<?> componentClass, int n, By clickableArea) {
    WebElement webElement = getComponentScope(componentClass, n).findElement(clickableArea);
    removeComponentByContextMenu(webElement);
    waitForComponentToBeRemoved(webElement);
    return this;
  }

  /**
   * Remove the first occurrence of componentClass.
   *
   * @param componentClass component class
   * @return This AemParsys.
   */
  public AemParsys removeFirstComponentOfType(Class<?> componentClass) {
    return removeNthComponentOfType(componentClass, 0);
  }

  /**
   * Remove the first occurrence of componentClass using custom clickable area.
   *
   * @param componentClass component class
   * @return This AemParsys.
   */
  public AemParsys removeFirstComponentOfType(Class<?> componentClass, By clickableArea) {
    return removeNthComponentOfType(componentClass, 0, clickableArea);
  }

  /**
   * Remove the n-th(index) component, not matter what type it is. Indexing starts at 0.
   *
   * @param index component index
   * @return This AemParsys.
   */
  public AemParsys removeComponent(int index) {
    try {
      WebElement webElement = currentScope
          .findElements(By.cssSelector(SELECTOR_FOR_COMPONENT_IN_PARSYS)).get(index);
      removeComponentByContextMenu(webElement);
      waitForComponentToBeRemoved(webElement);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new NoSuchComponentException(e);
    }
    return this;
  }

  /**
   * Remove all components in parsys.
   *
   * @return This AemParsys.
   */
  public AemParsys clear() {
    List<WebElement> list =
        currentScope.findElements(By.cssSelector(SELECTOR_FOR_COMPONENT_IN_PARSYS));
    list.forEach(this::removeComponentByContextMenu);
    return this;
  }

  /**
   * @return Number of components inserted into parsys.
   */
  public int componentsCount() {
    return currentScope.findElements(By.cssSelector(SELECTOR_FOR_COMPONENT_IN_PARSYS)).size();
  }

  /**
   * @return droppable area that accepts components dragged from the sidekick
   */
  public Droppable getDroppable() {
    return factory.createDroppable(getClickableParsys(), currentFrame);
  }

  private WebElement getComponentScope(Class<?> componentClass, int index) {
    final String componentCssClass = new AemComponentHandler(componentClass).getCssClassName();
    try {
      return currentScope.findElements(getComponentLocator(componentCssClass)).get(index);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new NoSuchComponentException(e);
    }
  }

  private <T> By getComponentLocator(Class<T> componentClass) {
    final String componentCssClass = new AemComponentHandler(componentClass).getCssClassName();
    return getComponentLocator(componentCssClass);
  }

  private By getComponentLocator(String componentCssClass) {
    return By.cssSelector(String.format(componentLocatorFormat, componentCssClass));
  }

  private void removeComponentByContextMenu(WebElement webElement) {
    contextMenu.open(webElement).clickOption(MenuOption.DELETE);
    confirmationWindow.acceptConfirmationWindow();
  }

  private WebElement getClickableParsys() {
    wait.withTimeout(Timeouts.SMALL)
        .until(webDriver -> getParsysStream().count() >= 1, Timeouts.MINIMAL);
    return getParsysStream()
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("Clickable parsys element not found."));
  }

  private Stream<WebElement> getParsysStream() {
    return clickableParsyses.stream().filter(parsysByLocation(insertComponentArea.getLocation()));
  }

  private Predicate<WebElement> parsysByLocation(final Point location) {
    return input -> {
      int xPositionDiff = Math.abs(location.getX() - input.getLocation().getX());
      int yPositionDiff = Math.abs(location.getY() - input.getLocation().getY());

      return xPositionDiff + yPositionDiff <= parsysOffset;
    };
  }

  private void waitForComponentToBeRemoved(WebElement webElement) {
    wait.withTimeout(Timeouts.SMALL).until(ExpectedConditions.stalenessOf(webElement));
  }

}
