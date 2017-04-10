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

import com.cognifide.qa.bb.loadable.tracker.ConditionStatus;
import com.cognifide.qa.bb.loadable.tracker.ConditionProgressTracker;
import com.cognifide.qa.bb.loadable.context.LoadableComponentContext;
import com.cognifide.qa.bb.loadable.condition.LoadableComponentCondition;
import com.cognifide.qa.bb.loadable.exception.LoadableConditionException;
import com.cognifide.qa.bb.utils.AopUtil;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import java.util.Deque;
import java.util.Stack;

/**
 *
 * Class responsible for evaluating the entire hierarchy of Loadable Conditions from top to the bottom
 */
@Singleton
public class ConditionChainRunner {

  @Inject
  private Injector injector;

  /**
   * Evaluates the entire hierarchy of Loadable Conditions from top to the bottom
   *
   * @param conditionStack Stack of conditions to be evaluated
   * @throws LoadableConditionException when some condition fails
   */
  public void chainCheck(ConditionStack conditionStack) {
    Deque<LoadableComponentContext> stack = conditionStack.getLoadableContextStack();
    ConditionProgressTracker progressTracker = new ConditionProgressTracker(stack);

    while (!stack.isEmpty()) {
      LoadableComponentContext loadableContext = stack.pop();
      if (loadableContext.getConditionContext() != null) {
        progressTracker.stepStart(loadableContext);
        LoadableComponentCondition componentCondition = produceInitializedCondition(loadableContext);
        Object subject = acquireSubjectInstance(loadableContext);
        evaluateCondition(componentCondition, subject, loadableContext, progressTracker);
      }
    }
  }

  private Object acquireSubjectInstance(LoadableComponentContext loadableContext) {
    Object result = loadableContext.getSubject();
    if (result == null) {
      result = injector.getInstance(AopUtil.getBaseClassForAopObject(loadableContext.getSubjectClass()));
    }
    return result;
  }

  private LoadableComponentCondition produceInitializedCondition(LoadableComponentContext loadableContext) {
    return injector.getInstance(loadableContext.getConditionContext().
            getLoadableComponent().condClass());
  }

  private void evaluateCondition(LoadableComponentCondition componentCondition, Object subject,
          LoadableComponentContext loadableContext, ConditionProgressTracker progressTracker) {
    boolean result = false;
    Exception exception = null;
    try {
      result = componentCondition.check(subject, loadableContext.getConditionContext().getLoadableComponent());
    } catch (Exception ex) {
      exception = ex;
    } finally {
      manageEvaluationResult(result, exception, progressTracker);
    }
  }

  private void manageEvaluationResult(boolean result, Exception exception,
          ConditionProgressTracker progressTracker) {
    if (!result || exception != null) {
      progressTracker.provideStepResult(ConditionStatus.FAIL);
      throw new LoadableConditionException(progressTracker.produceConditionTraceInfo(exception));
    } else {
      progressTracker.provideStepResult(ConditionStatus.SUCCESS);
    }
  }

}
