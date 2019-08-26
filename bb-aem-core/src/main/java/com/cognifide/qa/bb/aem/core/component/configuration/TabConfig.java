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
package com.cognifide.qa.bb.aem.core.component.configuration;

import java.util.Collections;
import java.util.List;

/**
 * Represents a component configuration of a single dialog tab.
 */
public class TabConfig {

  private final String tabName;

  private final List<FieldConfig> data;

  public TabConfig(String tabName, List<FieldConfig> data) {
    this.tabName = tabName;
    this.data = data;
  }

  /**
   * @return A list of Field Configurations within the tab
   */
  public List<FieldConfig> getData() {
    return Collections.unmodifiableList(data);
  }

  /**
   * @return tab name
   */
  public String getTabName() {
    return tabName;
  }

  @Override
  public String toString() {
    return "Tab '" + tabName + "'; config:\n" + data;
  }
}
