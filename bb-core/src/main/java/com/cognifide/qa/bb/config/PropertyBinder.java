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

import java.util.Properties;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * This is a utility class used by CoreModule to create property binding. Its methods should not be used
 * directly by Bobcat's users.
 */
public final class PropertyBinder {

  private PropertyBinder() {
  }

  /**
   * This method reads property files and creates following bindings:
   * <ul>
   * <li>a named binding for each property,
   * <li>a binding for Properties class; bound Property instance contains all the properties.
   * </ul>
   * Property file paths are retrieved from configuration.paths property. If configuration.paths is not set,
   * bindProperties will look in default location, i.e. "src/main/config". Property configuration.paths can
   * contain any number of paths. Paths in configuration.paths should be separated with semicolons.
   *
   * @param binder The Binder instance that will store the newly created property bindings.
   * @param configStrategy the selected configuration strategy
   */
  public static void bindProperties(Binder binder, ConfigStrategy configStrategy) {
    Properties properties = configStrategy.gatherProperties();
    Names.bindProperties(binder, properties);
    binder.bind(Properties.class).toInstance(properties);
  }
}
