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
package com.cognifide.qa.bb.loadable.hierarchy;

import com.cognifide.qa.bb.loadable.context.ComponentContext;
import com.cognifide.qa.bb.loadable.context.ConditionContext;

import java.util.ArrayList;
import java.util.List;

public class ClassFieldContext {

  private final Object subject;

  private final List<ConditionContext> conditionData;

  public ClassFieldContext(Object subject, List<ConditionContext> conditionData) {
    this.subject = subject;
    this.conditionData = conditionData;
  }

  public List<ComponentContext> toLoadableContextList() {
    List<ComponentContext> result = new ArrayList<>();
    conditionData.stream().
            forEach((loadableComponentData) -> {
              result.add(new ComponentContext(subject, loadableComponentData));
    });
    return result;
  }

  public Object getSubject() {
    return subject;
  }

  public List<ConditionContext> getConditionData() {
    return conditionData;
  }

}
