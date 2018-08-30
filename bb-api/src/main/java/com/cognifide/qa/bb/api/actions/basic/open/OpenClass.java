package com.cognifide.qa.bb.api.actions.basic.open;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.PerformBasicOperations;
import com.cognifide.qa.bb.api.traits.Openable;

public class OpenClass implements Action {
  private final Class<? extends Openable> openable;

  public OpenClass(Class<? extends Openable> openable) {
    this.openable = openable;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    Openable openableObject = actor.thatCan(PerformBasicOperations.class).instantiate(openable);
    openableObject.open();
    Assert.assertThat(openableObject.isOpened(), is(true));
  }
}
