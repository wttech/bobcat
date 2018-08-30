package com.cognifide.qa.bb.api.actions.basic.open;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.traits.Openable;

public class OpenObject implements Action {
  private final Openable openable;

  public OpenObject(Openable openable) {
    this.openable = openable;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    openable.open();
    Assert.assertThat(openable.isOpened(), is(true));
  }
}
