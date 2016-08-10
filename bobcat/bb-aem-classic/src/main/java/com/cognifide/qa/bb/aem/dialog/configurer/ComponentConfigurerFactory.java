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
package com.cognifide.qa.bb.aem.dialog.configurer;

import com.cognifide.qa.bb.aem.ui.AemDialog;

/**
 * This interface represents a factory of ComponentConfigurer instances. Guice will create and inject the
 * factory for you. See the binding in BobcumberModule.
 */
public interface ComponentConfigurerFactory {
  /**
   * Factory method that produces ComponentConfigurer instances.
   *
   * @param aemDialog component configuration dialog
   * @return configurer object for the component
   */
  ComponentConfigurer create(AemDialog aemDialog);
}
