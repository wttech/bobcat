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
package com.cognifide.qa.bb.aem.touch.data.componentconfigs;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.cognifide.qa.bb.aem.touch.util.YamlReader;

/**
 * This class reads component configuration from yaml configuration files
 */
public class ComponentConfigs {

  private static final String CFG_PATH = "component-configs/";

    /**
     * Reads configuration from yaml configuration files that supposed to be located under 'component-configs/'.
     *
     * @param component component name
     * @return map of components fields configuration
     */
  public Map<String, Map<String, List<FieldConfig>>> getConfigs(String component) {
    TypeReference typeReference = new TypeReference<Map<String, Map<String, List<FieldConfig>>>>() {
    };
    String path = CFG_PATH + component.toLowerCase().replace(" ", "-");
    return YamlReader.read(path, typeReference);
  }
}
