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

import com.cognifide.qa.bb.loadable.context.ConditionContext;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.webelement.BobcatWebElement;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebElementInterceptor implements MethodInterceptor {

  private static final int ORIGINAL_CALLER_CLASS_LEVEL = 6;

  private static final Logger LOG = LoggerFactory.getLogger(WebElementInterceptor.class);

  @Inject
  private ConditionsExplorer loadableCondsExplorer;

  @Inject
  private ConditionChainRunner loadConditionChainRunner;

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    Class callerClass = getCallerClass();

    if (callerClass.isAnnotationPresent(PageObject.class)) {
      LOG.
          debug("Caught invocation of method {} from {}. Started processing loadable component conditions hierarchy",
                      methodInvocation.getMethod().getName(), callerClass.getName());

      BobcatWebElement caller = (BobcatWebElement) methodInvocation.getThis();
      ClassFieldContext directContext = acquireDirectContext(caller);
      ConditionStack loadableContextHierarchy
              = loadableCondsExplorer.discoverLoadableContextHierarchy(callerClass, directContext);

      loadConditionChainRunner.chainCheck(loadableContextHierarchy);
    }
    return methodInvocation.proceed();

  }

  private Class getCallerClass() throws ClassNotFoundException {
    return Class.forName(Thread.currentThread().getStackTrace()[ORIGINAL_CALLER_CLASS_LEVEL].
            getClassName());
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
