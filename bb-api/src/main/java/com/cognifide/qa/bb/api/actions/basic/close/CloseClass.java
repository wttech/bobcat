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
