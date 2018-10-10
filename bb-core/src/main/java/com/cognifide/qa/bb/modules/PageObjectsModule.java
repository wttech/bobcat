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
package com.cognifide.qa.bb.modules;

import static com.google.inject.matcher.Matchers.any;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.mapper.PageObjectTypeListener;
import com.cognifide.qa.bb.mapper.field.CurrentFrameProvider;
import com.cognifide.qa.bb.mapper.field.FieldProvider;
import com.cognifide.qa.bb.mapper.field.PageObjectListProxyProvider;
import com.cognifide.qa.bb.mapper.field.PageObjectSelectorListProxyProvider;
import com.cognifide.qa.bb.mapper.field.ScopedPageObjectProvider;
import com.cognifide.qa.bb.mapper.field.SelectorPageObjectProvider;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.scope.current.CurrentScopeListProvider;
import com.cognifide.qa.bb.scope.current.CurrentWebElementProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

/**
 * This class contain all the bindings related to Page Objects handling.
 */
public class PageObjectsModule extends AbstractModule {

  @Override
  protected void configure() {

    bindListener(any(), new PageObjectTypeListener());

    Multibinder<FieldProvider> fieldProviders =
        Multibinder.newSetBinder(binder(), FieldProvider.class);
    fieldProviders.addBinding().to(ScopedPageObjectProvider.class);
    fieldProviders.addBinding().to(SelectorPageObjectProvider.class);
    fieldProviders.addBinding().to(PageObjectListProxyProvider.class);
    fieldProviders.addBinding().to(CurrentFrameProvider.class);
    fieldProviders.addBinding().to(PageObjectSelectorListProxyProvider.class);

    bind(Key.get(WebElement.class, CurrentScope.class)).toProvider(CurrentWebElementProvider.class);
    bind(new TypeLiteral<List<WebElement>>() {
    }).annotatedWith(CurrentScope.class).toProvider(CurrentScopeListProvider.class);
  }
}
