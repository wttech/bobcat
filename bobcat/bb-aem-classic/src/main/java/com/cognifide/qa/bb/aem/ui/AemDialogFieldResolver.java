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

import static com.cognifide.qa.bb.aem.ui.AemDialog.DIALOG_XPATH;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.scope.selector.SelectorScopedLocatorFactory;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Factory class that searches for the dialog field in the page and constructs a AemElement out of it.
 */
public class AemDialogFieldResolver implements FieldContainer {

  private static final String NO_LABEL = "no label";

  @Inject
  private WebDriver webDriver;

  @Inject
  private Injector injector;

  @Inject
  private ContextStack elementLocatorStack;

  /**
   * Searches for the field of a given type, identified by the selector class.
   *
   * @param selector        Locator by which to locate the dialog field.
   * @param dialogFieldType Class that represents dialog field.
   * @param <T> dialog field class
   * @return A dialogFieldType's instance that represents dialog field.
   */
  public <T> T getFieldBySelector(By selector, Class<T> dialogFieldType) {
    ElementLocatorFactory fieldScope = new SelectorScopedLocatorFactory(webDriver, selector);
    elementLocatorStack.push(new PageObjectContext(fieldScope, FramePath.parsePath("$cq")));
    try {
      return injector.getInstance(dialogFieldType);
    } finally {
      elementLocatorStack.pop();
    }
  }

  /**
   * Searches for the field of a given type, identified by its name.
   *
   * @param name            Name of the dialog field.
   * @param dialogFieldType Class that represents dialog field.
   * @param <T> dialog field class
   * @return A dialogFieldType's instance that represents dialog field.
   */
  public <T> T getFieldByName(String name, Class<T> dialogFieldType) {
    By selector = By.xpath(String.format(
        ".%s//*[@name='%s']/ancestor::*[contains(@class,'x-form-item')]", DIALOG_XPATH, name));
    return getFieldBySelector(selector, dialogFieldType);
  }

  /**
   * Searches for the field of a given type, identified by the xpath.
   *
   * @param xpath           Xpath expression by which to locate the dialog field.
   * @param dialogFieldType Class that represents dialog field.
   * @param <T> dialog field class
   * @return A dialogFieldType's instance that represents dialog field.
   */
  public <T> T getFieldByXpath(String xpath, Class<T> dialogFieldType) {
    By selector = By.xpath(String.format(".%s/%s", DIALOG_XPATH, xpath));
    return getFieldBySelector(selector, dialogFieldType);
  }

  /**
   * Searches for the field of a given type, with the specified label.
   *
   * @param label           Label of the dialog field.
   * @param dialogFieldType Class that represents dialog's field.
   * @param <T> dialog field class
   * @return A dialogFieldType's instance that represents dialog field.
   */
  @Override
  public <T> T getField(String label, Class<T> dialogFieldType) {
    String labelPart = NO_LABEL.equals(label) ?
        "label" : "label[text()=" + XpathUtils.quote(label) + "]";
    By selector = By.xpath(String
        .format(".%s//%s/ancestor::*[contains(@class,'x-form-item')]", DIALOG_XPATH, labelPart));
    return getFieldBySelector(selector, dialogFieldType);
  }

  public <T> T getFieldInActiveTab(String label, Class<T> dialogFieldType) {
    String activeDialogXpath = "div[contains(@class, 'x-panel x-panel-noborder') and not(contains"
        + "(@class, 'x-hide-display'))]";
    By selector;
    if (NO_LABEL.equals(label)) {
      selector = By.xpath(String.format(".%s//%s//label/ancestor::*[contains(@class,"
          + "'x-form-item')]", DIALOG_XPATH, activeDialogXpath));
    } else {
      selector = By.xpath(String.format(".%s//%s//label[text()=%s]/ancestor::*[contains(@class,"
          + "'x-form-item')]", DIALOG_XPATH, activeDialogXpath, XpathUtils.quote(label)));
    }
    return getFieldBySelector(selector, dialogFieldType);
  }

  /**
   * Searches for the field of a given type, identified by the css class.
   *
   * @param css             Css by which to locate the dialog field.
   * @param dialogFieldType Class that represents dialog field.
   * @param <T> dialog field class
   * @return A dialogFieldType's instance that represents dialog field.
   */
  public <T> T getFieldByCss(String css, Class<T> dialogFieldType) {
    By selector = By.cssSelector(css);
    return getFieldBySelector(selector, dialogFieldType);
  }
}
