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
