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
package com.cognifide.qa.bb.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.frame.FrameMap;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.scope.webelement.WebElementScopedLocatorFactory;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * This class extends Guice's Injector functionality by adding a bunch of "inject" methods, with various
 * parameters that describe context of the injection.
 */
public class PageObjectInjector {

  @Inject
  private Injector injector;

  @Inject
  private ContextStack stack;

  @Inject
  private WebDriver webDriver;

  @Inject
  private FrameMap frameMap;

  /**
   * Inject in default frame. Frame parameter is sometimes unnecessary, because it will be overridden by
   * Frame annotation on type clazz.
   *
   * @param clazz PageObject class.
   * @param <T>   type of PageObject class that will be returned.
   * @return instance of Injector.
   */
  public <T> T inject(Class<T> clazz) {
    return inject(clazz, new FramePath());
  }

  /**
   * Retrieve framePath from the parameter object and then call inject(Class&lt;T&gt; clazz, FramePath framePath)
   *
   * @param clazz  PageObject class.
   * @param object to retrieve framePath.
   * @param <T>    type of PageObject class that will be returned.
   * @return instance of Injector.
   */
  public <T> T inject(Class<T> clazz, Object object) {
    return inject(clazz, frameMap.get(object));
  }

  /**
   * Parse the path provided as a string and then call inject(Class&lt;T&gt; clazz, FramePath framePath).
   *
   * @param clazz     PageObject class.
   * @param framePath frame path to element represented by PageObject.
   * @param <T>       type of PageObject class that will be returned.
   * @return instance of Injector.
   */
  public <T> T inject(Class<T> clazz, String framePath) {
    return inject(clazz, FramePath.parsePath(framePath));
  }

  /**
   * This method creates the object of type clazz within context defined by the top web element and the
   * frame path provided as the parameter.
   *
   * @param clazz     PageObject class.
   * @param framePath instance of FramePath.
   * @param <T>       type of PageObject class that will be returned.
   * @return instance of Injector.
   */
  public <T> T inject(Class<T> clazz, FramePath framePath) {
    final ElementLocatorFactory elementLocatorFactory = new DefaultElementLocatorFactory(webDriver);
    stack.push(new PageObjectContext(elementLocatorFactory, framePath));
    try {
      return injector.getInstance(clazz);
    } finally {
      stack.pop();
    }
  }

  /**
   * Inject in default frame. Frame parameter is sometimes unnecessary, because it will be overridden by
   * Frame annotation on type clazz. Injection will be performed in context defined by the scope parameter.
   *
   * @param clazz PageObject class.
   * @param scope WebElement for scope definition.
   * @param <T>   type of PageObject class that will be returned.
   * @return instance of Injector.
   */
  public <T> T inject(Class<T> clazz, WebElement scope) {
    return inject(clazz, scope, new FramePath());
  }

  /**
   * Retrieve framePath from object and then call inject(Class&lt;T&gt; clazz, WebElement scope, FramePath
   * framePath).
   *
   * @param clazz  PageObject class.
   * @param scope  WebElement for scope definition.
   * @param object to retrieve framePath.
   * @param <T>    type of PageObject class that will be returned.
   * @return instance of Injector.
   */
  public <T> T inject(Class<T> clazz, WebElement scope, Object object) {
    return inject(clazz, scope, frameMap.get(object));
  }

  /**
   * First it parses framePath string parameter into FramePath and then performs injection in context
   * defined by the provided the scope WebElement and FramePath.
   *
   * @param clazz     PageObject class.
   * @param scope     WebElement for scope definition.
   * @param framePath frame path to element represented by PageObject.
   * @param <T>       type of PageObject class that will be returned.
   * @return instance of Injector.
   */
  public <T> T inject(Class<T> clazz, WebElement scope, String framePath) {
    return inject(clazz, scope, FramePath.parsePath(framePath));
  }

  /**
   * This method creates the object of type clazz within context defined by the "scope" WebElement parameter
   * and frame path provided as the parameter.
   *
   * @param clazz     PageObject class.
   * @param scope     WebElement for scope definition.
   * @param framePath instance of FramePath.
   * @param <T>       type of PageObject class that will be returned.
   * @return instance of Injector.
   */
  public <T> T inject(Class<T> clazz, WebElement scope, FramePath framePath) {
    final ElementLocatorFactory elementLocatorFactory =
        new WebElementScopedLocatorFactory(webDriver, scope);
    stack.push(new PageObjectContext(elementLocatorFactory, framePath));
    try {
      return injector.getInstance(clazz);
    } finally {
      stack.pop();
    }
  }

  public Injector getOriginalInjector() {
    return injector;
  }
}
