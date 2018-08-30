package com.cognifide.qa.bb.api.actors;

import java.util.Map;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.states.Assertion;

public interface Actor {

  void attemptsTo(Action... actions);

  void withAbilityTo(Class<? extends Ability> ability);

  <T extends Ability> T thatCan(Class<? extends T> ability);

  void should(Assertion... assertions);

  Map<Class<? extends Ability>, Ability> listAbilities();
}
