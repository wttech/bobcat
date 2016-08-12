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

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.ui.component.AemComponentHandler;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;

/**
 * Represents insert window associated with parsys.
 */
@PageObject
@Frame("$cq")
public class AemInsertWindow {

  private static final Logger LOG = LoggerFactory.getLogger(AemInsertWindow.class);

  private static final String TAB_FOLDED = "0px -240px";

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Inject
  private BobcatWait wait;

  @FindBy(xpath = ".//button[text()='OK']")
  private WebElement okButton;

  /**
   * Waits for the window to be displayed.
   *
   * @return This AemInsertWindow
   */
  public AemInsertWindow waitToBeDisplayed() {
    wait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.visibilityOf(currentScope));
    return this;
  }

  /**
   * Selects component in the window, clicks ok and waits until insert window disappears.
   *
   * @param componentClass component to be inserted
   * @return This AemInsertWindow
   */
  public AemInsertWindow insertComponent(Class<?> componentClass) {
    chooseComponent(componentClass, chooseTab(componentClass));
    clickOkAndWait();
    return this;
  }

  /**
   * Selects component in the window, clicks ok and waits until insert window disappears.
   *
   * @param componentGroup group of component
   * @param componentName  component name that should be inserted
   * @return This AemInsertWindow
   */
  public AemInsertWindow insertComponent(String componentGroup, String componentName) {
    chooseComponent(componentGroup, componentName, chooseTab(componentGroup));
    clickOkAndWait();
    return this;
  }

  private void clickOkAndWait() {
    okButton.click();

    wait.withTimeout(Timeouts.BIG).until(input -> {
      try {
        return !currentScope.isDisplayed();
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        LOG.debug("CurrentScope not found: ", e);
        return true;
      }
    }, 2);
  }

  private void chooseComponent(Class<?> componentClass, WebElement tab) {
    AemComponentHandler component = new AemComponentHandler(componentClass);
    chooseComponent(component.getComponentGroup(), component.getComponentName(), tab);
  }

  private WebElement chooseTab(Class<?> componentClass) {
    AemComponentHandler component = new AemComponentHandler(componentClass);
    return chooseTab(component.getComponentGroup());
  }

  private void chooseComponent(String componentGroup, String componentName, WebElement tab) {
    try {
      tab.findElement(
          By.xpath(String.format("./..//button[text()=%s]", XpathUtils.quote(componentName))))
          .click();
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("No such component " + componentName + " in group: "
          + componentGroup, e);
    }
  }

  private WebElement chooseTab(String componentGroup) {
    try {
      WebElement tab = currentScope.findElement(By.xpath(String.format(
          ".//*[contains(@class,'x-panel-header')][.//span[normalize-space(text())=%s]]",
          XpathUtils.quote(componentGroup))));
      if (TAB_FOLDED.equals(
          tab.findElement(By.cssSelector(".x-tool-toggle")).getCssValue("background-position"))) {
        tab.click();
      }
      return tab;
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("No such tab: " + componentGroup, e);
    }
  }
}
