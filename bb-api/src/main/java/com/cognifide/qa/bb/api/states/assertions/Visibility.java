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
package com.cognifide.qa.bb.api.states.assertions;

import java.util.function.Function;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.api.states.basic.ConditionalState;
import com.cognifide.qa.bb.api.states.State;
import com.cognifide.qa.bb.api.states.basic.StateInObject;

public class Visibility {

  public static <T> State<Boolean> of(Class<T> objectType,
      Function<T, WebElement> elementGetter) {
    StateInObject<T, WebElement> stateInObject = StateInObject.of(objectType, elementGetter);
    return ConditionalState.of(stateInObject, ExpectedConditions::visibilityOf);
  }

  public static State<Boolean> of(WebElement element) {
    return ConditionalState.of(actor -> element, ExpectedConditions::visibilityOf);
  }
}
