package com.cognifide.qa.bb.api.actions.basic.navigate;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.BrowseTheWeb;

public class NavigateBack implements Action {
  @Override
  public <T extends Actor> void performAs(T actor) {
    BrowseTheWeb.as(actor).getBrowser().navigate().back();
  }
}
