/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb.aem.data.components;

import java.util.Map;

public class Components {

  private final Map<String, ComponentDescription> descriptions;

  public Components(Map<String, ComponentDescription> descriptions) {
    this.descriptions = descriptions;
  }

  public String getDataPath(String componentName) {
    return descriptions.get(componentName).getDatapath();
  }

  public Class getClazz(String componentName) {
    return descriptions.get(componentName).getClazz();
  }

  public String getVariantValue(String componentName, String variantName) {
    return descriptions.get(componentName).getVariants().get(variantName);
  }
}
