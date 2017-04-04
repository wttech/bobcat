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
package com.cognifide.qa.bb.loadable.hierarchy;

import com.cognifide.qa.bb.loadable.condition.LoadableComponentCondition;
import com.cognifide.qa.bb.loadable.context.ClassFieldContext;
import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.context.ConditionContext;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.AopUtil;
import com.cognifide.qa.bb.webelement.BobcatWebElement;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.inject.Injector;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This intercepts invocation of {@link WebElement} methods and runs evaluation of conditions
 * provided in the {@link LoadableComponent} annotations on field that called the {@link WebElement}
 * method and every field in the hierarchy above.
 *
 */
public class WebElementInterceptor implements MethodInterceptor {

  private static final int ORIGINAL_CALLER_CLASS_LEVEL = 6;

  private static final Logger LOG = LoggerFactory.getLogger(WebElementInterceptor.class);

  @Inject
  private ConditionsExplorer loadableCondsExplorer;

  @Inject
  private ConditionChainRunner loadConditionChainRunner;

  @Inject
  private Injector injector;

  @Inject
  private PageObjectInvocationTracker pageObjectInvocationTracker;

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    Class methodCallerClass = getMethodCallerClassWithoutGuiceContext();

    if (methodCallerClass.isAnnotationPresent(PageObject.class)) {

      LOG.debug(
          "Caught invocation of method {} from {}. Started processing loadable component conditions hierarchy",
          methodInvocation.getMethod().getName(), methodCallerClass.getName());

      BobcatWebElement caller = (BobcatWebElement) methodInvocation.getThis();
      ClassFieldContext directContext = acquireDirectContext(caller);
      LinkedList<Object> subjectStack = pageObjectInvocationTracker.
              getSubjectStack();
      ConditionStack loadableContextHierarchy = loadableCondsExplorer.
          discoverLoadableContextHierarchy(directContext, subjectStack);

      Map<LoadableComponentCondition, List<String>> monitoredMethodsGrpdByConds =
          directContext
              .toLoadableContextList()
              .stream()
              .map(loadableContext -> loadableContext.getConditionContext()
                  .getLoadableComponent().condClass())
              .map(condClazz -> injector.getInstance(condClazz))
              .collect(Collectors.toMap(Function.identity(),
                  loadableCondition -> loadableCondition.getMonitoredMethods()));

      processLoadableConditions(methodInvocation, loadableContextHierarchy,
          monitoredMethodsGrpdByConds);

    }
    return methodInvocation.proceed();

  }

  private void processLoadableConditions(MethodInvocation methodInvocation,
      ConditionStack loadableContextHierarchy,
      Map<LoadableComponentCondition, List<String>> monitoredMethodsGrpdByConds) {
    for (Map.Entry<LoadableComponentCondition, List<String>> entry : monitoredMethodsGrpdByConds
        .entrySet()) {
      List<String> monitoredMethods = entry.getValue();
      String invokedMethodName = methodInvocation.getMethod().getName();
      String conditionClassName = entry.getKey().getClass().getName();
      if (monitoredMethods.contains(invokedMethodName)) {
        LOG.debug("Condition {} is monitoring method {} - evaluating condition hierarchy",
            conditionClassName, invokedMethodName);
        loadConditionChainRunner.chainCheck(loadableContextHierarchy);
        break;
      } else {
        LOG.debug(
            "Condition {} is not monitoring method {} - skipping evaluation of condition hierarchy",
            conditionClassName, invokedMethodName);
      }
    }
  }

  private Class getMethodCallerClassWithoutGuiceContext() throws ClassNotFoundException {
    return AopUtil.getBaseClassForAopObject(Class.
        forName(Thread.currentThread().getStackTrace()[ORIGINAL_CALLER_CLASS_LEVEL].
            getClassName()));
  }

  private ClassFieldContext acquireDirectContext(BobcatWebElement caller) {
    List<ConditionContext> directLoadCondition = caller.getLoadableConditionContext();
    ClassFieldContext directContext;
    if (!directLoadCondition.isEmpty()) {
      directContext = new ClassFieldContext(caller, caller.getLoadableConditionContext());
    } else {
      directContext = new ClassFieldContext(caller, Collections.emptyList());
    }
    return directContext;
  }
}
