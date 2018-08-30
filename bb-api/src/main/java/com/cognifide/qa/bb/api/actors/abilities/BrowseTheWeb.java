package com.cognifide.qa.bb.api.actors.abilities;

import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.api.actors.Ability;
import com.cognifide.qa.bb.api.actors.Actor;
import com.google.inject.Inject;

public class BrowseTheWeb implements Ability {
  @Inject
  private WebDriver browser;

  public WebDriver getBrowser() {
    return browser;
  }

  public static BrowseTheWeb as(Actor actor) {
    return actor.thatCan(BrowseTheWeb.class);
  }
}
