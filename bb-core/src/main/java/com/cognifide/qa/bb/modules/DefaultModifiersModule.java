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

import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.capabilities.CapabilitiesModifier;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.capabilities.EnableProxy;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.webdriver.ImplicitTimeoutModifier;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.webdriver.MaximizeModifier;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.webdriver.WebDriverModifier;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * This class contain all bindings for WebDriver and Capabilities modifiers.
 */
public class DefaultModifiersModule extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder<WebDriverModifier> webDriverModifiers = Multibinder
        .newSetBinder(binder(), WebDriverModifier.class);
    webDriverModifiers.addBinding().to(MaximizeModifier.class);
    webDriverModifiers.addBinding().to(ImplicitTimeoutModifier.class);

    Multibinder<CapabilitiesModifier> capabilitiesModifiers = Multibinder
        .newSetBinder(binder(), CapabilitiesModifier.class);
    capabilitiesModifiers.addBinding().to(EnableProxy.class);
  }
}
