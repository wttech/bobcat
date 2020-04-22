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
package com.cognifide.qa.bb.junit5.allure;

import static com.cognifide.qa.bb.junit5.JUnit5Constants.NAMESPACE;
import static com.cognifide.qa.bb.junit5.allure.AllureConstants.ALLURE_REPORT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.junit5.guice.InjectorUtils;
import com.google.inject.Injector;
import com.google.inject.Key;

import io.qameta.allure.Allure;

/**
 * Adds screenshot to report if there was an exception.
 * <p>
 * Loaded automatically by ServiceLoader.
 */
public class ScreenshotExtension implements TestExecutionExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ScreenshotExtension.class);

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
      throws Throwable {
    Injector injector = InjectorUtils.retrieveInjectorFromStore(context, NAMESPACE);
    if (injector != null && Boolean
        .valueOf(injector.getInstance(Properties.class).getProperty(ALLURE_REPORT))) {
      WebDriver webDriver = injector.getInstance(Key.get(WebDriver.class));
      try {
        takeScreenshot(webDriver);
      } catch (IOException e) {
        LOG.error("Could not take a screenshot", e);
      }
    }
    throw throwable;
  }

  private void takeScreenshot(WebDriver webDriver) throws IOException {
    File screenshotAs = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    Allure.addAttachment("Screenshot", new FileInputStream(screenshotAs));
  }
}
