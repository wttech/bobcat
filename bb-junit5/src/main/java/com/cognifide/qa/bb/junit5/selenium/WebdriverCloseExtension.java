/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.junit5.selenium;

import static com.cognifide.qa.bb.junit5.JUnit5Constants.NAMESPACE;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.junit5.guice.InjectorUtils;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Extension that will close WebDriver after the test is executed.
 * <p>
 * Loaded automatically by ServiceLoader.
 */
public class WebdriverCloseExtension
    implements BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterAllCallback {

  private WebDriver webDriver;

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    webDriver = getWebDriver(context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterTestExecution(ExtensionContext context) {
    if (webDriver != null) {
      webDriver.quit();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  // we need this for potential failures in BeforeEach methods
  public void afterAll(ExtensionContext context) {
    afterTestExecution(context);
  }

  //for mocking purposes
  WebDriver getWebDriver(ExtensionContext context) {
    Injector injector = InjectorUtils.retrieveInjectorFromStore(context, NAMESPACE);
    if (injector != null) {
      return injector.getInstance(Key.get(WebDriver.class));
    }
    throw new IllegalStateException("Could not obtain WebDriver instance");
  }
}
