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

import java.lang.reflect.Field;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.scope.ParentElementLocatorProvider;
import com.cognifide.qa.bb.scope.selector.SelectorScopedElementLocator;

/**
 * Locator factory that scopes given searchContext using a {@link By} selector. Reduces search scope
 * by using parent scope
 */
public class NestedSelectorScopedLocatorFactory
    implements ElementLocatorFactory, ParentElementLocatorProvider {

  private final SearchContext searchContext;
  private final ElementLocatorFactory parentFactory;
  private final By selector;
  private final boolean globalCurrenScope;

  /**
   * Creates a selector-scoped locator factory with scope within parent scope.
   *
   * @param searchContext      Initial search context
   * @param selector           Scope of the created locator
   * @param parentFactory      Factory that represents scope for elements
   * @param globalCurrentScope indicates if field is in global scope
   */
  public NestedSelectorScopedLocatorFactory(SearchContext searchContext, By selector,
      ElementLocatorFactory parentFactory, boolean globalCurrentScope) {
    this.searchContext = searchContext;
    this.selector = selector;
    this.parentFactory = parentFactory;
    this.globalCurrenScope = globalCurrentScope;
  }

  /**
   * Creates a locator for the given field.
   */
  @Override
  public ElementLocator createLocator(Field field) {
    return field.isAnnotationPresent(Global.class) ? new DefaultElementLocator(searchContext, field)
        : new SelectorScopedElementLocator(searchContext, selector, field);
  }

  /**
   * Returns scope represented by this factory. In this case, it is
   * {@link NestedSelectorScopedElementLocator} constructed out of parent factory and parent field
   * and selector.
   */
  @Override
  public ElementLocator getCurrentScope() {

    return new NestedSelectorScopedElementLocator(searchContext, parentFactory, selector,
        globalCurrenScope);
  }
}
