package com.cognifide.qa.bb.api.actions.basic.navigate;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.BrowseTheWeb;

public class NavigateToUrl implements Action {

  private final String url;

  public NavigateToUrl(String url) {
    this.url = url;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.thatCan(BrowseTheWeb.class).getBrowser().get(url);
  }
}
