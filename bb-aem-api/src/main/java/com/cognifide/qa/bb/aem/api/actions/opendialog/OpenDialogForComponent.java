package com.cognifide.qa.bb.aem.api.actions.opendialog;

import com.cognifide.qa.bb.aem.api.actions.SelectComponent;
import com.cognifide.qa.bb.aem.api.actions.SelectOnHoverbar;
import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;

public class OpenDialogForComponent implements Action {

  private final Class component;

  public OpenDialogForComponent(Class component) {
    this.component = component;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    //todo find Parsys & find Component in Parsys ?

    actor.attemptsTo(
        new SelectComponent(component),
        new SelectOnHoverbar(component, "Edit"));
  }
}
