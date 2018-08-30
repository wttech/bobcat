package com.cognifide.qa.bb.aem.api.actions;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;

public class SelectOnHoverbar implements Action {
  private final Class component;
  private final String option;

  public SelectOnHoverbar(Class component, String option) {
    this.component = component;
    this.option = option;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    //locate theComponent hoverbar
    //click theComponent option
    System.out
        .println("Selecting option: " + option + " on " + component + " component's hoverbar");
  }
}
