package com.cognifide.qa.bb.aem.api.actions;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;

public class SelectComponent implements Action {
  private final Class component;

  public SelectComponent(Class component) {
    this.component = component;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    //locate Component
    //click on theComponent Component
    System.out.println("Selecting component: " + component);
  }
}
