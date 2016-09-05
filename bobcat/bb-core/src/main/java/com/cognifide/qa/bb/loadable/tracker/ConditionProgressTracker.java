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
package com.cognifide.qa.bb.loadable.tracker;

import com.cognifide.qa.bb.loadable.context.ComponentContext;
import com.cognifide.qa.bb.loadable.context.ConditionContext;

import java.util.LinkedList;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionProgressTracker {

  private static final Logger LOG = LoggerFactory.getLogger(ConditionProgressTracker.class);

  private final LinkedList<ConditionProgressStep> progressData;

  private final Stack<ComponentContext> conditionHierarchy;

  public ConditionProgressTracker(Stack<ComponentContext> conditionHierarchy) {
    this.conditionHierarchy = conditionHierarchy;
    this.progressData = new LinkedList<>();
  }

  private String produceLoadableComponentInfo(ConditionContext loadableComponentData) {
    return loadableComponentData.getEnclosingClassName() + " -> " + loadableComponentData.getFieldName() + " ~ " + loadableComponentData.
            getCondClass().getName();
  }

  public void stepStart(ComponentContext context) {
    String info = produceLoadableComponentInfo(context.getLoadableData());
    LOG.debug("Started lodable component condition evaluation: " + info);
    progressData.
            add(new ConditionProgressStep(info));
  }

  public void provideStepResult(ConditionStatus status) {
    LOG.debug("Evaluated loadable condition "
            + progressData.peekLast().getLoadableComponentInfo() + " with status " + status.getMessage());

    progressData.peekLast().setStepStatus(status);
  }

  public String produceConditionTraceInfo(String rootCause) {
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
      sb.append("Root cause: ");
      sb.append(rootCause);
    }
    return sb.toString();
  }

}
