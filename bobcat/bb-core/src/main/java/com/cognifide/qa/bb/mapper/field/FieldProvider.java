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
package com.cognifide.qa.bb.mapper.field;

import java.lang.reflect.Field;
import java.util.Optional;

import com.cognifide.qa.bb.scope.PageObjectContext;

/**
 * Bobcat uses classes implementing FieldProvider interface as a source of values
 * for the injected fields of classes (usually PageObjects).
 */
public interface FieldProvider {

  /**
   * PageObjectInjectorListener calls this method to check if the provider is able to handle
   * currently injected field, before it calls provideValue.
   *
   * @param field represents field in page object.
   * @return true if provider is able to handle currently injected field.
   */
  boolean accepts(Field field);

  /**
   * PageObjectInjectorListener calls this method to get the value for the injected class field.
   *
   * @param pageObject This is the parent object of the injected field.
   * @param field      Provider will generate value for this field.
   * @param context    Provider might refer to frame path and locator factory while generating value for the
   *                   field.
   * @return Value for the injected field, as calculated by the provider.
   */
  Optional<Object> provideValue(Object pageObject, Field field, PageObjectContext context);

}
