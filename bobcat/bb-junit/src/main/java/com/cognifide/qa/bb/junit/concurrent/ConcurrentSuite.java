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
package com.cognifide.qa.bb.junit.concurrent;

import java.util.Properties;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import com.cognifide.qa.bb.junit.InjectorsMap;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.provider.selenium.webdriver.WebDriverRegistry;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Suite for running tests concurrently.
 */
public final class ConcurrentSuite extends Suite {

  /**
   * Constructs ConcurrentSuite.
   *
   * @param type - class
   * @throws InitializationError - if the test class is malformed.
   */
  public ConcurrentSuite(final Class<?> type) throws InitializationError {
    super(type, new ConcurrentRunnerBuilder(true));

    Modules annotation = type.getAnnotation(Modules.class);
    // only the first annotation matters
    Class<? extends Module> moduleFromAnnotation = annotation.value()[0];
    Injector injector = InjectorsMap.INSTANCE.forModule(moduleFromAnnotation);
    Properties properties = injector.getInstance(Properties.class);
    ReportingHandler reportingHandler = injector.getInstance(ReportingHandler.class);
    WebDriverRegistry webDriverRegistry = injector.getInstance(WebDriverRegistry.class);
    new Thread(reportingHandler).start();

    setScheduler(
        new ConcurrentSuiteRunnerScheduler(type, properties, reportingHandler, webDriverRegistry));
  }
}
