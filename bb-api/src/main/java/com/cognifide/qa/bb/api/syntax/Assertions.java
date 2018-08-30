package com.cognifide.qa.bb.api.syntax;

import java.util.function.Function;

import org.hamcrest.Matcher;

import com.cognifide.qa.bb.api.states.Assertion;
import com.cognifide.qa.bb.api.states.State;
import com.cognifide.qa.bb.api.states.basic.StateInObject;
import com.cognifide.qa.bb.api.states.assertions.StateAssertion;

public class Assertions {
  public static <T> Assertion seeThat(State<T> state, Matcher<T> matcher) {
    return the(state, matcher);
  }

  public static Assertion[] seeThat(Assertion... assertions) {
    return assertions;
  }

  public static <T> Assertion the(State<T> state, Matcher<T> matcher) {
    return new StateAssertion<>(state, matcher);
  }

  public static <T, R> State<R> in(Class<T> type, Function<T, R> element) {
    return StateInObject.of(type, element);
  }
}
