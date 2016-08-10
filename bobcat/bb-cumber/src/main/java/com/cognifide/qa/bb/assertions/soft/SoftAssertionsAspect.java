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
package com.cognifide.qa.bb.assertions.soft;

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * This aspects receives soft assertion failure notifications during step execution.
 * Once the step execution the aspect checks for errors to either:
 * <ul>
 * <li>complete execution normally (no failures)
 * <li>throw original failure cause (exactly 1 failure)
 * <li>throw {@link CompositeFailure} (more than 1 failure)
 * </ul>
 */
public class SoftAssertionsAspect implements MethodInterceptor {

  private static final ThreadLocal<List<Throwable>> failureValues = new ThreadLocal<>();

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    // exclude wait, notify, finalize methods...
    if (methodInvocation.getMethod().getDeclaringClass().equals(Object.class)) {
      return methodInvocation.proceed();
    }

    getFailures().clear();
    Object result = null;
    try {
      result = methodInvocation.proceed();
    } catch (Throwable t) {
      getFailures().add(t);
    }
    if (getFailures().size() == 1) {
      throw getFailures().get(0);
    } else if (getFailures().size() > 1) {
      throw new CompositeFailure(getFailures());
    }
    return result;
  }

  private static List<Throwable> getFailures() {
    List<Throwable> fails = failureValues.get();
    if (fails == null) {
      fails = new ArrayList<>();
      failureValues.set(fails);
    }
    return fails;
  }

  /**
   * Notifies the aspect that a failure occurred.
   *
   * @param softAssertionError failure cause
   */
  public static void addFailure(SoftAssertionError softAssertionError) {
    getFailures().add(softAssertionError);
  }

}
