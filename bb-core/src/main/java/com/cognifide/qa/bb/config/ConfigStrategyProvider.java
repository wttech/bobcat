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
package com.cognifide.qa.bb.config;

import com.cognifide.qa.bb.constants.ConfigKeys;

/**
 * Provides an instance of {@link ConfigStrategy}.
 */
public final class ConfigStrategyProvider {

  public static final String YAML_SYS_PROP_VALUE = "yaml";

  private ConfigStrategyProvider() {
    //empty
  }

  /**
   * Returns a correct implementation of {@link ConfigStrategy}.
   * At the moment two types can be provided:
   * <ul>
   * <li>{@link LegacyConfig} is served by default</li>
   * <li>{@link YamlConfig} is served when {@code bobcat.config} system property is set to {@code yaml}</li>
   * </ul>
   *
   * @return proper implementation of {@link ConfigStrategy}
   */
  public static ConfigStrategy get() {
    return YAML_SYS_PROP_VALUE.equals(System.getProperty(ConfigKeys.CONFIG_STRATEGY)) ?
        new YamlConfig() :
        new LegacyConfig();
  }
}
