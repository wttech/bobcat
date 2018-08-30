package com.cognifide.qa.bb.aem.api.actions.edit;

import java.util.Collections;

import com.cognifide.qa.bb.aem.api.actions.CloseDialog;
import com.cognifide.qa.bb.aem.api.actions.Configure;
import com.cognifide.qa.bb.aem.api.actions.OpenDialog;
import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;

public class EditComponent implements Action {

  private final Class component;

  public EditComponent(Class component) {
    this.component = component;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
        OpenDialog.forComponent(component),
        Configure.theComponent(component, Collections.singletonMap("field", "value")),
        CloseDialog.forComponent(component));
  }
}
