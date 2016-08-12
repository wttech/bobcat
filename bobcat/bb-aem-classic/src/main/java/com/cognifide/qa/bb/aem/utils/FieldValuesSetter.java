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
package com.cognifide.qa.bb.aem.utils;

import com.cognifide.qa.bb.aem.dialog.classic.field.AbstractTextInput;
import com.cognifide.qa.bb.aem.dialog.classic.field.Configurable;

/**
 * Utility class that will set value for any configurable field.
 */
public final class FieldValuesSetter {

  private FieldValuesSetter() {
    // empty
  }

  /**
   * Sets the field to the provided value, as long as the field parameter is of type Configurable. Otherwise
   * throws IllegalArgumentException.
   *
   * @param field      Field to set value to.
   * @param fieldValue Value that will be set in the field.
   */
  public static void setFieldValue(Object field, String fieldValue) {
    if (field instanceof Configurable) {
      ((Configurable) field).setValue(fieldValue);
    } else {
      throw new IllegalArgumentException("Type " + field.getClass().getCanonicalName()
          + " is incorrect");
    }

  }

  /**
   * Gets the field value, as long as the field parameter is of type Configurable. Otherwise
   * throws IllegalArgumentException.
   *
   * @param field field
   * @return field value
   */
  public static String getFieldValue(Object field) {
    if (field instanceof AbstractTextInput) {
      return ((AbstractTextInput) field).read();
    } else {
      throw new IllegalArgumentException("Type " + field.getClass().getCanonicalName()
          + " is incorrect");
    }
  }

}
