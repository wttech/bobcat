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


import org.openqa.selenium.Capabilities;

/**
 * Describes a modifier for Capabilities instance - it will be triggered <b>before</b> WebDriver
 * creation.
 * Modification can be controlled via {@code shouldModify()} method. Order of the execution can be
 * controlled via {@code getOrder()} method.
 * Each modifier must be registered in a Guice module using Multibinder.
 */
public interface CapabilitiesModifier {

  /**
   * This method can be used to control if the modification should be triggered, e.g. when a
   * property is
   * set to some value.
   *
   * @return true if the modification should be triggered
   */
  boolean shouldModify();

  /**
   * Modifies given instance of Capabilities and passes it further (potentially for additional
   * modifications).
   *
   * @param capabilities instance of the Capabilities that will be modified
   * @return modified Capabilities instance
   */
  Capabilities modify(Capabilities capabilities);

  /**
   * Optional method, used to control order of modifications - modificators will be sorted based
   * on the
   * value returned by this method.
   *
   * @return order of the modification, default is set to 0
   */
  default int getOrder() {
    return 0;
  }
}
