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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class performs actual wait. It should always be used with BobcatWait class which acts as a factory for
 * BobcatWebDriverWait.
 *
 * @deprecated will be removed in 2.0
 */
@Deprecated
public class BobcatWebDriverWait {

  private static final int IMPLICITLY_WAIT_TIME = 1;

  private final WebDriver webDriver;

  private final long timeOutInSeconds;

  private final long defaultTimeout;

  /**
   * BobcatWebDriverWait instances are constructed by BobcatWait,
   * so this constructor should not be used by the end user.
   *
   * @param webDriver        WebDriver that will be used to create underlying WebDriverWait object.
   * @param timeOutInSeconds How many seconds to wait before throwing an exception.
   * @param defaultTimeout   Implicit timeout value that will be restored after explicit wait finishes.
   */
  public BobcatWebDriverWait(WebDriver webDriver, long timeOutInSeconds, long defaultTimeout) {
    this.webDriver = webDriver;
    this.timeOutInSeconds = timeOutInSeconds;
    this.defaultTimeout = defaultTimeout;
  }

  /**
   * This method enhances Selenium's "until" method.
   * First it reduces implicit timeout to a near-zero value.
   * Then a timer is started and the condition is checked cyclically until it is fulfilled
   * or the timer times out. Finally, implicit timeout is set back to the value
   * defined in the property file (under "webdriver.defaultTimeout").
   *
   * @param condition Selenium's condition object that is queried cyclically inside the wait loop.
   * @param <T>       The function's expected return type.
   * @return The functions' return value if the function returned something different.
   * from null or false before the timeout expired.
   */
  public <T> T until(ExpectedCondition<T> condition) {
    webDriver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_TIME, TimeUnit.SECONDS);
    final T result = new WebDriverWait(webDriver, timeOutInSeconds).until(condition::apply);
    webDriver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
    return result;
  }

  /**
   * This method enhances Selenium's "until" method.
   * First it reduces implicit timeout to a near-zero value.
   * Then a timer is started and the condition is checked cyclically until it is fulfilled
   * or the timer times out, with delay between checks.
   * Finally, implicit timeout is set back to the value
   * defined in the property file (under "webdriver.defaultTimeout").
   *
   * @param condition Selenium's condition object that is queried cyclically inside the wait loop.
   * @param <T>       The function's expected return type.
   * @param delay     Delay between polls in seconds
   * @return The function's return value if the function returned something different
   * from null or false before the timeout expired.
   */
  public <T> T until(ExpectedCondition<T> condition, long delay) {
    webDriver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_TIME, TimeUnit.SECONDS);
    final T result =
        new WebDriverWait(webDriver, timeOutInSeconds, delay * 1000L).until(condition::apply);
    webDriver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
    return result;
  }
}
