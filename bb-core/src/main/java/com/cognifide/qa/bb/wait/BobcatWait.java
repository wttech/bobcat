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
package com.cognifide.qa.bb.wait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.google.inject.Inject;

/**
 * This is the go-to solution for handling dynamic elements with Bobcat.
 * It is based on the {@link org.openqa.selenium.support.ui.WebDriverWait} mechanism.
 */
@ThreadScoped
public class BobcatWait {

  private static final Logger LOG = LoggerFactory.getLogger(BobcatWait.class);

  private Timings timings = new TimingsBuilder().build();

  private List<Class<? extends Throwable>> ignoredExceptions = new ArrayList<>();

  private WebDriver webDriver;

  @Inject
  public BobcatWait(WebDriver webDriver) {
    this.webDriver = webDriver;
  }

  /**
   * Allows to customize the timings (explicit &amp; implicit timeout + polling time).
   *
   * @param timings a {@link Timings} instance - use the {@link TimingsBuilder} to create one
   * @return a self reference
   * @see Timings
   */
  public BobcatWait tweak(Timings timings) {
    this.timings = timings;
    return this;
  }

  /**
   * Adds a list of exception to be ignored during condition evaluation
   *
   * @param exceptions list of exceptions to be ignored
   * @return a self reference
   * @see org.openqa.selenium.support.ui.FluentWait#ignoreAll(Collection)
   */
  public BobcatWait ignoring(List<Class<? extends Throwable>> exceptions) {
    ignoredExceptions.addAll(exceptions);
    return this;
  }

  /**
   * This method enhances Selenium's "until" method.
   * First it reduces implicit timeout to a near-zero value.
   * Then a timer is started and the condition is checked cyclically until it is fulfilled
   * or the timer times out. Finally, implicit timeout is set back to the value
   * defined in the property file (under {@value com.cognifide.qa.bb.constants.ConfigKeys#TIMINGS_IMPLICIT_TIMEOUT}
   *
   * @param condition Selenium's condition object that is queried cyclically inside the wait loop.
   * @param <T>       The function's expected return type.
   * @return The ExpectedCondition's return value if the function returned something different from null or false before the timeout expired.
   * @see WebDriverWait#until(Function)
   */
  public <T> T until(ExpectedCondition<T> condition) {
    try {
      setImplicitTimeoutToNearZero();
      return getWebDriverWait()
          .ignoreAll(ignoredExceptions)
          .until(condition);
    } finally {
      ignoredExceptions = new ArrayList<>();
      timings = new TimingsBuilder().build();
      restoreImplicitTimeout();
    }
  }

  /**
   * Checks if the provided condition is met.
   *
   * @param condition condition to be checked
   * @return boolean indicating if condition is met
   */
  public boolean isConditionMet(final ExpectedCondition<?> condition) {
    try {
      until(condition);
    } catch (TimeoutException | StaleElementReferenceException e) {
      LOG.debug("{} condition has not been met before timeout ", condition, e);
      return false;
    }
    return true;
  }

  /**
   * Sets implicit timeout to {@value Timings#NEAR_ZERO} milliseconds.
   */
  protected void setImplicitTimeoutToNearZero() {
    webDriver.manage().timeouts().implicitlyWait(Timings.NEAR_ZERO, TimeUnit.SECONDS);
  }

  /**
   * Restores implicit timeout to the value defined in the {@link Timings} instance.
   */
  protected void restoreImplicitTimeout() {
    webDriver.manage().timeouts().implicitlyWait(timings.getImplicitTimeout(), TimeUnit.SECONDS);
  }

  /**
   * @return an instance of {@link WebDriverWait} based on the provided {@link Timings}
   */
  protected WebDriverWait getWebDriverWait() {
    return new WebDriverWait(webDriver, timings.getExplicitTimeout(), timings.getPollingInterval());
  }
}
