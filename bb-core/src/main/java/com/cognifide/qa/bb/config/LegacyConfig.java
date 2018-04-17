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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;

/**
 * This is the old, legacy way of loading properties in Bobcat based on sets of .properties files.
 * This strategy is loaded by default at the moment, in 2.0 release, Bobcat will switch over to {@link YamlConfig}.
 */
public class LegacyConfig implements ConfigStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(LegacyConfig.class);
  public static final String DEFAULT_CONFIGURATION_PATH = "src/main/config";

  /**
   * Loads default configuration provided by Bobcat from {@value ConfigKeys#DEFAULT_PROPERTIES_NAME} file.
   *
   * @return default set of properties
   */
  @Override
  public Properties loadDefaultConfig() {
    Properties defaultProperties = new Properties();
    try (InputStream is = PropertyBinder.class.getClassLoader()
        .getResourceAsStream(ConfigKeys.DEFAULT_PROPERTIES_NAME)) {
      defaultProperties.load(is);
    } catch (IOException e) {
      LOG.error("Can't bind default properties", e);
    }
    return defaultProperties;
  }

  /**
   * Loads Bobcat user properties, defined in various {@code .properties files}.
   * <p>
   * The location of properties files can be controlled via {@link ConfigKeys#CONFIGURATION_PATHS} system property.
   * By default reads the files from {@value #DEFAULT_CONFIGURATION_PATH}.
   *
   * @return set of properties defined by Bobcat user
   */
  @Override
  public Properties loadConfig() {
    Properties properties = new Properties();
    String parents = System.getProperty(ConfigKeys.CONFIGURATION_PATHS, DEFAULT_CONFIGURATION_PATH);
    String[] split = StringUtils.split(parents, ";");
    Arrays.stream(split)
        .forEach(name -> {
              File configParent = new File(StringUtils.trim(name));
              loadPropertiesFromFile(configParent, properties);
            }
        );
    return properties;
  }

  private void loadPropertiesFromFile(File file, Properties properties) {
    if (!file.exists()) {
      LOG.warn("{} file doesn't exists.", file.getPath());
      return;
    }
    if (file.isDirectory()) {
      Arrays.stream(file.listFiles())
          .forEach(child -> loadPropertiesFromFile(child, properties));
    } else {
      LOG.debug("loading properties from: {} (ie. {})", file, file.getAbsolutePath());
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
        properties.load(reader);
      } catch (FileNotFoundException e) {
        LOG.error("File was not found: {}", file, e);
      } catch (IOException e) {
        LOG.error("Could not read file: {}", file, e);
      }
    }
  }
}
