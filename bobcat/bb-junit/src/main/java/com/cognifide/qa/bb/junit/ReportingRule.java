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
package com.cognifide.qa.bb.junit;

import java.util.List;
import java.util.function.Consumer;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.cognifide.qa.bb.utils.AopUtil;

/**
 * This is a test rule that registers all breakpoints in test's lifecycle as entries in TestEventCollector, so that it
 * is possible for the Bobcat's user to generate a report after the test run is finished.
 */
public class ReportingRule extends TestWatcher {

  private static final Logger LOG = LoggerFactory.getLogger(ReportingRule.class);

  private final Injector injector;

  /**
   * Constructs ReportingRule.
   *
   * @param injector ReportingRule will use this injector to get an instance of TestEventCollector.
   */
  public ReportingRule(Injector injector) {
    this.injector = injector;
  }

  @Override
  protected void failed(Throwable e, Description description) {
    LOG.debug("storing exception entry in collectors..");
    collect(testEventCollection -> testEventCollection.exception(e));
  }

  @Override
  protected void starting(Description description) {
    LOG.debug("starting test event collectors..");
    collect(testEventCollection -> testEventCollection.start(getTestName(description)));
  }

  @Override
  protected void finished(Description description) {
    LOG.debug("ending test event collectors..");
    collect(TestEventCollector::end);
  }

  private void collect(Consumer<TestEventCollector> consumer) {
    injector.findBindingsByType(TypeLiteral.get(TestEventCollector.class)).stream()
        .map(Binding::getProvider)
        .map(Provider::get)
        .forEach(consumer);

    logCollectors(injector);
  }

  private void logCollectors(Injector injector) {
    List<Binding<TestEventCollector>> bindings =
        injector.findBindingsByType(TypeLiteral.get(TestEventCollector.class));
    if (LOG.isInfoEnabled()) {
      LOG.info("bound test event collectors: {}", bindings.size());
    }
    if (LOG.isDebugEnabled()) {
      for (Binding<TestEventCollector> binding : bindings) {
        Provider<TestEventCollector> provider = binding.getProvider();
        TestEventCollector collector = provider.get();
        LOG.debug("collector: '{}'", collector);
      }
    }
  }

  private String getTestName(Description d) {
    return String.format("%s - %s", AopUtil.getBaseClassForAopObject(d.getTestClass()).getSimpleName(),
        d.getMethodName());
  }

}
