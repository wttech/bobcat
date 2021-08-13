/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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
package com.cognifide.qa.bb.core.modules;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.modules.CoreModule;
import com.google.inject.AbstractModule;

public class ProxyEnabledModule extends AbstractModule {
  @Override
  protected void configure() {
    System.setProperty(ConfigKeys.PROXY_ENABLED, "true");
    install(new CoreModule());
  }
}
