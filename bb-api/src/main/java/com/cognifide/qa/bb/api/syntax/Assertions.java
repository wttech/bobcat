/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
