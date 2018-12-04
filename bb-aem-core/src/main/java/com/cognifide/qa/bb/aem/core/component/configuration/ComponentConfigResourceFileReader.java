/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.component.configuration;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cognifide.qa.bb.utils.YamlReader;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Utility class for reading a single component configuration from a YAML file in test resources.
 */
public class ComponentConfigResourceFileReader
    implements ComponentConfigReader<ResourceFileLocation> {

  /**
   * {@inheritDoc}
   */
  @Override
  public ComponentConfiguration readConfiguration(ResourceFileLocation resourceFileLocation) {
    TypeReference typeReference = new TypeReference<Map<String, List<FieldConfig>>>() {
    };
    Map<String, List<FieldConfig>> configData = Collections.unmodifiableMap(
        YamlReader.readFromTestResources(resourceFileLocation.getFileName(), typeReference));

    List<TabConfig> tabsConfig = configData.entrySet().stream()
        .map(entry -> new TabConfig(entry.getKey(), entry.getValue()))
        .collect(toList());
    return new ComponentConfiguration(tabsConfig);
  }

}
