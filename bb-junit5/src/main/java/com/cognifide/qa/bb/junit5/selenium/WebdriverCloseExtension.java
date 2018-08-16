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

import com.cognifide.qa.bb.junit5.guice.InjectorUtils;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

/**
 * Estension that will close webdriver after the test is executed
 */
public class WebdriverCloseExtension implements AfterTestExecutionCallback {

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Injector injector = InjectorUtils.retrieveInjectorFromStore(context, NAMESPACE);
    if (injector != null) {
      injector.getInstance(Key.get(WebDriver.class)).quit();
    }

  }

}
