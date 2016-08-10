/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
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
 * #L%
 */
package com.cognifide.qa.bb.frame;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.scope.frame.FrameMap;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * This is an aspect that intercepts all frame-aware methods. It should not be accessed directly by Bobcat's user.
 * Instead, CoreModule will create instance of this aspect and bind it to PageObjects and classes annotated with Frame
 * annotation.
 */
public class FrameAspect implements MethodInterceptor {

  @Inject
  private FrameMap frameMap;

  @Inject
  private Provider<FrameSwitcher> provider;

  /**
   * This is an advice method that wraps around the intercepted method. It should not be called directly by Bobcat's
   * user.
   * <p>
   * If the intercepted methods comes from Object class, the advice calls this method directly. Otherwise, it switches
   * to frame associated with the intercepted method, performs the method and switches back.
   *
   * @param methodInvocation - current method invocation
   * @throws java.lang.Throwable - throwable coming from invoking method
   */
  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    return isObjectMethodCall(methodInvocation)
        ? methodInvocation.proceed()
        : switchFrameAndProceed(methodInvocation);
  }

  private boolean isObjectMethodCall(MethodInvocation methodInvocation) {
    return methodInvocation.getMethod().getDeclaringClass().equals(Object.class);
  }

  private Object switchFrameAndProceed(MethodInvocation methodInvocation) throws Throwable {
    final FrameSwitcher switcher = provider.get();
    final FramePath framePath = getFramePath(methodInvocation);
    switcher.switchTo(framePath);
    try {
      return methodInvocation.proceed();
    } finally {
      switcher.switchBack();
    }
  }

  private FramePath getFramePath(MethodInvocation methodInvocation) {
    final Frame methodAnnotation = methodInvocation.getMethod().getAnnotation(Frame.class);
    final FramePath classFramePath = frameMap.get(methodInvocation.getThis());
    return methodAnnotation == null ? classFramePath : classFramePath.addFrame(methodAnnotation);
  }
}
