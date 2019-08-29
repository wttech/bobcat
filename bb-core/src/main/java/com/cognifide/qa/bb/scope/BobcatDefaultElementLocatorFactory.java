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
package com.cognifide.qa.bb.scope;

import com.cognifide.qa.bb.mapper.annotations.FieldAnnotationsProvider;
import com.google.inject.Injector;
import java.lang.reflect.Field;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class BobcatDefaultElementLocatorFactory implements ElementLocatorFactory {

  private final SearchContext searchContext;

  private final Injector injector;

  public BobcatDefaultElementLocatorFactory(SearchContext searchContext, Injector injector) {
    this.searchContext = searchContext;
    this.injector = injector;
  }

  public ElementLocator createLocator(Field field) {
    return new DefaultElementLocator(searchContext,
        FieldAnnotationsProvider.create(field, injector));
  }
}
