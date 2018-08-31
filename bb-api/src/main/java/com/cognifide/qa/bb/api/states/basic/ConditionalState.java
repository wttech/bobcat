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
package com.cognifide.qa.bb.api.states.basic;

import java.util.function.Function;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.PerformBasicOperations;
import com.cognifide.qa.bb.api.states.State;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;

public class ConditionalState<T> implements State<Boolean> {

  private State<T> stateUnderCondition;
  private Function<T, ExpectedCondition<T>> condition;

  private ConditionalState(State<T> stateUnderCondition,
      Function<T, ExpectedCondition<T>> condition) {
    this.stateUnderCondition = stateUnderCondition;
    this.condition = condition;
  }

  @Override
  public Boolean observedBy(Actor actor) {
    T element = stateUnderCondition.observedBy(actor);
    BobcatWait wait = actor.thatCan(PerformBasicOperations.class).instantiate(BobcatWait.class);
    boolean result = true;
    try {
      wait.withTimeout(Timeouts.SMALL).until(condition.apply(element));
    } catch (TimeoutException e) {
      result = false;
    }
    return result;
  }

  public static <T> ConditionalState<T> of(State<T> stateUnderCondition,
      Function<T, ExpectedCondition<T>> condition) {
    return new ConditionalState<>(stateUnderCondition, condition);
  }
}
