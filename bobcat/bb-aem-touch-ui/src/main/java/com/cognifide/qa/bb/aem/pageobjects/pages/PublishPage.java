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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Named;

@PageObject
public class PublishPage {

  private static final Logger LOG = LoggerFactory.getLogger(PublishPage.class);

  public static final By CONTENT_SELECTOR = By.cssSelector("div.content");

  public static final By HEADER_SELECTOR = By.cssSelector("div.header");

  private String path;

  @Inject
  private PageObjectInjector pageObjectInjector;

  @Inject
  private WebDriver webDriver;

  @Inject
  @Named("publish.url")
  private String domain;

  @AssistedInject
  public PublishPage(@Assisted String path) {
    this.path = path;
  }

  public void open() {
    webDriver.get(domain + path);
  }

  public String getPath() {
    return path;
  }

  public String getTitle() {
    return webDriver.getTitle();
  }

  //TODO to be refactored with general pages handling
  public <T> T fromContent(Class<T> component) {
    return getFromScope(component, CONTENT_SELECTOR);
  }

  public <T> T fromHeader(Class<T> component) {
    return getFromScope(component, HEADER_SELECTOR);
  }

  public <T> T fromPage(Class<T> component) {
    WebElement scope = null;
    try {
      String selector = (String) component.getField("CSS").get(null);
      scope = webDriver.findElement(By.cssSelector(selector));
    } catch (IllegalAccessException e) {
      LOG.info(
          "CSS not accessible, page object injected with default scope: " + component.getName());
    } catch (NoSuchFieldException e) {
      LOG.info("CSS field not present in the page object, page object injected with default scope: "
          + component.getName());
    }
    return scope == null ?
        pageObjectInjector.inject(component) :
        pageObjectInjector.inject(component, scope);
  }

  public String getContentText() {
    return webDriver.findElement(CONTENT_SELECTOR).getText();
  }

  private <T> T getFromScope(Class<T> component, By parentSelector) {
    WebElement scope;
    try {
      String selector = (String) component.getField("CSS").get(null);
      scope = webDriver.findElement(parentSelector).findElement(By.cssSelector(selector));
    } catch (IllegalAccessException e) {
      LOG.info("CSS was not accessible, injecting with default scope", e);
      scope = webDriver.findElement(parentSelector);
    } catch (NoSuchFieldException e) {
      LOG.info("CSS field was not present in the page object, injecting with default scope", e);
      scope = webDriver.findElement(parentSelector);
    }
    return scope == null ?
        pageObjectInjector.inject(component) :
        pageObjectInjector.inject(component, scope);
  }
}
