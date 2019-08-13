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
package com.cognifide.qa.bb.provider.selenium.webdriver;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.provider.selenium.webdriver.close.ClosingAwareWebDriver;
import com.cognifide.qa.bb.provider.selenium.webdriver.close.ClosingAwareWebDriverFactory;
import com.cognifide.qa.bb.provider.selenium.webdriver.close.WebDriverClosedListener;
import com.cognifide.qa.bb.provider.selenium.webdriver.creators.WebDriverCreator;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.WebDriverModifiers;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * This is a provider that will produce WebDriver instances for all your PageObjects. It is
 * ThreadScoped, so each thread will receive its own instance of WebDriver. WebDriverProvider caches the
 * WebDriver, so all PageObjects in one thread will be using one instance of WebDriver.
 */
@ThreadScoped
public class WebDriverProvider implements Provider<WebDriver> {

  private ClosingAwareWebDriver cachedWebDriver;

  @Inject
  @Named(ConfigKeys.WEBDRIVER_TYPE)
  private String type;

  @Inject
  private ClosingAwareWebDriverFactory closingAwareWebDriverFactory;

  @Inject
  private Capabilities capabilities;

  @Inject
  private WebDriverRegistry registry;

  @Inject
  private WebDriverModifiers webDriverModifiers;

  @Inject
  private Set<WebDriverClosedListener> closedListeners;

  @Inject
  private Set<WebDriverEventListener> listeners;

  @Inject
  private Set<WebDriverCreator> webDriverCreators;

  /**
   * This is the provider method that produces WebDriver instance. It returns either a cached
   * webdriver or creates a new one.
   */
  @Override
  public WebDriver get() {
    if (cachedWebDriver == null || !cachedWebDriver.isAlive()) {
      cachedWebDriver = create();
    }
    return cachedWebDriver;
  }

  private ClosingAwareWebDriver create() {
    WebDriverCreator webDriverCreator = webDriverCreators.stream()
        .filter(creator -> StringUtils.equalsIgnoreCase(type, creator.getId()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            "No WebDriverCreator registered for the provided type: " + type));

    final WebDriver raw = webDriverCreator.create(capabilities);
    final WebDriver modified = webDriverModifiers.modifyWebDriver(raw);

    final ClosingAwareWebDriver closingAwareWebDriver =
        closingAwareWebDriverFactory.create(modified);
    closedListeners.forEach(closingAwareWebDriver::addListener);
    listeners.forEach(((EventFiringWebDriver) closingAwareWebDriver)::register);

    registry.add(closingAwareWebDriver);
    return closingAwareWebDriver;
  }
}
