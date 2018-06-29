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
package com.cognifide.qa.bb.eyes;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.applitools.eyes.selenium.Eyes;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class WithEyes implements TestRule {

  private final WebDriver webDriver;
  private final Eyes eyes;
  private String appName;

  @Inject
  public WithEyes(WebDriver webDriver, Eyes eyes, @Named("eyes.appName") String appName) {
    this.webDriver = webDriver;
    this.eyes = eyes;
    this.appName = appName;
  }

  public WebDriver getWebDriver() {
    return webDriver;
  }

  public Eyes getEyes() {
    return eyes;
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        try {
          WebDriver wrappedDriver = ((EventFiringWebDriver) webDriver).getWrappedDriver();
          eyes.open(wrappedDriver, appName, description.getMethodName());
          base.evaluate();
        } finally {
          closeEyes();
        }
      }
    };
  }

  private void closeEyes() {
    try {
      eyes.close();
    } finally {
      eyes.abortIfNotClosed();
    }
  }
}
