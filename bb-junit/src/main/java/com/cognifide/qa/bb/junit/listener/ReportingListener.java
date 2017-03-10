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
package com.cognifide.qa.bb.junit.listener;

import java.util.Set;

import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import com.cognifide.qa.bb.junit.concurrent.ReportingHandler;
import com.google.common.collect.Sets;
import com.google.inject.Injector;

/**
 * This is a JUnit lifecycle listener that fires test report generation after running all tests.
 */
public final class ReportingListener extends RunListener {

  private final Set<Injector> injectors = Sets.newConcurrentHashSet();

  @Override
  public void testRunFinished(Result result) throws Exception {
    injectors.stream().forEach(injector -> {
      ReportingHandler reportingHandler = injector.getInstance(ReportingHandler.class);
      if (reportingHandler != null) {
        reportingHandler.generateReport();
      }
    });
  }

  public void addInjector(Injector injector) {
    injectors.add(injector);
  }
}
