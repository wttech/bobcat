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
package com.cognifide.qa.bb.aem.touch.modules;

import com.cognifide.qa.bb.aem.touch.data.components.Components;
import com.cognifide.qa.bb.aem.touch.data.components.ComponentsProvider;
import com.cognifide.qa.bb.aem.touch.data.pages.Pages;
import com.cognifide.qa.bb.aem.touch.data.pages.PagesProvider;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.PublishPageFactory;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * This module contains:
 * <ul>
 * <li> fields module,
 * <li> factory for author pages
 * <li> factory for publish pages
 * <li> binding {@link Components} to {@link ComponentsProvider}
 * <li> binding {@link Pages} to {@link PagesProvider}
 * </ul>
 */
public class AemTouchUiModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new FieldsModule());
    bind(Components.class).toProvider(ComponentsProvider.class);
    bind(Pages.class).toProvider(PagesProvider.class);

    install(new FactoryModuleBuilder().build(AuthorPageFactory.class));
    install(new FactoryModuleBuilder().build(PublishPageFactory.class));
  }

}
