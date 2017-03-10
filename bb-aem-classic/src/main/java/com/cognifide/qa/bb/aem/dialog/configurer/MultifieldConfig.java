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
 * Represents a single field in a multifield.
 * <p>
 * TODO Change the name to something like FieldInAMultifield.
 */
public class MultifieldConfig {

  private final String fieldValue;

  private final String fieldLabel;

  private final Class fieldType;

  private final int fieldIndex;

  private final String multifieldLabel;

  /**
   * Constructs MultifieldConfig. Initializes its fields.
   *
   * @param entry          single configuration data
   * @param dialogFieldMap map containing available dialog fields implementations
   */
  public MultifieldConfig(ConfigurationEntry entry, DialogFieldMap dialogFieldMap) {
    String[] types = entry.getType().split("#");
    String[] labels = entry.getLabel().split("#");

    assertCorrectLength(types, labels, entry);

    this.fieldValue = entry.getValue();
    this.fieldIndex = parseFieldIndex(types[1]);
    this.fieldType = dialogFieldMap.getField(types[2]);
    this.fieldLabel = labels[1];
    this.multifieldLabel = labels[0];
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
   * @return Label of the multifield.
   */
  public String getMultiFieldLabel() {
    return multifieldLabel;
  }

  /**
   * @return Index of the field within the multifield.
   */
  public int getFieldIndex() {
    return fieldIndex;
  }

  /**
   * @return Type of the field.
   */
  public Class<?> getFieldType() {
    return fieldType;
  }

  private int parseFieldIndex(String index) {
    try {
      return Integer.parseInt(index);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("FieldIndex is not a valid number: " + index, e);
    }
  }

  private void assertCorrectLength(String[] types, String[] labels, ConfigurationEntry entry) {
    Preconditions.checkArgument(types.length >= 3,
        "Type: '%s' defined in the table is incorrect", entry.getType());
    Preconditions.checkArgument(labels.length >= 2,
        "Label: '%s' defined in the table is incorrect", entry.getLabel());
  }
}
