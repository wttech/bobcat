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

public class LegacyConfig implements ConfigStrategy {
  private static final Logger LOG = LoggerFactory.getLogger(LegacyConfig.class);

  @Override
  public Properties loadDefaultProperties() {
    Properties defaultProperties = new Properties();
    try (InputStream is = PropertyBinder.class.getClassLoader()
        .getResourceAsStream(ConfigKeys.DEFAULT_PROPERTIES_NAME)) {
      defaultProperties.load(is);
    } catch (IOException e) {
      LOG.error("Can't bind default properties", e);
    }
    return defaultProperties;
  }

  @Override
  public void loadProperties(Properties properties) {
    String parents = System.getProperty(ConfigKeys.CONFIGURATION_PATHS, "src/main/config");
    String[] split = StringUtils.split(parents, ";");
    Arrays.stream(split)
        .forEach(name -> {
              File configParent = new File(StringUtils.trim(name));
              loadPropertyFromFile(configParent, properties);
            }
        );
  }

  private void loadPropertyFromFile(File file, Properties properties) {
    if (!file.exists()) {
      LOG.warn("{} file doesn't exists.", file.getPath());
      return;
    }
    if (file.isDirectory()) {
      Arrays.stream(file.listFiles())
          .forEach(child -> loadPropertyFromFile(child, properties));
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
