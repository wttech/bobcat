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

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.cognifide.qa.bb.qualifier.Global;

/**
 * This locator:
 * <ol>
 * <li>takes search context,</li>
 * <li>narrows it using passed selector,</li>
 * <li>finds web elements using field annotations.</li>
 * </ol>
 */
public class SelectorScopedElementLocator implements ElementLocator {

  private final SearchContext searchContext;

  private final By selector;

  private final Field field;

  /**
   * Create a element locator scoped by given search context and selector.
   *
   * @param searchContext Base search context
   * @param selector Scope of the locator (it narrows the searchContext)
   * @param field Field to find in the scope.
   */
  public SelectorScopedElementLocator(SearchContext searchContext, By selector, Field field) {
    this.searchContext = searchContext;
    this.selector = selector;
    this.field = field;
  }

  @Override
  public WebElement findElement() {
    SearchContext context = getContext();
    return new DefaultElementLocator(context, field).findElement();
  }

  @Override
  public List<WebElement> findElements() {
    return selector.findElements(searchContext).stream()
        .flatMap(webElement -> new DefaultElementLocator(webElement, field).findElements().stream())
        .collect(toList());
  }

  private SearchContext getContext() {
    return field.isAnnotationPresent(Global.class) ? searchContext
        : selector.findElement(searchContext);
  }
}