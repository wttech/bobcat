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

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.context.LoadableComponentContext;
import com.cognifide.qa.bb.loadable.context.ConditionContext;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.AopUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This keeps context of class field annotated with {@link LoadableComponent}. It gets split by list of
 * {@link ConditionContext} so the {@link LoadableComponentContext} holds only one condition at the time.
 */
public class ClassFieldContext {

  private final Object subject;

  private final Class subjectClass;

  private final List<ConditionContext> conditionData;

  public ClassFieldContext(Class subjectClass, List<ConditionContext> conditionData) {
    this.subject = null;
    this.subjectClass = AopUtil.getBaseClassForAopObject(subjectClass);
    this.conditionData = conditionData;
  }

  public ClassFieldContext(Object subject, List<ConditionContext> conditionData) {
    this.subject = subject;
    this.subjectClass = AopUtil.getBaseClassForAopObject(subject);
    this.conditionData = conditionData;
  }

  public List<LoadableComponentContext> toLoadableContextList() {
    List<LoadableComponentContext> result = new ArrayList<>();
    conditionData.stream().
            forEach((loadableComponentData) -> {
              result.add(new LoadableComponentContext(subject, subjectClass, loadableComponentData));
            });
    return result;
  }

  /**
   *
   * @return subject instance. Null when the context is regarding {@link PageObject} which needs lazy
   * initialization.
   */
  public Object getSubject() {
    return subject;
  }

  /**
   *
   * @return Subject's class
   */
  public Class getSubjectClass() {
    return subjectClass;
  }

  /**
   *
   * @return Condition Context list form {@link LoadableComponent} annotations.
   */
  public List<ConditionContext> getConditionData() {
    return conditionData;
  }

}
