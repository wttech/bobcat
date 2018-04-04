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
package com.cognifide.qa.bb.scope.current;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;

import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.ParentElementLocatorProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * This class is a provider of WebElements that represent current scope associated with your PageObject.
 */
public class CurrentWebElementProvider implements Provider<WebElement> {

  @Inject
  private ContextStack stack;

  @Inject
  private WebDriver webDriver;

  /**
   * Provider method. Returns top WebElement when context stack is empty. If the context stack contains
   * some element, provider will return the last one.
   */
  @Override
  public WebElement get() {
    return stack.isEmpty() ? getTopElement() : getScopedWebElement();
  }

  private WebElement getScopedWebElement() {
    ElementLocatorFactory factory = stack.peek().getElementLocatorFactory();
    return (factory instanceof ParentElementLocatorProvider) ?
        getWebElementFromFactory(factory) :
        getTopElement();
  }

  private WebElement getWebElementFromFactory(ElementLocatorFactory factory) {
    InvocationHandler handler = new LocatingElementHandler(
        ((ParentElementLocatorProvider) factory).getCurrentScope());
    return (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
        new Class[] {WebElement.class, WrapsElement.class, Locatable.class}, handler);
  }

  private WebElement getTopElement() {
    return webDriver.findElement(By.xpath(".//*"));
  }
}
