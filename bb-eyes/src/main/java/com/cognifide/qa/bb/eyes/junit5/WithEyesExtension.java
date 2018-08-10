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
package com.cognifide.qa.bb.eyes.junit5;

import static com.cognifide.qa.bb.junit5.JUnit5Constants.NAMESPACE;

import java.util.Properties;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.applitools.eyes.selenium.Eyes;
import com.cognifide.qa.bb.junit5.guice.InjectorUtils;
import com.google.inject.Injector;
import com.google.inject.Key;

public class WithEyesExtension implements BeforeEachCallback, AfterEachCallback {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Injector injector = InjectorUtils.retrieveInjectorFromStore(context, NAMESPACE);
    Properties properties = injector.getInstance(Key.get(Properties.class));
    WebDriver wrappedDriver = ((EventFiringWebDriver) injector.getInstance(WebDriver.class)).getWrappedDriver();
    getEyes(context).open(wrappedDriver, String.valueOf(properties.get("eyes.appName")), context.getDisplayName());
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Eyes eyes = getEyes(context);
    try {
      eyes.close();
    } finally {
      eyes.abortIfNotClosed();
    }
  }

  private Eyes getEyes(ExtensionContext context) {
    return InjectorUtils.retrieveInjectorFromStore(context, NAMESPACE).getInstance(Eyes.class);
  }
}
