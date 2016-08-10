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
package com.cognifide.qa.bb.traffic.aspects;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.proxy.ProxyController;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

/**
 * Handles processing of @RecordTraffic annotation. Starts recording network traffic before executing the
 * annotated method and stops afterwards.
 */
public class RecordTrafficAspect implements MethodInterceptor {

  @Inject
  @Named(ConfigKeys.PROXY_ENABLED)
  private boolean proxyEnabled;

  @Inject
  private Injector injector;

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    if (invocation.getMethod().getDeclaringClass().equals(Object.class)) {
      return invocation.proceed();
    }

    if (proxyEnabled) {
      ProxyController proxyController = injector.getInstance(ProxyController.class);
      proxyController.startAnalysis();
      try {
        return invocation.proceed();
      } finally {
        proxyController.stopAnalysis();
      }
    }

    return invocation.proceed();
  }

}
