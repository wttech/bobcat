package com.cognifide.qa.bb.api.states.basic;

import java.util.function.Function;

import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.PerformBasicOperations;
import com.cognifide.qa.bb.api.states.State;

public class StateInObject<T, R> implements State<R> {

  private final Function<T, R> element;
  private Class<T> type;

  private StateInObject(Class<T> type, Function<T, R> element) {
    this.type = type;
    this.element = element;
  }

  @Override
  public R observedBy(Actor actor) {
    T object = actor.thatCan(PerformBasicOperations.class).instantiate(type);
    return element.apply(object);
  }

  public static <T, R> StateInObject<T, R> of(Class<T> type, Function<T, R> elementGetter) {
    return new StateInObject<>(type, elementGetter);
  }
}
