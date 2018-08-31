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
package com.cognifide.qa.bb.api.misc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Ability;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.states.Assertion;

public class DummyActor implements Actor {

  private Map<Class<? extends Ability>, Ability> abilities = new HashMap<>();

  @Override
  public void attemptsTo(Action... actions) {
    Arrays.stream(actions).forEach(action -> action.performAs(this));
  }

  @Override
  public void withAbilityTo(Class<? extends Ability> ability) {
    try {
      abilities.put(ability, ability.newInstance());
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Ability> T thatCan(Class<? extends T> ability) {
    if (!abilities.containsKey(ability)) {
      throw new IllegalStateException("Actor does not posses the requested ability: " + ability);
    }
    return (T) abilities.get(ability);
  }

  @Override
  public void should(Assertion... assertions) {
    Arrays.stream(assertions).forEach(assertion -> assertion.assertedBy(this));
  }

  @Override
  public Map<Class<? extends Ability>, Ability> listAbilities() {
    return abilities;
  }
}
