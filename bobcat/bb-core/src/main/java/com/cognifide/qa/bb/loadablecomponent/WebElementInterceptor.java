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
package com.cognifide.qa.bb.loadablecomponent;

import com.cognifide.qa.bb.exceptions.BobcatRuntimeException;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.webelement.BobcatWebElement;
import com.google.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class WebElementInterceptor implements MethodInterceptor {

  private static final int ORIGINAL_CALLER_CLASS_LEVEL = 5;

  @Inject
  private LoadableQualifiersExplorer loadablesExplorer;

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    try {
      Class callerClass = Class.forName(Thread.currentThread().getStackTrace()[ORIGINAL_CALLER_CLASS_LEVEL].
              getClassName());

      if (callerClass.isAnnotationPresent(PageObject.class)) {
        LoadableQualifiersStack loadablesAbove = loadablesExplorer.discoverLoadableContextAbove(callerClass);
        BobcatWebElement caller = (BobcatWebElement) methodInvocation.getThis();
        Loadable directLoadCondition = caller.getLoadable();
        //check()
      }
    } catch (ClassNotFoundException ex) {
      throw new BobcatRuntimeException("Problem with loadables context");
    }
    return methodInvocation.proceed();
  }
}
