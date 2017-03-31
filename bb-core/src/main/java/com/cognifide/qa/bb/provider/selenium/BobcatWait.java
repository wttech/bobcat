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
package com.cognifide.qa.bb.provider.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.provider.selenium.webdriver.WebDriverProvider;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This is an utility class that extends Selenium's wait functionality. If you need wait/until replacement,
 * inject instance of this class into your PageObject. If you only need "sleep" function, call it directly
 * from BobcatWait statically, because "sleep" is static.
 * <p>
 * This class serves as a factory for BobcatWebDriverWait instances.
 */
public class BobcatWait {

  private static final Logger LOG = LoggerFactory.getLogger(BobcatWait.class);

  private static final int IMPLICITLY_WAIT_TIME = 10;

  @Inject
  @Named(ConfigKeys.WEBDRIVER_DEFAULT_TIMEOUT)
  private int defaultTimeout;

  @Inject
  private WebDriverProvider webDriverProvider;

  /**
   * This is a wrapper method for Thread's sleep method.
   * <p>
   * It catches and logs InterruptedException thrown from original sleep method,
   * so the user doesn't need to bother with writing a try-catch block.
   * <p>
   * Use this method only if you can't use "withTimeout" (which is more flexible).
   * <p>
   * If you don't use "withTimeout", call "sleep" directly from BobcatWait,
   * without creating an instance.
   *
   * @param durationInSec Thread will sleep for durationInSec seconds.
   */
  public static void sleep(double durationInSec) {
    try {
      TimeUnit.MILLISECONDS.sleep((long) (durationInSec * 1000));
    } catch (InterruptedException e) {
      LOG.error("Sleep was interrupted", e);
      Thread.currentThread().interrupt();
    }
  }

  /**
   * @param timeOutInSeconds When you call "until" on BobcatWebDriverWait,
   *                         it will wait for timeOutInSeconds seconds before throwing an exception.
   * @return BobcatWebDriverWait instance
   */
  public BobcatWebDriverWait withTimeout(int timeOutInSeconds) {
    return new BobcatWebDriverWait(webDriverProvider.get(), timeOutInSeconds, defaultTimeout);
  }

  /**
   * This method checks if the element identified by the locator is present on the page
   * and returns immediately.
   *
   * @param locator Locator that identifies element on the page.
   * @return If true, then element is present on the page.
   */
  public boolean checkPresenceWithNoTimeout(By locator) {
    WebDriver webDriver = webDriverProvider.get();
    webDriver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_TIME, TimeUnit.MILLISECONDS);
    try {
      return !webDriver.findElements(locator).isEmpty();
    } finally {
      webDriver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
    }
  }
}
