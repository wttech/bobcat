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
package com.cognifide.qa.bb.logging.subreport;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.cognifide.qa.bb.logging.ReporterModule;
import com.cognifide.qa.bb.logging.TestEventCollectorImpl;
import com.google.inject.Inject;

/**
 * This is an aspect that opens and closes subreports automatically.
 * It attaches to all methods that have Subreport annotation.
 * See {@link ReporterModule} for the aspect binding.
 */
public class SubreportInterceptor implements MethodInterceptor {

  @Inject
  private TestEventCollectorImpl testEventCollector;

  /**
   * This is advice method. It retrieves subreport name from the Subreport annotation,
   * opens the subreport, proceeds with the method and finally closes the subreport.
   * @throws java.lang.Throwable
   */
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Subreport subreport = invocation.getMethod().getAnnotation(Subreport.class);
    testEventCollector.startSubreport(subreport.value());
    try {
      return invocation.proceed();
    } finally {
      testEventCollector.endSubreport(subreport.value());
    }
  }

}
