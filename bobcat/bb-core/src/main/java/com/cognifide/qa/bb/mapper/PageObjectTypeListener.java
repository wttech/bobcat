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

import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * This is Bobcat's default injection listener. When Guice injects an instance of a type decorated with
 * PageObject annotation, PageObjectTypeListener will register a PageObjectInjectorListener in the current
 * TypeEncounter. When Guice finishes default injection, it will ask PageObjectInjectorListener to perform
 * custom injections.
 * <p>
 * This class should not be used by Bobcat's users.
 */
public final class PageObjectTypeListener implements TypeListener {
  /**
   * Automatically called by Guice during injection of any object. Checks if the injected object's type is
   * annotated as PageObject. If yes, it will register PageObjectInjectorListener in the TypeEncounter, so
   * that PageObjectInjectorListener will be able to perform custom injections.
   */
  @Override
  public <I> void hear(TypeLiteral<I> typeLiteral, final TypeEncounter<I> typeEncounter) {
    if (typeLiteral.getRawType().isAnnotationPresent(PageObject.class)) {
      typeEncounter.register(new PageObjectInjectorListener(typeEncounter));
    }
  }
}
