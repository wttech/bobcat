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

import com.cognifide.qa.bb.loadable.LoadableProcessorFilter;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.AopUtil;
import com.google.inject.Inject;

import java.util.Set;

public class PageObjectInterceptor implements MethodInterceptor {

  @Inject
  private PageObjectInvocationTracker pageObjectInvocationTracker;

  @Inject
  private Set<LoadableProcessorFilter> loadableProcessorFilters;

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    Class<?> clazz = AopUtil.getBaseClassForAopObject(Class.forName(Thread.currentThread().getStackTrace()[8].
      getClassName()));
    if (loadableProcessorFilters.stream().anyMatch(filter -> filter.isApplicable(clazz))) {
      pageObjectInvocationTracker.clearStack();
    }
    if (methodInvocation.getMethod().getDeclaringClass().isAnnotationPresent(PageObject.class)) {
      pageObjectInvocationTracker.add(clazz, methodInvocation.getThis());
    }
    return methodInvocation.proceed();
  }

}
