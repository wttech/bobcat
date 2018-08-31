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
package com.cognifide.qa.bb.api.actors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.abilities.BrowseTheWeb;
import com.cognifide.qa.bb.api.actors.abilities.PerformBasicOperations;
import com.cognifide.qa.bb.api.states.Assertion;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class Bobcat implements Actor {
  private Injector injector;
  protected Map<Class<? extends Ability>, Ability> abilities = new HashMap<>();

  @Inject
  public Bobcat(Injector injector) {
    this.injector = injector;
    withAbilityTo(PerformBasicOperations.class);
    withAbilityTo(BrowseTheWeb.class);
  } //todo refactor this to loosen the dependency coupling here

  @Override
  public void attemptsTo(Action... actions) {
    Arrays.stream(actions).forEach(action -> action.performAs(this));
  }

  @Override
  public void withAbilityTo(Class<? extends Ability> ability) {
    abilities.put(ability, injector.getInstance(ability));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Ability> T thatCan(Class<? extends T> ability) {
    if (!abilities.containsKey(ability)) {
      throw new IllegalStateException("Actor does not posses the requested ability");
    }
    return (T) abilities.get(ability);
  }

  @Override
  public void should(Assertion... assertions) {
    Arrays.stream(assertions).forEach(state -> state.assertedBy(this));
  }

  @Override
  public Map<Class<? extends Ability>, Ability> listAbilities() {
    return abilities;
  }
}
