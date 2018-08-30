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
