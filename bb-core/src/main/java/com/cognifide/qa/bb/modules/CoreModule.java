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

import com.cognifide.qa.bb.cookies.CookiesModule;
import com.cognifide.qa.bb.guice.ThreadLocalScope;
import com.cognifide.qa.bb.guice.ThreadScoped;
import com.google.inject.AbstractModule;

/**
 * This class contain all the core bindings required for Bobcat to work. You need to install this module in
 * your own Guice module. After installation, following features will be available:
 * <ul>
 * <li>property bindings,
 * <li>system type,
 * <li>webDriver provider,
 * <li>browser capabilities provider,
 * <li>current scope,
 * <li>frame switcher,
 * <li>ThreadScoped annotation,
 * <li>WebDriverEventListener,
 * <li>jcr session provider.
 * </ul>
 */
public class CoreModule extends AbstractModule {

  @Override
  protected void configure() {

    bindScope(ThreadScoped.class, new ThreadLocalScope());

    install(new PropertyModule());

    install(new SeleniumModule());

    install(new PageObjectsModule());

    install(new WebdriverModule());

    install(new BobcatWebElementModule());

    install(new FrameModule());

    install(new ProxyModule());

    install(new DefaultModifiersModule());

    install(new LoadablesModule());

    install(new DragAndDropModule());

    install(new CookiesModule());

  }
}
