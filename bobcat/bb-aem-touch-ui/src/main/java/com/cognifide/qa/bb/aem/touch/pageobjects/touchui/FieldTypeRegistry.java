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
package com.cognifide.qa.bb.aem.touch.pageobjects.touchui;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.dialogfields.DialogField;
import com.google.inject.Inject;

/**
 * This class provides registry of fields type and possibility of checking given field type class.
 */
class FieldTypeRegistry {

  private final Map<String, DialogField> registry;

  /**
   * Constructs FieldTypeRegistry. Initializes registry of DialogFields and fills it with values given in constructor parameter.
   *
   * @param dialogFields set of dialog fields which will be translated to dialog fields registry
   */
  @Inject
  public FieldTypeRegistry(Set<DialogField> dialogFields) {
    registry = new HashMap<>();
    dialogFields.stream() //
            .forEach(dialogField -> register(dialogField.getType(), dialogField));
  }

  private void register(String key, DialogField value) {
    registry.put(key, value);
  }

  Class<?> getClass(String type) {
    return registry.get(type).getClass().getSuperclass();
  }
}
