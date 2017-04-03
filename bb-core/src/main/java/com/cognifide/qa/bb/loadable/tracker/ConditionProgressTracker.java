/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.loadable.tracker;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.context.LoadableComponentContext;
import com.cognifide.qa.bb.loadable.context.ConditionContext;

import java.util.Deque;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tracks evaluation of conditions defined in {@link LoadableComponent} annotations
 */
public class ConditionProgressTracker {

  private static final Logger LOG = LoggerFactory.getLogger(ConditionProgressTracker.class);

  private final LinkedList<ConditionProgressStep> progressData;

  private final Deque<LoadableComponentContext> conditionHierarchy;

  public ConditionProgressTracker(Deque<LoadableComponentContext> conditionHierarchy) {
    this.conditionHierarchy = conditionHierarchy;
    this.progressData = new LinkedList<>();
  }

  /**
   * Register start of an step evaluation
   *
   * @param context The context of {@link LoadableComponent} annotation which is currently under
   *        processing
   */
  public void stepStart(LoadableComponentContext context) {
    String info = produceLoadableComponentInfo(context.getConditionContext());
    LOG.debug("Started lodable component condition evaluation: {}", info);
    progressData.add(new ConditionProgressStep(info));
  }

  /**
   * Sets the condition evaluation status after evaluation
   *
   * @param status Condition evaluation status
   */
  public void provideStepResult(ConditionStatus status) {
    ConditionProgressStep lastStep = progressData.peekLast();
    LOG.debug("Evaluated loadable condition {} with status {}", lastStep.getLoadableComponentInfo(),
        status.getMessage());

    lastStep.setStepStatus(status);
  }

  /**
   *
   * @param rootCause Exception that caused the error
   * @return String containing the information about conditions that have been run in hierarchical
   *         order starting from the top. The output is in the following format: [class that uses
   *         the field annotated with {@link LoadableComponent}] - [fieldName] ~ [condition class
   *         that have been under evaluation] (Status)
   */
  public String produceConditionTraceInfo(Exception rootCause) {
    StringBuilder sb = new StringBuilder("Loadable conditions trace info:");
    sb.append(System.lineSeparator());
    int indent = 0;
    while (!progressData.isEmpty()) {
      for (int i = 0; i < indent; i++) {
        sb.append(" ");
      }
      sb.append(progressData.pop().toString());
      sb.append(System.lineSeparator());
      indent += 2;
    }
    int difference = conditionHierarchy.size() - progressData.size();
    if (difference > 0) {
      sb.append(difference);
      sb.append(" conditions left for evaluation.");
      sb.append(System.lineSeparator());
    }
    if (rootCause != null) {
      sb.append("Root cause: ");
      sb.append(System.lineSeparator());
      sb.append(rootCause);
    }
    return sb.toString();
  }

  private String produceLoadableComponentInfo(ConditionContext loadableComponentData) {
    return loadableComponentData.getDeclaringClassName() + " -> "
        + loadableComponentData.getFieldName()
        + " ~ " + loadableComponentData.getLoadableComponent().condClass().getName();
  }

}
