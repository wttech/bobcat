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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.cognifide.qa.bb.config.yaml.Config;
import com.cognifide.qa.bb.utils.YamlReader;

/**
 * Provides configuration strategy that loads Bobcat properties from a YAML file.
 * The 'new' Bobcat configuration strategy, it replaces the {@link LegacyConfig}.
 * <p>
 * At the moment, to enable this config, Bobcat users needs to run tests with {@value com.cognifide.qa.bb.constants
 * .ConfigKeys#CONFIG_STRATEGY} system property set to {@code yaml}.
 * <p>
 * <p>
 * YAML configuration files should follow this format:
 * <pre>
 * {@code
 *
 * default:
 *   properties:
 *    # default properties
 *   contexts: [context1, context2} # list of context to be loaded by default
 * contexts:
 *   context1:
 *     prop1: value1
 *     prop2: value2
 *     # ...
 *   context2:
 *     propr1: value3
 *     # ...
 *   # ...
 * }</pre>
 *
 * @since 1.4.0
 */
public class YamlConfig implements ConfigStrategy {
  public static final String DEFAULT_CONFIG_NAME = "/default";
  public static final String USER_CONFIG_NAME = "/config";

  public static final String SYS_PROP_CONFIG_CONTEXTS = "bobcat.config.contexts";

  /**
   * Loads the default Bobcat configuration from {@code default.yaml} file.
   *
   * @return {@inheritDoc}
   */
  @Override
  public Properties loadDefaultConfig() {
    Properties defaultConfig = new Properties();
    defaultConfig.putAll(readDefaultYaml().getDefaultConfig().getProperties());
    return defaultConfig;
  }

  //not private for unit-testing purposes
  Config readDefaultYaml() {
    return YamlReader.read(DEFAULT_CONFIG_NAME, Config.class);
  }

  /**
   * Loads Bobcat user configuration from {@code config.yaml} file.
   *
   * @return {@inheritDoc}
   */
  @Override
  public Properties loadConfig() {
    Properties config = new Properties();
    Config rawConfig = readUserYaml();
    config.putAll(rawConfig.getDefaultConfig().getProperties());
    config.putAll(loadContexts(rawConfig));
    return config;
  }

  //not private for unit-testing purposes
  Config readUserYaml() {
    return YamlReader.read(USER_CONFIG_NAME, Config.class);
  }

  private Map<String, String> loadContexts(Config rawConfig) {
    Map<String, String> contexts = new HashMap<>();
    String contextsProperty = System.getProperty(SYS_PROP_CONFIG_CONTEXTS);
    List<String> selectedContexts = StringUtils.isNotBlank(contextsProperty) ?
        Arrays.asList(contextsProperty.split(",")) : rawConfig.getDefaultConfig().getContexts();

    selectedContexts.stream().forEach(context -> contexts
        .putAll(rawConfig.getContexts().getOrDefault(context, Collections.emptyMap())));
    return contexts;
  }
}
