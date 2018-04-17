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

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.cognifide.qa.bb.constants.ConfigKeys;

/**
 * Classes implementing this interfaces determine how Bobcat configuration is being loaded.
 */
public interface ConfigStrategy {

  /**
   * Determines how all properties are being processed.
   * <p>
   * The default implementation uses the following order:
   * <ol>
   * <li>Load the default configuration included by Bobcat itself (via {@link #loadDefaultConfig()} method)</li>
   * <li>Load the config from Bobcat user files (via {@link #loadConfig()} method); override any previous
   * properties</li>
   * <li>Override the loaded properties from system properties provided from command line</li>
   * <li>Set system properties with loaded properties that start with {@value ConfigKeys#WEBDRIVER_PROP_PREFIX}</li>
   * </ol>
   *
   * @return all loaded and processed {@link Properties}
   */
  default Properties gatherProperties() {
    Properties config = loadDefaultConfig();
    config.putAll(loadConfig());
    Properties overridenConfig = overrideFromSystemProperties(config);
    setSystemProperties(overridenConfig);
    return overridenConfig;
  }

  /**
   * Determines how the default configuration included in Bobcat is loaded.
   *
   * @return the default set of Bobcat {@link Properties}
   */
  Properties loadDefaultConfig();

  /**
   * Determines how the Bobcat user configuration is loaded.
   *
   * @return the user set of Bobcat {@link Properties}
   */
  Properties loadConfig();

  /**
   * Determines how the loaded configuration is overriden with system properties, e.g. parameteres provided via
   * command-line.
   *
   * @param properties the {@link Properties} containing the default and user configuration
   * @return {@link Properties} with overrides from system properties
   */
  default Properties overrideFromSystemProperties(Properties properties) {
    System.getProperties().stringPropertyNames().stream().forEach(key -> {
      String systemProperty = System.getProperty(key);
      if (StringUtils.isNotBlank(systemProperty)) {
        properties.setProperty(key, systemProperty);
      }
    });
    return properties;
  }

  /**
   * Sets system properties for loaded properties that start with {@value ConfigKeys#WEBDRIVER_PROP_PREFIX}.
   *
   * @param properties loaded config
   */
  default void setSystemProperties(Properties properties) {
    properties.stringPropertyNames().stream()
        .filter(key -> StringUtils.isBlank(System.getProperty(key)))
        .filter(key -> key.startsWith(ConfigKeys.WEBDRIVER_PROP_PREFIX))
        .forEach(key -> System.setProperty(key, properties.getProperty(key)));
  }
}
