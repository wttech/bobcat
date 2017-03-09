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
package com.cognifide.qa.bb.mapper.field;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.PageObjectInjector;

/**
 * This class is a Java proxy handler. It is created and registered in
 * PageObjectListProxyProvider. Its
 * responsibility is to provide up-to-date list of PageObjects identified by the locator.
 */
public class PageObjectListInvocationHandler implements InvocationHandler {

  private final Class<?> type;

  private final PageObjectInjector injector;

  private final ElementLocator locator;

  private final boolean cacheResults;

  private final FramePath framePath;

  private List<Object> list;

  /**
   * Constructs the PageObjectListInvocationHandler.
   *
   * @param genericType    Type of the element stored in the list.
   * @param elementLocator Locator that is used to fetch elements from the page.
   * @param injector       Injector that will create items from webelements identified by
   *                       elementLocator.
   * @param cacheResults   If this flag is set, PageObjectListInvocationHandler will generate the
   *                       list once.
   *                       If it's false, list will be generated each time this handler is invoked.
   * @param framePath      Injector performs injection in the frame indicated by framePath.
   */
  public PageObjectListInvocationHandler(Class<?> genericType, ElementLocator elementLocator,
      PageObjectInjector injector, boolean cacheResults, FramePath framePath) {
    this.type = genericType;
    this.injector = injector;
    this.locator = elementLocator;
    this.cacheResults = cacheResults;
    this.framePath = framePath;
  }

  /**
   * This method intercepts all methods on the proxied object. First it constructs the list of
   * PageObjects
   * (or reuses existing one if caching is allowed), then proceeds with the original method.
   *
   * @throws java.lang.reflect.InvocationTargetException if the underlying method throws an
   * exception.
   * @throws java.lang.IllegalAccessException            if this object is enforcing Java
   * language access control and the underlying method is inaccessible.
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException,
      IllegalAccessException {
    if (list == null || !cacheResults) {
      List<WebElement> webElements = locator.findElements();
      list = new ArrayList<>(webElements.size());
      for (WebElement item : webElements) {
        Object myItem = injector.inject(type, item, framePath);
        list.add(myItem);
      }
      list = Collections.unmodifiableList(list);
    }
    return method.invoke(list, args);
  }

}
