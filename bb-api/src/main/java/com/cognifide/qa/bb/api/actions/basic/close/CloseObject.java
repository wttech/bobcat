package com.cognifide.qa.bb.api.actions.basic.close;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.traits.Closeable;

public class CloseObject implements Action {

  private final Closeable closeable;

  public CloseObject(Closeable closeable) {
    this.closeable = closeable;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    closeable.close();
    Assert.assertThat(closeable.isClosed(), is(true));
  }
}
