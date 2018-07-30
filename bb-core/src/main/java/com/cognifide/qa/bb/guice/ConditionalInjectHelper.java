/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.guice;

import com.google.common.collect.Sets;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import java.util.Set;

public final class ConditionalInjectHelper {

  public static <T> Set<T> retrieveSetBindings(Injector injector, Class<T> type) {
    try {
      return injector.getInstance(Key.get((TypeLiteral<Set<T>>) TypeLiteral.get(Types
          .setOf(type))));
    } catch (ConfigurationException e) {
      return Sets.newHashSet();
    }
  }

  private ConditionalInjectHelper(){
    //empty in utility class
  }


}
