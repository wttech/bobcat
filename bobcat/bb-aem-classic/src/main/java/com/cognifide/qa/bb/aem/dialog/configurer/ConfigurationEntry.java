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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single row in a data table.
 */
public class ConfigurationEntry {
  private String tab;

  private String type;

  private String label;

  private String value;

  public ConfigurationEntry() {
    // There is default constructor being generated for this class, hence the explicit definition
    // is needed
  }

  /**
   * @param tab   dialog tab name
   * @param type  field type
   * @param label field label
   * @param value field value
   */
  public ConfigurationEntry(String tab, String type, String label, String value) {
    this.tab = tab;
    this.type = type;
    this.label = label;
    this.value = value;
  }

  /**
   * @return Tab label in a dialog.
   */
  public String getTab() {
    return tab;
  }

  /**
   * Sets the tab label.
   *
   * @param tab Tab label.
   */
  public void setTab(String tab) {
    this.tab = tab;
  }

  /**
   * @return Type of the component, e.g. "dropdown", "text field", "checkbox".
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of the component.
   *
   * @param type Type of the component.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return Label of the dialog field.
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets label of the dialog field.
   *
   * @param label Label of the dialog field.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * @return Value assigned to the field.
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value assigned to the field.
   *
   * @param value Value assigned to the field.
   */
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(this.tab)
        .append(this.type)
        .append(this.label)
        .append(this.value)
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    ConfigurationEntry rhs = (ConfigurationEntry) obj;
    return new EqualsBuilder()
        .append(tab, rhs.tab)
        .append(label, rhs.label)
        .append(type, rhs.type)
        .append(value, rhs.value)
        .isEquals();
  }

  @Override
  public String toString() {
    return super.toString() + String.format(" : [tab=%s, type=%s, label=%s, value=%s]",
        this.tab,
        this.type,
        this.label,
        this.value);
  }
}
