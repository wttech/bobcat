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

/**
 * Represents map of components and their descriptions
 */
public class Components {

  private final Map<String, ComponentDescription> descriptions;

  /**
   * Constructs Components object
   *
   * @param descriptions map containing components descriptions.
   */
  public Components(Map<String, ComponentDescription> descriptions) {
    this.descriptions = descriptions;
  }

  /**
   * @param componentName name of component.
   * @return data path of the component.
   */
  public String getDataPath(String componentName) {
    return descriptions.get(componentName).getDatapath();
  }

  /**
   * @param componentName name of component.
   * @return class of the component.
   */
  public Class getClazz(String componentName) {
    return descriptions.get(componentName).getClazz();
  }

  /**
   * @param componentName name of component.
   * @param variantName name of the components variant.
   * @return value of the components variant.
   */
  public String getVariantValue(String componentName, String variantName) {
    return descriptions.get(componentName).getVariants().get(variantName);
  }
}
