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
package com.cognifide.qa.bb.mapper;

import java.util.Set;

import com.cognifide.qa.bb.mapper.field.FieldProvider;
import com.google.inject.Inject;

/**
 * This class stores Bobcat's field providers. It is used as a provider of field providers in
 * PageObjectInjectorListener. It should not be used directly by Bobcat's users.
 */
public class FieldProviderRegistry {

  @Inject
  private Set<FieldProvider> providers;

  /**
   * @return the list of providers bound to FieldProvider.
   */
  public Set<FieldProvider> getProviders() {
    return providers;
  }
}
