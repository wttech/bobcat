/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.aem.touch.data.componentconfigs;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentConfiguration {

  private final Map<String, List<FieldConfig>> data;

  public ComponentConfiguration(Map<String, List<FieldConfig>> data) {
    this.data = data;
  }

  public List<FieldConfig> getConfigurationForTab(String tab) {
    return data.get(tab);
  }

  /**
   * Returns first {@link FieldConfig} with provided label
   * @param configurationKey key of yaml component configuration
   * @param label field label
   * @return
   */
  public FieldConfig getFieldConfigByLabel(String configurationKey, String label) {
    return data.get(configurationKey).stream()
            .filter(t -> t.getLabel().equals(label))
            .findFirst()
            .orElse(null);
  }

  /**
   *
   * @param configurationKey onfigurationKey key of yaml configuration
   * @param type field type provided in yaml component configuration
   * @return
   */
  public List<FieldConfig> getFieldConfigsByType(String configurationKey, String type) {
    return data.get(configurationKey).stream()
            .filter(t -> t.getType().equals(type))
            .collect(Collectors.toList());
  }

  /**
   *
   * @return Tab names described in this documentation
   */
  public Set<String> getTabs() {
    return data.keySet();
  }

}
