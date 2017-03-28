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
package com.cognifide.qa.bb.provider.selenium.webdriver.modifiers;


import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.capabilities.CapabilitiesModifier;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.collectors.WebDriverModifyingCollector;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.webdriver.WebDriverModifier;
import com.google.inject.Inject;

/**
 * Gathers all Capabilities and WebDriver modifiers, sorts them and filters enabled ones only.
 */
@ThreadScoped
public class WebDriverModifiers {

  private final List<CapabilitiesModifier> capabilitiesModifiers;

  private final List<WebDriverModifier> driverModifiers;

  @Inject
  public WebDriverModifiers(Set<CapabilitiesModifier> capabilitiesModifiers,
      Set<WebDriverModifier> driverModifiers) {
    this.capabilitiesModifiers = capabilitiesModifiers.stream() //
        .filter(CapabilitiesModifier::shouldModify) //
        .sorted(Comparator.comparing(CapabilitiesModifier::getOrder)) //
        .collect(Collectors.toList());
    this.driverModifiers = driverModifiers.stream() //
        .filter(WebDriverModifier::shouldModify) //
        .sorted(Comparator.comparing(WebDriverModifier::getOrder))//
        .collect(Collectors.toList());
  }

  /**
   * Modifies provided Capabilities with registered set of modifiers.
   *
   * @param capabilities raw Capabilities instance
   * @return modified Capabilities instance
   */
  public Capabilities modifyCapabilities(Capabilities capabilities) {
    Capabilities modifiedCapabilities = capabilities;
    for (CapabilitiesModifier modifier : capabilitiesModifiers) {
      modifiedCapabilities = modifier.modify(capabilities);
    }
    return modifiedCapabilities;
  }

  /**
   * Modifies provided WebDriver with registered set of modifiers.
   *
   * @param webDriver raw WebDriver instance
   * @return modified WebDriver instance
   */
  public WebDriver modifyWebDriver(WebDriver webDriver) {
    return driverModifiers.stream().collect(modifyDrivers(webDriver));
  }

  private Collector<? super WebDriverModifier, WebDriver, WebDriver> modifyDrivers(
      final WebDriver webdriver) {
    return new WebDriverModifyingCollector(webdriver);
  }

}
