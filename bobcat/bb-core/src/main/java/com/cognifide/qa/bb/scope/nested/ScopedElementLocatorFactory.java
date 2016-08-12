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

import java.lang.reflect.Field;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.scope.ParentElementLocatorProvider;

/**
 * Locator factory that produces locators within parent scope, or in global scope,
 * if the Global annotation is present.
 */
public class ScopedElementLocatorFactory
    implements ElementLocatorFactory, ParentElementLocatorProvider {

  private final ElementLocatorFactory parentFactory;

  private final Field parentField;

  private final WebDriver webDriver;

  /**
   * Creates an element-scoped locator factory.
   *
   * @param webDriver     WebDriver instance that will serve as a global scope
   *                      for Global-annotated fields
   * @param parentFactory Factory that represents scope for elements without Global annotation
   * @param parentField   Field that contains current field,
   *                      reducing the scope indicated by parentFactory
   */
  public ScopedElementLocatorFactory(WebDriver webDriver,
      ElementLocatorFactory parentFactory, Field parentField) {
    this.parentFactory = parentFactory;
    this.parentField = parentField;
    this.webDriver = webDriver;
  }

  @Override
  public ElementLocator getCurrentScope() {
    return parentFactory.createLocator(parentField);
  }

  @Override
  public ElementLocator createLocator(final Field field) {
    return field.isAnnotationPresent(Global.class) ?
        new DefaultElementLocator(webDriver, field) :
        new ScopedElementLocator(parentFactory, parentField, field);
  }
}
