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
package com.cognifide.qa.bb.scope.nested;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * This is a two-step locator. First it constructs a scope out of the scope factory and the parent field, then
 * searches for the injected field in this scope.
 */
public class ScopedElementLocator implements ElementLocator {

  private final ElementLocatorFactory scopeFactory;

  private final Field scopeField;

  private final Field searchField;

  /**
   * Constructs ScopedElementLocator.
   *
   * @param scopeFactory instance of ElementLocatorFactory
   * @param scopeField field for scope definition
   * @param searchField class field
   */
  public ScopedElementLocator(ElementLocatorFactory scopeFactory,
      Field scopeField, Field searchField) {
    this.scopeFactory = scopeFactory;
    this.scopeField = scopeField;
    this.searchField = searchField;
  }

  /**
   * Constructs a scope out of the scope factory and the parent field, then searches for the injected field
   * in this scope.
   */
  @Override
  public WebElement findElement() {
    WebElement context = scopeFactory.createLocator(scopeField).findElement();
    return new DefaultElementLocator(context, searchField).findElement();
  }

  /**
   * Constructs a scope out of the scope factory and the parent field, then searches for the injected field
   * in this scope.
   */
  @Override
  public List<WebElement> findElements() {
    return scopeFactory.createLocator(scopeField).findElements().stream()
        .flatMap(element -> new DefaultElementLocator(element, searchField).findElements().stream())
        .collect(toList());
  }
}
