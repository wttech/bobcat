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
package com.cognifide.qa.bb.aem.ui;

/**
 * AemElement that serves as a container for other AemElements.
 */
public interface FieldContainer {
  /**
   * Fetches field from the container, identified by its label.
   *
   * @param label field label
   * @param dialogFieldType field type
   * @param <T> dialog field class
   * @return Found field.
   */
  <T> T getField(String label, Class<T> dialogFieldType);
}
