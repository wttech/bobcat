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
package com.cognifide.qa.bb.aem.touch.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.Resources;

/**
 * This is utility class reading yaml configuration files.
 *
 * @deprecated since 1.4.0, use {@link com.cognifide.qa.bb.utils.YamlReader}; to be removed in 2.0.0
 */
@Deprecated
public final class YamlReader {
  private static final Logger LOG = LoggerFactory.getLogger(YamlReader.class);

  private static final String YAML = ".yaml";

  private YamlReader() {
    // utility class
  }

  /**
   * Reads yaml configuration file under given path and returns structure defined in type reference.
   *
   * @param path          path to yaml configuration file.
   * @param typeReference type reference which will be used to construct result.
   * @return type reference defined structure of read yaml configuration file.
   */
  public static <T> T read(String path, TypeReference typeReference) {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      URI uri = Resources.getResource(path + YAML).toURI();
      File file = Paths.get(uri).toFile();
      return mapper.readValue(file, typeReference);
    } catch (IOException | URISyntaxException e) {
      LOG.error("Could not read YAML file: {} {}", path, e);
      throw new IllegalStateException("YAML file could not be read");
    }
  }
}
