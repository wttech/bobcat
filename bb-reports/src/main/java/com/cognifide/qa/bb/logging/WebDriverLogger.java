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
package com.cognifide.qa.bb.logging;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.google.inject.Inject;

/**
 * This class listens for webDriver events and registers them in TestEventCollector.
 * Bobcat users should not create instances of this class manually.
 * Reporting module will create them itself.
 */
public class WebDriverLogger implements WebDriverEventListener {

  @Inject
  private TestEventCollectorImpl eventCollector;

  private String event;

  private String parameter;

  private long startTime;

  private void beforeEvent(String name) {
    beforeEvent(name, null);
  }

  private void beforeEvent(String name, String parameter) {
    this.event = name;
    this.parameter = parameter;
    this.startTime = System.currentTimeMillis();
  }

  private void afterEvent() {
    long duration = System.currentTimeMillis() - startTime;
    eventCollector.event(event, parameter, duration);
  }

  private void afterEvent(String parameter) {
    this.parameter += parameter;
    afterEvent();
  }

  @Override
  public void beforeAlertAccept(WebDriver webDriver) {
    beforeEvent("alertAccept");
  }

  @Override
  public void afterAlertAccept(WebDriver webDriver) {
    afterEvent();
  }

  @Override
  public void afterAlertDismiss(WebDriver webDriver) {
    afterEvent();
  }

  @Override
  public void beforeAlertDismiss(WebDriver webDriver) {
    beforeEvent("alertDismiss");
  }

  @Override
  public void beforeNavigateTo(String url, WebDriver driver) {
    beforeEvent("navigateTo", url);
  }

  @Override
  public void afterNavigateTo(String url, WebDriver driver) {
    afterEvent();
  }

  @Override
  public void beforeNavigateBack(WebDriver driver) {
    beforeEvent("navigateBack");
  }

  @Override
  public void afterNavigateBack(WebDriver driver) {
    afterEvent();
  }

  @Override
  public void beforeNavigateForward(WebDriver driver) {
    beforeEvent("navigateForward");
  }

  @Override
  public void afterNavigateForward(WebDriver driver) {
    afterEvent();
  }

  @Override
  public void beforeFindBy(By by, WebElement element, WebDriver driver) {
    beforeEvent("findBy", fetchParameter(by, element));
  }

  private String fetchParameter(By by, WebElement element) {
    if (element == null) {
      return by.toString();
    } else {
      beforeEvent("findBy", by.toString());
      return String.format("%s -> %s", element.toString(), by.toString());
    }
  }

  @Override
  public void afterFindBy(By by, WebElement element, WebDriver driver) {
    afterEvent();
  }

  @Override
  public void beforeClickOn(WebElement element, WebDriver driver) {
    beforeEvent("click", element.toString());
  }

  @Override
  public void afterClickOn(WebElement element, WebDriver driver) {
    afterEvent();
  }

  @Override
  public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
    StringBuilder sb = new StringBuilder();
    sb.append(element.toString());
    sb.append(" value: [");
    sb.append(element.getAttribute("value"));
    sb.append("] -> [");
    beforeEvent("changeValue", sb.toString());
  }

  @Override
  public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
    afterEvent(element.getAttribute("value") + "]");
  }

  @Override
  public void beforeScript(String script, WebDriver driver) {
    beforeEvent("executeJs", script);
  }

  @Override
  public void afterScript(String script, WebDriver driver) {
    afterEvent();
  }

  @Override
  public void beforeSwitchToWindow(String s, WebDriver webDriver) {
    beforeEvent("switchToWindow");
  }

  @Override
  public void afterSwitchToWindow(String s, WebDriver webDriver) {
    afterEvent();
  }

  @Override
  public void onException(Throwable throwable, WebDriver driver) {
    // empty as we have a lot of selenium exceptions that are expected
    // we don't want to have them in report
  }

  @Override
  public void beforeNavigateRefresh(WebDriver driver) {
    beforeEvent("navigate().refresh()");
  }

  @Override
  public void afterNavigateRefresh(WebDriver driver) {
    afterEvent();
  }
}
