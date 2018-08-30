package com.cognifide.qa.bb.api.states;

import com.cognifide.qa.bb.api.actors.Actor;

public interface State<T> {
  T observedBy(Actor actor);
}
