package com.cognifide.qa.bb.api.states.basic;

import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.BrowseTheWeb;
import com.cognifide.qa.bb.api.states.State;

public enum Page implements State<String> {
  TITLE {
    @Override
    public String observedBy(Actor actor) {
      return BrowseTheWeb.as(actor).getBrowser().getTitle();
    }
  },
  CURRENT_URL {
    @Override
    public String observedBy(Actor actor) {
      return BrowseTheWeb.as(actor).getBrowser().getCurrentUrl();
    }
  }
}
