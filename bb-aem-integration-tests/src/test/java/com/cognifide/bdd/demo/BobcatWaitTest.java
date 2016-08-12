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
package com.cognifide.bdd.demo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class BobcatWaitTest {

  private final static Logger LOG = LoggerFactory.getLogger(BobcatWaitTest.class);

  private static final int TIME_OUT_IN_SECONDS = 2;

  private static final int DELAY = 1;

  @Inject
  private WebDriver webDriver;

  @Inject
  private BobcatWait bobcatWait;

  private static class MyExpectedCondition implements ExpectedCondition<Boolean> {
    private int waitCounter;

    @Override
    public Boolean apply(WebDriver webDriver) {
      waitCounter++;
      return false;
    }

    public int getWaitCounter() {
      return waitCounter;
    }
  }

  @Test
  public void bobcatWaitTest() {
    MyExpectedCondition condition = new MyExpectedCondition();
    try {
      bobcatWait.withTimeout(TIME_OUT_IN_SECONDS).until(condition, DELAY);
    } catch (TimeoutException e) {
      // ignoring timeout
    } catch (Exception e) {
      LOG.error("Excpetion thrown from bobcatWait", e);
    } finally {
      assertThat(numberOfRetries(), is(condition.getWaitCounter()));
    }
  }

  @Test
  public void seleniumWaitTest() {
    MyExpectedCondition condition = new MyExpectedCondition();
    try {
      new WebDriverWait(webDriver, TIME_OUT_IN_SECONDS, DELAY * 1000).until(condition);
    } catch (TimeoutException e) {
      // ignoring timeout
    } finally {
      assertThat(numberOfRetries(), is(condition.getWaitCounter()));
    }
  }

  private int numberOfRetries() {
    return (int) Math.ceil((float) TIME_OUT_IN_SECONDS / DELAY) + 1;
  }
}
