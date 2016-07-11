/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * This is a utility class used by CoreModule to create property binding. Its methods should not be used
 * directly by Bobcat's users.
 */
public final class PropertyBinder {

  private static final Logger LOG = LoggerFactory.getLogger(PropertyBinder.class);

  private static final String[] PREFIXES = new String[] {"webdriver.", "phantomjs."};

  private PropertyBinder() {
  }

  /**
   * This method reads property files and creates following bindings:
   * <ul>
   * <li>a named binding for each property,
   * <li>a binding for Properties class; bound Property instance contains all the properties.
   * </ul>
   * Property file paths are retrieved from configuration.paths property. If configuration.paths is not set,
   * bindProperties will look in default location, i.e. "src/main/config". Property configuration.paths can
   * contain any number of paths. Paths in configuration.paths should be separated with semicolons.
   *
   * @param binder The Binder instance that will store the newly created property bindings.
   */
  public static void bindProperties(Binder binder) {
    try {
      String parents = System.getProperty(ConfigKeys.CONFIGURATION_PATHS, "src/main/config");
      String[] split = StringUtils.split(parents, ";");
      Properties properties = loadDefaultProperties();
      for (String name : split) {
        File configParent = new File(StringUtils.trim(name));
        loadProperties(configParent, properties);
      }
      overrideFromSystemProperties(properties);
      setSystemProperties(properties);
      overrideTimeouts(properties);
      Names.bindProperties(binder, properties);
      binder.bind(Properties.class).toInstance(properties);
    } catch (IOException e) {
      LOG.error("Can't bind properties", e);
    }
  }

  private static void overrideTimeouts(Properties properties) {
    int big = Integer.parseInt(properties.getProperty(ConfigKeys.TIMEOUTS_BIG));
    int medium = Integer.parseInt(properties.getProperty(ConfigKeys.TIMEOUTS_MEDIUM));
    int small = Integer.parseInt(properties.getProperty(ConfigKeys.TIMEOUTS_SMALL));
    int minimal = Integer.parseInt(properties.getProperty(ConfigKeys.TIMEOUTS_MINIMAL));
    new Timeouts(big, medium, small, minimal);
  }

  private static Properties loadDefaultProperties() throws IOException {
    Properties properties = new Properties();
    InputStream is = PropertyBinder.class.getClassLoader()
        .getResourceAsStream(ConfigKeys.DEFAULT_PROPERTIES_NAME);
    try {
      properties.load(is);
    } catch (IOException e) {
      LOG.error("Can't bind default properties", e);
    } finally {
      is.close();
    }
    return properties;
  }

  private static void loadProperties(File file, Properties properties) throws IOException {
    if (!file.exists()) {
      return;
    }
    if (file.isDirectory()) {
      for (File child : file.listFiles()) {
        loadProperties(child, properties);
      }
    } else {
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

      LOG.debug("loading properties from: {} (ie. {})", file, file.getAbsolutePath());
      try {
        properties.load(reader);
      } finally {
        reader.close();
      }
    }
  }

  private static void overrideFromSystemProperties(Properties properties) {
    for (String key : properties.stringPropertyNames()) {
      String systemProperty = System.getProperty(key);
      if (StringUtils.isNotBlank(systemProperty)) {
        properties.setProperty(key, systemProperty);
      }
    }
  }

  private static void setSystemProperties(Properties properties) {
    for (String key : properties.stringPropertyNames()) {
      String systemProperty = System.getProperty(key);
      if (StringUtils.isNotBlank(systemProperty)) {
        continue;
      }
      for (String prefix : PREFIXES) {
        if (key.startsWith(prefix)) {
          System.setProperty(key, properties.getProperty(key));
          break;
        }
      }
    }
  }

}
