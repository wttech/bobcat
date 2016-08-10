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
package com.cognifide.qa.bb.aem.touch.data.components;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents component description.
 */
public class ComponentDescription {
  @JsonProperty
  private String datapath;

  @JsonProperty
  private Class clazz;

  @JsonProperty
  private Map<String, String> variants;

  /**
   * @return Data path of the component.
   */
  public String getDatapath() {
    return datapath;
  }

  /**
   * @return Class of the component.
   */
  public Class getClazz() {
    return clazz;
  }

  /**
   * @return Map of the component variants.
   */
  public Map<String, String> getVariants() {
    return variants;
  }
}
