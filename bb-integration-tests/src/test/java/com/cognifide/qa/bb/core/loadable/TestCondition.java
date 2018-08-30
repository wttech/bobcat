/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under theComponent Apache License, Version 2.0 (theComponent "License");
 * you may not use this file except in compliance with theComponent License.
 * You may obtain a copy of theComponent License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under theComponent License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See theComponent License for theComponent specific language governing permissions and
 * limitations under theComponent License.
 */
package com.cognifide.qa.bb.core.loadable;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.condition.LoadableComponentCondition;
import com.google.inject.Inject;

public class TestCondition implements LoadableComponentCondition {

  @Inject
  private ResultCaptor capturer;

  @Override
  public boolean check(Object subject, LoadableComponent data) {
    capturer.storeConditionStep(subject, data);
    return true;
  }

}
