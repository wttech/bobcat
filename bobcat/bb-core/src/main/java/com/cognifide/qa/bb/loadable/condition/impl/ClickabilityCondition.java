/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.loadable.condition.impl;

import com.cognifide.qa.bb.loadable.context.ConditionContext;
import com.cognifide.qa.bb.loadable.condition.LoadableComponentCondition;
import com.cognifide.qa.bb.loadable.exception.LoadableConditionException;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ClickabilityCondition implements LoadableComponentCondition {

  @Inject
  private BobcatWait wait;

  @Override
  public boolean check(Object object, ConditionContext data) {
    if (object instanceof WebElement) {
      WebElement subject = (WebElement) object;
      return wait.withTimeout(data.getTimeout()).until(ignored -> ExpectedConditions.
              elementToBeSelected(subject), 2).apply(null);
    }
    throw new LoadableConditionException("Loadable Component Condition placed on not applicable field");

  }

}
