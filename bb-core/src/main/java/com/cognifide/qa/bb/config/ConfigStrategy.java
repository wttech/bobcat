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

/**
 * Classes implementing this interfaces determine how Bobcat configuration is being loaded.
 */
public interface ConfigStrategy {

  default Properties gatherProperties() {
    Properties properties = loadDefaultProperties();
    loadProperties(properties);
    overrideFromSystemProperties(properties);
    setSystemProperties(properties);
    return properties;
  }

  Properties loadDefaultProperties();

  void loadProperties(Properties properties);

  default void overrideFromSystemProperties(Properties properties) {
    System.getProperties().stringPropertyNames().stream().forEach(key -> {
      String systemProperty = System.getProperty(key);
      if (StringUtils.isNotBlank(systemProperty)) {
        properties.setProperty(key, systemProperty);
      }
    });
  }

  default void setSystemProperties(Properties properties) {
    properties.stringPropertyNames().stream()
        .filter(key -> StringUtils.isBlank(System.getProperty(key)))
        .filter(key -> key.startsWith("webdriver."))
        .forEach(key -> System.setProperty(key, properties.getProperty(key)));
  }
}
