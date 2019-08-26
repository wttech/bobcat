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
package com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.capabilities;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.Capabilities;

import com.google.inject.Inject;

/**
 * Gathers all Capabilities modifiers, sorts them and filters enabled ones only.
 */
public class CapabilitiesModifiers {
  private final List<CapabilitiesModifier> modifiers;

  @Inject
  public CapabilitiesModifiers(Set<CapabilitiesModifier> modifiers) {
    this.modifiers = modifiers.stream() //
        .filter(CapabilitiesModifier::shouldModify) //
        .sorted(Comparator.comparing(CapabilitiesModifier::getOrder)) //
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
    for (CapabilitiesModifier modifier : modifiers) {
      modifiedCapabilities = modifier.modify(capabilities);
    }
    return modifiedCapabilities;
  }

}
