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
package com.cognifide.qa.bb.aem.api;

import java.util.function.Function;

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.api.actors.Actor;
import com.cognifide.qa.bb.api.actors.abilities.PerformBasicOperations;
import com.cognifide.qa.bb.api.states.State;

public class WebElementState<T, R> implements State<R> {
  private Function<T, WebElement> elementGetter;
  private Class<T> objectType;
  private Function<WebElement, R> webElementProp;

  private WebElementState(Class<T> objectType, Function<T, WebElement> elementGetter,
      Function<WebElement, R> webElementProp) {
    this.objectType = objectType;
    this.elementGetter = elementGetter;
    this.webElementProp = webElementProp;
  }

  @Override
  public R observedBy(Actor actor) {
    return elementGetter.andThen(webElementProp)
        .apply(actor.thatCan(PerformBasicOperations.class).instantiate(objectType));
  }

  public static <T, R> State<R> of(Class<T> objectType, Function<T, WebElement> elementGetter,
      Function<WebElement, R> webElementProp) {
    return new WebElementState<>(objectType, elementGetter, webElementProp);
  }
}
