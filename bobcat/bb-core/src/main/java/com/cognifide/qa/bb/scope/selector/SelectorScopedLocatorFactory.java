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
package com.cognifide.qa.bb.scope.selector;

import java.lang.reflect.Field;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.scope.ParentElementLocatorProvider;

/**
 * Locator factory that scopes given searchContext using a {@link By} selector.
 */
public class SelectorScopedLocatorFactory
    implements ElementLocatorFactory, ParentElementLocatorProvider {

  private final SearchContext searchContext;

  private final By selector;

  /**
   * Creates a selector-scoped locator factory.
   *
   * @param searchContext Initial search context
   * @param selector      Scope of the created locator
   */
  public SelectorScopedLocatorFactory(SearchContext searchContext, By selector) {
    this.searchContext = searchContext;
    this.selector = selector;
  }

  /**
   * Creates a locator for the given field.
   */
  @Override
  public ElementLocator createLocator(Field field) {
    return new SelectorScopedElementLocator(searchContext, selector, field);
  }

  /**
   * Returns scope represented by this factory.
   * In this case, it is SelectorElementLocator constructed out of searchContext and selector.
   */
  @Override
  public ElementLocator getCurrentScope() {
    return new SelectorElementLocator(searchContext, selector);
  }
}
