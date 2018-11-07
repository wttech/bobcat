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
package com.cognifide.qa.bb.config;

import com.cognifide.qa.bb.constants.ConfigKeys;

/**
 * Provides an instance of {@link ConfigStrategy}.
 */
public final class ConfigStrategyProvider {

  private ConfigStrategyProvider() {
    //empty
  }

  /**
   * Returns a correct implementation of {@link ConfigStrategy}, based on {@value ConfigKeys#CONFIG_STRATEGY} System property.
   * At the moment only single type is implemented in Bobcat:
   * <ul>
   * <li>{@link YamlConfig} is served by default</li>
   * </ul>
   *
   * @return proper implementation of {@link ConfigStrategy}
   */
  public static ConfigStrategy get() {
    return new YamlConfig();
  }
}
