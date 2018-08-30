package com.cognifide.qa.bb.api.misc;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;

public class PerformDummyAction implements Action {

  @Override
  public <T extends Actor> void performAs(T actor) {
    System.out.println("Performing a dummy action");
  }
}
