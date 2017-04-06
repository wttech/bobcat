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
package com.cognifide.qa.bb.aem.touch.pageobjects.touchui;

import static com.cognifide.qa.bb.aem.touch.util.ContentHelper.JCR_CONTENT;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import com.cognifide.qa.bb.aem.touch.data.componentconfigs.ComponentConfiguration;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.touch.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.touch.data.components.Components;
import com.cognifide.qa.bb.aem.touch.util.Conditions;
import com.cognifide.qa.bb.aem.touch.util.DataPathUtil;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Class represents web page parsys.
 */
@PageObject(css = ".cq-Overlay.cq-Overlay--component.cq-Overlay--container")
public class Parsys {

  private static final String IS_SELECTED = "is-selected";

  private static final String INSERT_BUTTON_SELECTOR = "button[data-action='INSERT']";

  @Inject
  private Conditions conditions;

  @Inject
  private Components components;

  @Inject
  private Actions actions;

  @FindBy(css = ".cq-Overlay--placeholder[data-text='Drag components here']")
  private WebElement dropArea;

  @FindPageObject
  private List<Component> componentList;

  @Global
  @FindPageObject
  private InsertComponentWindow insertComponentWindow;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Global
  @FindBy(css = INSERT_BUTTON_SELECTOR)
  private WebElement insertButton;

  @Inject
  private WebDriver driver;

  /**
   * @return data path of parsys
   */
  public String getDataPath() {
    String rawValue = currentScope.getAttribute(HtmlTags.Attributes.DATA_PATH);
    return StringUtils.substringAfter(rawValue, JCR_CONTENT);
  }

  /**
   * If possible, open insert component window and returns its instance.
   *
   * @return instance of {@link InsertComponentWindow}.
   */
  public InsertComponentWindow openInsertDialog() {
    tryToSelect();
    tryToOpenInsertWindow();
    return insertComponentWindow;
  }

  /**
   * Looks for components with given name and returns first found instance.
   *
   * @param componentName name of the component.
   * @return instance of {@link Component}.
   */
  public Component getComponent(String componentName) {
    String dataPath = components.getDataPath(componentName);
    String componentDataPath = DataPathUtil.normalize(dataPath);
    return componentList.stream() //
        .filter(containsDataPath(componentDataPath)) //
        .findFirst() //
        .orElseThrow(() -> new IllegalStateException("Component not present in the parsys"));
  }

  /**
   * Looks for components with given name and returns last found instance.
   *
   * @param componentName name of the component.
   * @return instance of {@link Component}.
   */
  public Component getLastComponent(String componentName) {
    String dataPath = components.getDataPath(componentName);
    String componentDataPath = DataPathUtil.normalize(dataPath);
    return componentList.stream() //
        .filter(containsDataPath(componentDataPath)) //
        .reduce((a, b) -> b)
        .orElseThrow(() -> new IllegalStateException("Component not present in the parsys"));
  }

  /**
   * Checks if any component with given name exists.
   *
   * @param componentName name of the component.
   * @return true if any component is present under given data path.
   */
  public boolean isComponentPresent(String componentName) {
    String dataPath = components.getDataPath(componentName);
    String componentDataPath = DataPathUtil.normalize(dataPath.toLowerCase());

    return componentList.stream() //
        .anyMatch(containsDataPath(componentDataPath));
  }

  /**
   * Opens insert dialog and inserts component with given name.
   *
   * @param componentName name of the component.
   */
  public void insertComponent(String componentName) {
    openInsertDialog().insertComponent(componentName);
  }

  /**
   * Configures component with given name with given map of fields cofig ({@link FieldConfig})
   *
   * @param componentName name of the component.
   * @param data map of configuration parameters for the component.
   */
  public void configureComponent(String componentName, ComponentConfiguration data) {
    getComponent(componentName).configure(data);
  }

  /**
   * Deletes first component found with given name.
   *
   * @param componentName name of the component to be deleted.
   */
  public void deleteComponent(String componentName) {
    getComponent(componentName).delete();
  }

  /**
   * Deletes last component found under given data path.
   *
   * @param componentName name of the component to be deleted.
   */
  public void deleteLastComponent(String componentName) {
    getLastComponent(componentName).delete();
  }

  /**
   * @return {@link WebElement} of the parsys.
   */
  public WebElement getParsys() {
    return currentScope;
  }

  /**
   * @return true if the parsys is not stale.
   */
  public boolean isNotStale() {
    return conditions.isConditionMet(not(stalenessOf(currentScope)));
  }

  private void tryToSelect() {
    conditions.verify(input -> {
      conditions.verify(visibilityOf(dropArea)).click();
      return dropArea.getAttribute(HtmlTags.Attributes.CLASS).contains(IS_SELECTED);
    }, Timeouts.MEDIUM);
  }

  /**
   * it may happen that the window pops up just a moment before {@code dropArea.click(} happens,
   * which results in WebdriverException: 'Other element would receive the click' - thus it is
   * catched and validated
   */
  private void tryToOpenInsertWindow() {
    conditions.verify(ignored -> {
      try {
        boolean isInsertButtonPresent = !driver
                .findElements(By.cssSelector(INSERT_BUTTON_SELECTOR))
                .isEmpty();
        if(!isInsertButtonPresent) {
          // AEM 6.1
          actions.doubleClick(dropArea).perform();
        } else {
          // AEM 6.2
          insertButton.click();
        }
      } catch (WebDriverException e) {
        return e.getMessage().contains("Other element would receive the click");
      }
      return insertComponentWindow.isDisplayedExpectingComponents();
    }, Timeouts.MEDIUM);
  }

  private Predicate<Component> containsDataPath(String componentDataPath) {
    return component -> StringUtils.contains(component.getDataPath(), componentDataPath);
  }
}