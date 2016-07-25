/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb.aem.pageobjects.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.cognifide.qa.bb.aem.data.componentconfigs.ComponentConfigs;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.data.components.Components;
import com.cognifide.qa.bb.aem.pageobjects.touchui.GlobalBar;
import com.cognifide.qa.bb.aem.pageobjects.touchui.Parsys;
import com.cognifide.qa.bb.aem.util.Conditions;
import com.cognifide.qa.bb.aem.util.DataPathUtil;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Named;

@PageObject
public class AuthorPage {

  private static final Logger LOG = LoggerFactory.getLogger(AuthorPage.class);

  private static final String EDITOR_HTML = "/editor.html";

  public static final String IS_HIDDEN = "is-hidden";

  public static final String CONTENT_FRAME = "ContentFrame";

  private String path;

  @Inject
  private WebDriver webDriver;

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

  @FindBy(css = Parsys.CSS)
  private List<Parsys> parsyses;

  @FindBy(css = "#OverlayWrapper")
  private WebElement authoringOverlay;

  @AssistedInject
  public AuthorPage(@Assisted String path) {
    this.path = path;
  }

  public void open() {
    webDriver.get(domain + EDITOR_HTML + path);
  }

  public boolean isLoaded() {
    return conditions.isConditionMet(
            not(ignored -> StringUtils.contains(authoringOverlay.getAttribute("class"), IS_HIDDEN)));
  }

  public Parsys getParsys(String dataPath) {
    String path = DataPathUtil.normalize(dataPath);
    return parsyses.stream() //
            .filter(parsys -> StringUtils.contains(parsys.getDataPath(), path)) //
            .findFirst() //
            .orElseThrow(() -> new IllegalStateException("Parsys not found"));
  }

  public <T> T getContent(Class<T> component) {
    Objects.requireNonNull(component, "clazz property was not specified in YAML config");
    globalBar.switchToPreviewMode();
    frameSwitcher.switchTo(CONTENT_FRAME);
    WebElement scope = null;
    try {
      String selector = (String) component.getField("CSS").get(null);
      scope = webDriver.findElement(By.cssSelector(selector));
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

  public WebElement addComponent(String parsys, String component) {
    WebElement result = getParsys(parsys).insertComponent(component);
    verifyParsysRerendered(parsys);
    return result;
  }

  public Map<String, List<FieldConfig>> configureComponent(String parsys, String component,
          String configName) {
    Map<String, List<FieldConfig>> data = componentConfigs.getConfigs(component).get(configName);
    if (data == null) {
      throw new IllegalArgumentException("Config does not exist: " + configName);
    }
    getParsys(parsys).configureComponent(components.getDataPath(component), data);
    verifyParsysRerendered(parsys);
    return data;
  }

  public void deleteComponent(String parsys, String component) {
    getParsys(parsys).deleteComponent(components.getDataPath(component));
    verifyParsysRerendered(parsys);
  }

  private void verifyParsysRerendered(String parsys) {
    conditions.verifyPostAjax(webDriver -> getParsys(parsys).isNotStale());
  }
}
