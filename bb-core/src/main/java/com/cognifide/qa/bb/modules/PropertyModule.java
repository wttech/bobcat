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
package com.cognifide.qa.bb.modules;

import com.cognifide.qa.bb.SystemType;
import com.cognifide.qa.bb.config.ConfigStrategy;
import com.cognifide.qa.bb.config.ConfigStrategyProvider;
import com.cognifide.qa.bb.config.PropertyBinder;
import com.google.inject.AbstractModule;

/**
 * This module is responsible for loading Bobcat configuration.
 */
public class PropertyModule extends AbstractModule {
  @Override
  protected void configure() {
    ConfigStrategy strategy = ConfigStrategyProvider.get();
    PropertyBinder.bindProperties(binder(), strategy);
    bind(SystemType.class).toInstance(SystemType.current());
  }
}
