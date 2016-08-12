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

import com.google.common.base.Preconditions;

/**
 * Represents a single field in a fieldset.
 * <p>
 * TODO Change the name to something like FieldInAFieldset.
 */
public class FieldsetConfig {

  private final String fieldValue;

  private final String fieldLabel;

  private final Class fieldType;

  private final String fieldsetLabel;

  /**
   * Constructs FieldsetConfig. Initializes its fields.
   *
   * @param entry          single configuration data
   * @param dialogFieldMap map containing available dialog fields implementations
   */
  public FieldsetConfig(ConfigurationEntry entry, DialogFieldMap dialogFieldMap) {
    String[] types = entry.getType().split("#");
    String[] labels = entry.getLabel().split("#");

    assertCorrectLength(types, labels, entry);

    this.fieldValue = entry.getValue();
    this.fieldType = dialogFieldMap.getField(types[1]);
    this.fieldLabel = labels[1];
    this.fieldsetLabel = labels[0];
  }

  /**
   * @return Value assigned to the field.
   */
  public String getFieldValue() {
    return fieldValue;
  }

  /**
   * @return Label of the field.
   */
  public String getFieldLabel() {
    return fieldLabel;
  }

  /**
   * @return Type of the field.
   */
  public Class<?> getFieldType() {
    return fieldType;
  }

  /**
   * @return Label of the fieldset.
   */
  public String getFieldsetLabel() {
    return fieldsetLabel;
  }

  private void assertCorrectLength(String[] types, String[] labels, ConfigurationEntry entry) {
    Preconditions.checkArgument(types.length >= 2,
        "Type: '%s' defined in the table is incorrect", entry.getType());
    Preconditions.checkArgument(labels.length >= 2,
        "Label: '%s' defined in the table is incorrect", entry.getLabel());
  }
}
