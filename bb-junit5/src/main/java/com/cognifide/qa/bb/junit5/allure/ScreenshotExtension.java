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

import com.cognifide.qa.bb.junit5.guice.InjectorUtils;
import com.cognifide.qa.bb.provider.selenium.webdriver.close.ClosingAwareWebDriverWrapper;
import com.google.inject.Injector;
import com.google.inject.Key;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;

/**
 * Adds screenshot to report if there was an exception
 */
public class ScreenshotExtension implements TestExecutionExceptionHandler {

  private static final String NATIVE_APP_CONTEXT = "NATIVE_APP";

  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ScreenshotExtension.class);

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
      throws Throwable {
    Injector injector = InjectorUtils.retrieveInjectorFromStore(context, NAMESPACE);
    if (injector != null && injector.getInstance(Properties.class)
        .getProperty(ALLURE_REPORT, "false").equals("true")) {
      prepareScreenshot(injector.getInstance(Key.get(WebDriver.class)));
    }

    throw throwable;
  }

  /**
   * Changes context if Appium driver is in use
   * @param webDriver
   */
  private void prepareScreenshot(WebDriver webDriver) {
    try {
      if (webDriver instanceof ClosingAwareWebDriverWrapper
          && ((ClosingAwareWebDriverWrapper) webDriver)
          .getWrappedDriver() instanceof AppiumDriver) {
        AppiumDriver appiumDriver =
            (AppiumDriver) ((ClosingAwareWebDriverWrapper) webDriver).getWrappedDriver();
        String originalContext = appiumDriver.getContext();
        appiumDriver.context(NATIVE_APP_CONTEXT);
        takeScreenshot(webDriver);
        appiumDriver.context(originalContext);

      } else {
        takeScreenshot(webDriver);
      }
    } catch (IOException e) {
      LOG.error("Can't take screenshot", e);
    }

  }

  private void takeScreenshot(WebDriver webDriver) throws IOException {
    File screenshotAs = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    Allure.addAttachment("Screenshot", FileUtils.openInputStream(screenshotAs));
  }
}
