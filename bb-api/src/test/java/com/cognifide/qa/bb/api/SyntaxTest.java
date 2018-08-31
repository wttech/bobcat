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
