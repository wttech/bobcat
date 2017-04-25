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
package com.cognifide.qa.bb.scope.nestedselector;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.scope.ParentElementLocatorProvider;
import com.cognifide.qa.bb.scope.SearchContextAwareLocator;

/**
 * This is a two-step locator. First it constructs a scope out of the scope factory and the parent
 * field, then searches for the field with selector in this scope.
 */
public class NestedSelectorScopedElementLocator implements SearchContextAwareLocator {

  private final ElementLocatorFactory scopeFactory;
  private final SearchContext searchContext;
  private final By selector;
  private final boolean globalCurrentScope;

  /**
   * Constructs NestedSelectorScopedElementLocator.
   *
   * @param searchContext instance of SearchContext
   * @param scopeFactory instance of ElementLocatorFactory
   * @param selector selector
   * @param globalCurrentScope indicates if field is in global scope
   */
  public NestedSelectorScopedElementLocator(SearchContext searchContext,
      ElementLocatorFactory scopeFactory, By selector, boolean globalCurrentScope) {
    this.scopeFactory = scopeFactory;
    this.selector = selector;
    this.searchContext = searchContext;
    this.globalCurrentScope = globalCurrentScope;
  }

  /**
   * Constructs a scope out of the scope factory and the parent field, then searches for field with
   * selector in this scope.
   */
  @Override
  public WebElement findElement() {

    SearchContext context =
        scopeFactory instanceof ParentElementLocatorProvider && !globalCurrentScope
            ? ((ParentElementLocatorProvider) scopeFactory).getCurrentScope().findElement()
            : searchContext;
    return context.findElement(selector);
  }

  /**
   * Constructs a scope out of the scope factory and the parent field, then searches for the field
   * with selector in this scope.
   */
  @Override
  public List<WebElement> findElements() {
    if (scopeFactory instanceof ParentElementLocatorProvider && !globalCurrentScope) {
      return ((ParentElementLocatorProvider) scopeFactory).getCurrentScope().findElements().stream()
          .flatMap(element -> element.findElements(selector).stream())
          .collect(toList());
    }
    return searchContext.findElements(selector);
  }

  @Override
  public SearchContext getSearchContext() {
    return searchContext;
  }

}