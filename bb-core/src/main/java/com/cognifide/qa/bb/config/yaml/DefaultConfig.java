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
package com.cognifide.qa.bb.config.yaml;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultConfig {
  private List<String> contexts = Collections.emptyList();

  private Map<String, String> properties = Collections.emptyMap();

  public List<String> getContexts() {
    return contexts;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setContexts(List<String> contexts) {
    this.contexts = contexts;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }
}
