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

import java.util.ArrayList;
import java.util.List;

/**
 * This builder provides a way to construct manually Component Data Tables.
 * Each method creates an ConfigurationEntry matching a single row in the table. To obtain whole config
 * invoke {@link #build()}.
 * <br>
 * Entries are added with default tab name: "General". To change it, use {@link #setTab(String)} method.
 */
public class ComponentConfigBuilder {

  private final List<ConfigurationEntry> config;

  private String currentTab = "General";

  public ComponentConfigBuilder() {
    this.config = new ArrayList<>();
  }

  /**
   * Sets tab name for next entries.
   *
   * @param tab tab name
   * @return this for method chaining
   */
  public ComponentConfigBuilder setTab(String tab) {
    currentTab = tab;
    return this;
  }

  /**
   * Adds field entry, equivalent of: | tab_name | field type | label | value |
   *
   * @param itemType field type
   * @param label    field label
   * @param value    field value
   * @return this for method chaining
   */
  public ComponentConfigBuilder add(String itemType, String label, String value) {
    config.add(new ConfigurationEntry(currentTab, itemType, label, value));
    return this;
  }

  /**
   * Adds multifield entry, equivalent of:
   * | tab_name | multifield#index#itemType | multifieldLabel#itemLabel | value |
   *
   * @param multifieldLabel multifield label
   * @param index           multifield item index
   * @param itemType        multifield item type
   * @param itemLabel       mutlifield item label
   * @param value           field value
   * @return this for method chaining
   */
  public ComponentConfigBuilder addItemInMultifield(String multifieldLabel, int index,
      String itemType, String itemLabel, String value) {
    String type = String.format("multifield#%s#%s", index, itemType);
    String label = String.format("%s#%s", multifieldLabel, itemLabel);
    config.add(new ConfigurationEntry(currentTab, type, label, value));
    return this;
  }

  /**
   * Adds fieldset entry, equivalent of:
   * | tab_name | fieldset#itemType | multifieldLabel#itemLabel | value |
   *
   * @param fieldsetLabel fieldset label
   * @param itemType      fiedset item type
   * @param itemLabel     fieldset item label
   * @param value         field value
   * @return this for method chaining
   */
  public ComponentConfigBuilder addItemInFieldset(String fieldsetLabel, String itemType,
      String itemLabel, String value) {
    String type = String.format("fieldset#%s", itemType);
    String label = String.format("%s#%s", fieldsetLabel, itemLabel);
    config.add(new ConfigurationEntry(currentTab, type, label, value));
    return this;
  }

  /**
   * Builds the Component Config based on added entries.
   *
   * @return component configuration as a {@code List<ConfigurationEntry>}
   */
  public List<ConfigurationEntry> build() {
    return config;
  }
}
