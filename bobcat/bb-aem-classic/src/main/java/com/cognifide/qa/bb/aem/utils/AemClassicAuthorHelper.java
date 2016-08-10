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
package com.cognifide.qa.bb.aem.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.aem.ui.component.AemComponentHandler;
import com.cognifide.qa.bb.aem.ui.parsys.AemParsys;
import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.aem.Components;
import com.cognifide.qa.bb.qualifier.CurrentFrame;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This class provides methods for obtaining AemSections or components and parsyses scopes through String
 * parameters.
 */
@PageObject
@Frame("$cq")
public class AemClassicAuthorHelper {

  @Inject
  private WebDriver webDriver;

  @Inject
  private PageObjectInjector pageObjectInjector;

  @CurrentFrame
  private FramePath framePath;

  @Inject
  @Named(AemConfigKeys.PARSYS_LOCATOR_FORMAT)
  private String parsysLocatorFormat;

  @Inject
  @Named(AemConfigKeys.COMPONENT_LOCATOR_FORMAT)
  private String componentLocatorFormat;

  /**
   * Used to obtain component's scope (WebElement) by its name and the name of the parent parsys.
   *
   * @param parsysName parent parsys that component is located in
   * @param component  enum class implementing Components interface
   * @return WebElement of the given component
   */
  public WebElement getComponentScope(String parsysName, Components component) {
    return getNthComponentScope(parsysName, component, 0);
  }

  /**
   * Used to obtain n-th (index) component's scope (WebElement) by its name and the name of the parent parsys.
   *
   * @param parsysName parent parsys that component is located in
   * @param component  enum class implementing Components interface
   * @param index      zero-based index of the component inside the parsys
   * @return WebElement of the given component
   */
  public WebElement getNthComponentScope(String parsysName, Components component, int index) {
    String cssClassName = new AemComponentHandler(component.getComponentClass()).getCssClassName();
    By locator = By.cssSelector(String.format(componentLocatorFormat, cssClassName));
    return getParsysScope(parsysName).findElements(locator).get(index);
  }

  /**
   * Used to obtain component's scope (WebElement) by its name.
   *
   * @param component enum class implementing Components interface
   * @return WebElement of the given component
   */
  public WebElement getComponentScope(Components component) {
    String cssClassName = new AemComponentHandler(component.getComponentClass()).getCssClassName();
    By locator = By.cssSelector(String.format(componentLocatorFormat, cssClassName));
    return webDriver.findElement(locator);
  }

  /**
   * Used to obtain parsys' scope (WebElement) by its name.
   *
   * @param parsysName name of the parsys
   * @return WebElement of the given parsys
   */
  public WebElement getParsysScope(String parsysName) {
    String css = String.format(parsysLocatorFormat, parsysName);
    return webDriver.findElement(By.cssSelector(css));
  }

  /**
   * Returns AemParsys PageObject based on the provided name.
   *
   * @param parsysName name of the requested parsys
   * @return {@link AemParsys}
   */
  public AemParsys getParsys(String parsysName) {
    return pageObjectInjector.inject(AemParsys.class, getParsysScope(parsysName), framePath);
  }

  /**
   * Returns AemDialog PageObject related to the specified component.
   *
   * @param parsysName parent parsys of the component
   * @param component  enum class implementing Components interface
   * @return {@link AemDialog}
   */
  public AemDialog getDialog(String parsysName, Components component) {
    return getDialog(parsysName, component, 0);
  }

  /**
   * Returns AemDialog PageObject related to the n-th specified component.
   *
   * @param parsysName parent parsys of the component
   * @param component  enum class implementing Components interface
   * @param index      zero-based index of the component inside the parsys
   * @return {@link AemDialog}
   */
  public AemDialog getDialog(String parsysName, Components component, int index) {
    return pageObjectInjector
        .inject(AemDialog.class, getNthComponentScope(parsysName, component, index), framePath);
  }

  /**
   * Returns AemDialog PageObject related to the specified component.
   *
   * @param component enum class implementing Components interface
   * @return {@link AemDialog}
   */
  public AemDialog getDialog(Components component) {
    return pageObjectInjector.inject(AemDialog.class, getComponentScope(component), framePath);
  }
}
