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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentConfiguration {

  private final List<TabConfig> data;

  public ComponentConfiguration(List<TabConfig> data) {
    this.data = data;
  }

  /**
   *
   * @param tab Name of tab in the dialog window
   * @return Lists of defined FieldConfigs under the selected tab
   */
  public List<FieldConfig> getConfigurationForTab(String tab) {
    return data.stream().filter(t -> t.getTabName().equals(tab)).findFirst().get().getData();
  }

  /**
   * Returns first {@link FieldConfig} with provided label
   *
   * @param tabName Tab name in the dialog window
   * @param configurationKey key of yaml component configuration
   * @param label field label
   * @return
   */
  public FieldConfig getFieldConfigByLabel(String tabName, String configurationKey, String label) {
    return getConfigurationForTab(tabName).stream()
            .filter(t -> t.getLabel().equals(label))
            .findFirst()
            .orElse(null);
  }

  /**
   *
   * @param tabName Tab name in the dialog window
   * @param configurationKey onfigurationKey key of yaml configuration
   * @param type field type provided in yaml component configuration
   * @return
   */
  public List<FieldConfig> getFieldConfigsByType(String tabName, String configurationKey, String type) {
    return getConfigurationForTab(tabName).stream()
            .filter(t -> t.getType().equals(type))
            .collect(Collectors.toList());
  }

  /**
   *
   * @return Tabs described in the configuration
   */
  public List<TabConfig> getTabs() {
    return Collections.unmodifiableList(data);
  }

}
