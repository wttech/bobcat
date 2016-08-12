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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * This class parses component configurations from a JSON file
 * to {@link com.cognifide.qa.bb.aem.dialog.configurer.ComponentConfig}
 */
public class JsonToComponentConfig {

  private static final Logger LOG = LoggerFactory.getLogger(JsonToComponentConfig.class);

  private static final String PATH_TEMPLATE = "/component-configs/%s.json";

  /**
   * Reads config from a JSON file and returns a {@link com.cognifide.qa.bb.aem.dialog.configurer.ComponentConfig}.
   * Assumes that files are located under /component-configs directory.
   *
   * @param jsonName name of the JSON file
   * @return config represented by JSON
   */
  public ComponentConfig readConfig(String jsonName) {
    ComponentConfig config = null;
    try (Reader reader = new InputStreamReader(JsonToComponentConfig.class.getResourceAsStream(
        String.format(PATH_TEMPLATE, jsonName)), StandardCharsets.UTF_8)) {
      Gson gson = new Gson();
      config = gson.fromJson(reader, ComponentConfig.class);

    } catch (UnsupportedEncodingException e) {
      LOG.error("Unsupported encoding: ", e);
    } catch (IOException e) {
      LOG.error("Cannot read specified JSON file: ", e);
    }
    if (config == null) {
      throw new IllegalArgumentException("Could load provided JSON file as a ComponentConfig");
    }
    return config;
  }
}
