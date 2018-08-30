package com.cognifide.qa.bb.aem.api.actions;

import com.cognifide.qa.bb.aem.api.Dialog;
import com.cognifide.qa.bb.aem.api.utils.ComponentUtils;
import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actions.basic.Close;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.PerformBasicOperations;

public class CloseDialog implements Action {
  private final Class component;

  private CloseDialog(Class component) {
    this.component = component;
  }

  public static Action forComponent(Class component) {
    return new CloseDialog(component);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    Class<? extends Dialog> dialog = ComponentUtils.getDialog(component);
    Dialog locatedDialog = PerformBasicOperations.as(actor).locate(dialog);

    actor.attemptsTo(Close.the(locatedDialog));
  }
}
