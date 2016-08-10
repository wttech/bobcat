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
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.nested.ScopedElementLocatorFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * This is a source of values for PageObjects fields that are annotated with CurrentScope and have type
 * List&lt;WebElement&gt;.
 */
public class CurrentScopeListProvider implements Provider<List<WebElement>> {

  @Inject
  private ContextStack stack;

  /**
   * Provider method. Bobcat will automatically call this method to fetch WebElements that represent
   * current scope.
   *
   * @return Collection of CurrentScope WebElements.
   */
  @Override
  public List<WebElement> get() {
    return stack.isEmpty() ? Collections.emptyList() : getScopedList();
  }

  private List<WebElement> getScopedList() {
    ElementLocatorFactory factory = stack.peek().getElementLocatorFactory();
    return factory instanceof ScopedElementLocatorFactory ?
        retrieveListFromFactory(factory) :
        Collections.emptyList();
  }

  // Warnings suppressed here as Proxy.newProxyInstance method returns Object by design
  @SuppressWarnings("unchecked")
  private List<WebElement> retrieveListFromFactory(ElementLocatorFactory factory) {
    InvocationHandler handler = new LocatingElementListHandler(
        ((ScopedElementLocatorFactory) factory).getCurrentScope());
    return (List<WebElement>) Proxy
        .newProxyInstance(WebElement.class.getClassLoader(), new Class[] {List.class}, handler);
  }
}
