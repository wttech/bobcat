package com.cognifide.qa.bb.api.actions.basic.close;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.PerformBasicOperations;
import com.cognifide.qa.bb.api.traits.Closeable;

public class CloseClass implements Action {
  private final Class<? extends Closeable> closeable;

  public CloseClass(Class<? extends Closeable> closeable) {
    this.closeable = closeable;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    Closeable closeableObject =
        actor.thatCan(PerformBasicOperations.class).locate(closeable);
    closeableObject.close();
    Assert.assertThat(closeableObject.isClosed(), is(true));
  }
}
