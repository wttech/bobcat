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
package com.cognifide.qa.bb.aem.touch.pageobjects.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;

import java.util.List;
import java.util.Objects;

import com.cognifide.qa.bb.qualifier.FindPageObject;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.touch.data.componentconfigs.ComponentConfigs;
import com.cognifide.qa.bb.aem.touch.data.componentconfigs.ComponentConfiguration;
import com.cognifide.qa.bb.aem.touch.data.components.Components;
import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.GlobalBar;
import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.Parsys;
import com.cognifide.qa.bb.aem.touch.util.Conditions;
import com.cognifide.qa.bb.aem.touch.util.DataPathUtil;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Named;

/**
 * Represents author page.
 */
@PageObject
public class AuthorPage {

  private static final Logger LOG = LoggerFactory.getLogger(AuthorPage.class);

  private static final String EDITOR_HTML = "/editor.html";

  private static final String IS_HIDDEN = "is-hidden";

  private static final String CONTENT_FRAME = "ContentFrame";

  private final String path;

  @Inject
  private WebDriver driver;

  @Inject
  private PageObjectInjector pageObjectInjector;

  @Inject
  private FrameSwitcher frameSwitcher;

  @Inject
  private Conditions conditions;

  @Inject
  private ComponentConfigs componentConfigs;

  @Inject
  private Components components;

  @Inject
  @Named("author.url")
  private String domain;

  @Inject
  private GlobalBar globalBar;

  @FindPageObject
  private List<Parsys> parsyses;

  @FindBy(css = "#OverlayWrapper")
  private WebElement authoringOverlay;

  /**
   * Constructs AuthorPage. Sets 'path' field value. Don't call it manually. Use the factory.
   *
   * @param path path of the page.
   */
  @AssistedInject
  public AuthorPage(@Assisted String path) {
    this.path = path;
  }

  /**
   * Opens page in web driver.
   */
  public void open() {
    driver.get(domain + EDITOR_HTML + path);
  }

  /**
   * @return true if page is loaded.
   */
  public boolean isLoaded() {
    boolean isLoaded = isLoadedCondition();
    if (!isLoaded) {
      retryLoad();
    }
    return isLoaded;
  }

  /**
   * Looks for parsys by data path. If parsys is not found then throws runtime exception
   * {@link IllegalStateException}.
   *
   * @param dataPath data path of parsys.
   * @return Parsys object.
   */
  public Parsys getParsys(String dataPath) {
    String componentDataPath = DataPathUtil.normalize(dataPath);
    return parsyses.stream() //
            .filter(parsys -> StringUtils.contains(parsys.getDataPath(), componentDataPath)) //
            .findFirst() //
            .orElseThrow(() -> new IllegalStateException("Parsys not found"));
  }

  /**
   * Looks for css class of given component class and return its content.
   *
   * @param component component class.
   * @return content of component.
   */
  public <T> T getContent(Class<T> component) {
    Objects.requireNonNull(component, "clazz property was not specified in YAML config");
    globalBar.switchToPreviewMode();
    frameSwitcher.switchTo(CONTENT_FRAME);
    WebElement scope = null;
    try {
      String selector = (String) component.getField("CSS").get(null);
      scope = driver.findElement(By.cssSelector(selector));
    } catch (IllegalAccessException e) {
      LOG.error("CSS was not accessible, injecting with default scope", e);
    } catch (NoSuchFieldException e) {
      LOG.error("CSS field was not present in the page object, injecting with default scope", e);
    }
    frameSwitcher.switchBack();
    return scope == null
            ? pageObjectInjector.inject(component, CONTENT_FRAME)
            : pageObjectInjector.inject(component, scope, CONTENT_FRAME);
  }

  /**
   * Adds component of given name to parsys of given name on the page. Verifies if parsys is rendered.
   *
   * @param parsys name of parsys on the page.
   * @param componentName name of component.
   */
  public void addComponent(String parsys, String componentName) {
    getParsys(parsys).insertComponent(componentName);
    verifyParsysRerendered(parsys);
  }

  /**
   * Configures component in parsys with specific configuration. Verifies if parsys is rendered.
   *
   * @param parsys parsys name.
   * @param componentName component name.
   * @param configName configuration name.
   * @return Map with component configuration.
   */
  public ComponentConfiguration configureComponent(String parsys, String componentName,
          String configName) {
    ComponentConfiguration data = componentConfigs.getConfigs(componentName).get(configName.toLowerCase());
    if (data == null) {
      throw new IllegalArgumentException("Config does not exist: " + configName);
    }
    getParsys(parsys).configureComponent(componentName, data);
    verifyParsysRerendered(parsys);
    return data;
  }

  /**
   * Deletes component from parsys. Verifies if parsys is rendered.
   *
   * @param parsys parsys name.
   * @param componentName component name.
   */
  public void deleteComponent(String parsys, String componentName) {
    globalBar.switchToEditMode();
    getParsys(parsys).deleteComponent(componentName);
    verifyParsysRerendered(parsys);
  }

  /**
   * Remove all components with name.
   *
   * @param parsys parsys name
   * @param componentName name of the component
   */
  public void clearParsys(String parsys, String componentName) {
    globalBar.switchToEditMode();
    while (getParsys(parsys).isComponentPresent(componentName)) {
      deleteComponent(parsys, componentName);
    }
  }

  private void verifyParsysRerendered(String parsys) {
    conditions.verifyPostAjax(object -> getParsys(parsys).isNotStale());
  }

  private void retryLoad() {
    conditions.verify(ignored -> {
        LOG.debug("Retrying page open");
        driver.navigate().refresh();
        return isLoadedCondition();
    }, Timeouts.MEDIUM);
  }

  private boolean isLoadedCondition() {
    return conditions.isConditionMet(
        not(ignored -> StringUtils
            .contains(authoringOverlay.getAttribute(HtmlTags.Attributes.CLASS), IS_HIDDEN)));
  }
}
