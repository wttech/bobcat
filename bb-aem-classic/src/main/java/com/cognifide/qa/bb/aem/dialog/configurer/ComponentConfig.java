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
package com.cognifide.qa.bb.aem.dialog.configurer;

import java.util.List;

/**
 * Represents component configuration:
 * <ul>
 * <li>name of the component</li>
 * <li>config name</li>
 * <li>actual configuration in form of {@code List<ConfigurationEntry>}</li>
 * </ul>
 */
public class ComponentConfig {

  private final String component;

  private final String name;

  private final List<ConfigurationEntry> config;

  public ComponentConfig(String component, String name, List<ConfigurationEntry> config) {
    this.component = component;
    this.name = name;
    this.config = config;
  }

  /**
   * Getter for component.
   *
   * @return component
   */
  public String getComponent() {
    return component;
  }

  /**
   * Getter for name.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Getter for config.
   *
   * @return config
   */
  public List<ConfigurationEntry> getConfig() {
    return config;
  }
}
