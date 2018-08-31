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
package com.cognifide.qa.bb.api.actors.abilities;

import com.cognifide.qa.bb.api.actors.Ability;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class PerformBasicOperations implements Ability {

  @Inject
  private Injector injector;

  public <T> T instantiate(Class<T> type) {
    return injector.getInstance(type);
  }

  public <T> T locate(Class<T> component) {
    return injector.getInstance(PageObjectInjector.class).inject(component);
  }

  public static PerformBasicOperations as(Actor actor) {
    return actor.thatCan(PerformBasicOperations.class);
  }
}
