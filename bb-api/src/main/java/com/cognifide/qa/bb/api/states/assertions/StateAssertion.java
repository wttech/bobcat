package com.cognifide.qa.bb.api.states.assertions;

import org.hamcrest.Matcher;
import org.junit.Assert;

import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.states.Assertion;
import com.cognifide.qa.bb.api.states.State;

public class StateAssertion<T> implements Assertion {
  private final State<T> state;
  private final Matcher<T> matcher;

  public StateAssertion(State<T> state, Matcher<T> matcher) {
    this.state = state;
    this.matcher = matcher;
  }

  @Override
  public void assertedBy(Actor actor) {
    Assert.assertThat(state.observedBy(actor), matcher);
  }
}
