package com.cognifide.qa.bb.aem.api.actions.configure;

import java.util.Map;

import com.cognifide.qa.bb.aem.api.Dialog;
import com.cognifide.qa.bb.aem.api.utils.ComponentUtils;
import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.PerformBasicOperations;

public class ConfigureComponent implements Action {
  private final Class component;
  private final Map parameters;

  public ConfigureComponent(Class component, Map parameters) {
    this.component = component;
    this.parameters = parameters;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    Class<? extends Dialog> dialog = ComponentUtils.getDialog(component);
    Dialog locatedDialog = actor.thatCan(PerformBasicOperations.class).locate(dialog);

    System.out.println(
        "Configuring dialog for " + component + " using following parameters:" + parameters);
  }
}
