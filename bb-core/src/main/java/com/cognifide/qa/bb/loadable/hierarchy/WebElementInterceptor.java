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

import com.cognifide.qa.bb.loadable.context.ClassFieldContext;
import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.context.ConditionContext;
import com.cognifide.qa.bb.mapper.WebElementCachePolicy;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.AopUtil;
import com.cognifide.qa.bb.webelement.BobcatWebElement;
import com.cognifide.qa.bb.webelement.BobcatWebElementContext;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.List;

import com.google.inject.Injector;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
  private PageObjectInvocationTracker pageObjectInvocationTracker;

  @Inject
  private Injector injector;

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    Class methodCallerClass = getMethodCallerClassWithoutGuiceContext();
    boolean elementRefreshed = false;
    BobcatWebElement caller = (BobcatWebElement) methodInvocation.getThis();
    if (methodCallerClass.isAnnotationPresent(PageObject.class)) {

      LOG.
          debug(
              "Caught invocation of method {} from {}. Started processing loadable component conditions hierarchy",
              methodInvocation.getMethod().getName(), methodCallerClass.getName());

      if (caller.getCachePolicy() == WebElementCachePolicy.POLICY_IGNORE) {
        WebDriver driver = injector.getInstance(WebDriver.class);
        By byReference = caller.convertWebElementToByReference();
        if (byReference != null) {
          BobcatWebElement freshBobcatWebElement = refreshWebElement(caller, driver, byReference);
          elementRefreshed = true;
          caller = freshBobcatWebElement;
        } else {
          LOG.warn("Ignore cache annotation ignored, as there is a problem with webElement: ", caller);
        }
      }

      ClassFieldContext directContext = acquireDirectContext(caller);
      ConditionStack loadableContextHierarchy = loadableCondsExplorer.
          discoverLoadableContextHierarchy(directContext, pageObjectInvocationTracker.
              getSubjectStack());
      loadConditionChainRunner.chainCheck(loadableContextHierarchy);
    }

    return elementRefreshed ? methodInvocation.getMethod().invoke(caller,
        methodInvocation.getArguments()) : methodInvocation.proceed();

  }

  private BobcatWebElement refreshWebElement(BobcatWebElement caller, WebDriver driver, By byReference) {
    WebElement freshElement = driver.findElement(byReference);
    BobcatWebElementContext context =
        new BobcatWebElementContext(freshElement, caller.getLocatable(),
            caller.getLoadableConditionContext(), caller.getCachePolicy());
    return new BobcatWebElement(context);
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
