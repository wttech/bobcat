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
package com.cognifide.qa.bb.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * This is a utility class for reading yaml configuration files.
 */
public final class YamlReader {
  public static final String YAML = ".yaml";
  private static final Logger LOG = LoggerFactory.getLogger(YamlReader.class);
  public static final String YAML_FILE_COULD_NOT_BE_READ = "YAML file could not be read";

  private YamlReader() {
    // utility class
  }

  /**
   * Reads yaml configuration file under given path and returns structure defined in {@link TypeReference}.
   *
   * @param path          path to yaml configuration file.
   * @param typeReference type reference which will be used to construct result.
   * @param <T>           type of the mapped object
   * @return YAML file mapped to an object defined by provided TypeReference
   */
  public static <T> T read(String path, TypeReference typeReference) {
    try {
      return new ObjectMapper(new YAMLFactory()).readValue(readFile(path), typeReference);
    } catch (IOException e) {
      LOG.error("Could not parse YAML file: {}", path, e);
      throw new IllegalStateException(YAML_FILE_COULD_NOT_BE_READ, e);
    }
  }

  /**
   * Reads yaml configuration file under given path and returns structure defined in {@link TypeReference}.
   *
   * @param path path to yaml configuration file.
   * @param type type which will be used to construct result.
   * @param <T>  type of the mapped object
   * @return YAML file mapped to an object defined by provided TypeReference
   */
  public static <T> T read(String path, Class<T> type) {
    try {
      return new ObjectMapper(new YAMLFactory()).readValue(readFile(path), type);
    } catch (IOException e) {
      LOG.error("Could not parse YAML file: {}", path, e);
      throw new IllegalStateException(YAML_FILE_COULD_NOT_BE_READ, e);
    }
  }

  private static InputStream readFile(String path) {
    String fullPath = StringUtils.endsWith(path, YAML) ? path : path + YAML;
    return YamlReader.class.getResourceAsStream(fullPath);
  }
}
