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

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.cognifide.qa.bb.actions.ActionsProvider;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.javascriptexecutor.JavascriptExecutorProvider;
import com.cognifide.qa.bb.provider.selenium.webdriver.CapabilitiesProvider;
import com.cognifide.qa.bb.provider.selenium.webdriver.WebDriverProvider;
import com.cognifide.qa.bb.provider.selenium.webdriver.close.ClosingAwareWebDriver;
import com.cognifide.qa.bb.provider.selenium.webdriver.close.ClosingAwareWebDriverFactory;
import com.cognifide.qa.bb.provider.selenium.webdriver.close.ClosingAwareWebDriverWrapper;
import com.cognifide.qa.bb.provider.selenium.webdriver.close.WebDriverClosedListener;
import com.cognifide.qa.bb.proxy.ProxyCloser;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;

/**
 * This class contain all the core bindings related to WebDriver.
 */
public class WebdriverModule extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder.newSetBinder(binder(), WebDriverEventListener.class);

    Multibinder<WebDriverClosedListener> closedListeners = Multibinder
        .newSetBinder(binder(), WebDriverClosedListener.class);
    closedListeners.addBinding().to(FrameSwitcher.class);
    closedListeners.addBinding().to(ProxyCloser.class);

    install(new FactoryModuleBuilder()
        .implement(ClosingAwareWebDriver.class, ClosingAwareWebDriverWrapper.class)
        .build(ClosingAwareWebDriverFactory.class));

    bind(WebDriver.class).toProvider(WebDriverProvider.class);
    bind(Actions.class).toProvider(ActionsProvider.class);
    bind(Capabilities.class).toProvider(CapabilitiesProvider.class);
    bind(JavascriptExecutor.class).toProvider(JavascriptExecutorProvider.class);
  }
}
