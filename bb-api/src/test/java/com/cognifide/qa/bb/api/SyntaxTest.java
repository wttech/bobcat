package com.cognifide.qa.bb.api;

import static com.cognifide.qa.bb.api.syntax.Assertions.seeThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.misc.DummyActor;
import com.cognifide.qa.bb.api.misc.ExampleState;
import com.cognifide.qa.bb.api.misc.PerformDummyAction;

public class SyntaxTest {

  @Test
  public void apiSyntaxCompiles() {
    Actor actor = new DummyActor();

    actor.attemptsTo(new PerformDummyAction());

    actor.should(seeThat(new ExampleState(), is("Example state")));
  }
}
