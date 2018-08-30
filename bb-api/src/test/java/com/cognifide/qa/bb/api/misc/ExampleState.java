package com.cognifide.qa.bb.api.misc;

import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.states.State;

public class ExampleState implements State<String> {
  @Override
  public String observedBy(Actor actor) {
    return "Example state";
  }
}
