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
package com.cognifide.qa.bb.provider.selenium.webdriver.close;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.provider.selenium.webdriver.BobcatTargetLocator;

@RunWith(MockitoJUnitRunner.class)
public class ClosingAwareWebDriverWrapperTest {

  public static final boolean IS_REUSABLE = true;

  public static final boolean NOT_REUSABLE = false;

  public static final boolean IS_MAXIMIZED = true;

  public static final boolean NOT_MAXIMIZED = false;

  public static final boolean IS_MOBILE = true;

  public static final boolean NOT_MOBILE = false;

  private WebDriverListener listener;

  @Mock
  private WebDriver webDriver;

  @Mock
  private WebDriver.Options options;

  @Mock
  private WebDriver.Window window;

  @Mock
  @SuppressWarnings("unused")
  private WebDriver.TargetLocator targetLocator;

  @Mock
  private FrameSwitcher frameSwitcher;

  @InjectMocks
  @Spy
  private BobcatTargetLocator bobcatTargetLocator;

  @Mock
  private Alert alert;

  private ClosingAwareWebDriverWrapper testedObject;

  @Test
  public void shouldCleanDriverOnCloseWhenReusable() {
    //given
    setUp(IS_MAXIMIZED, IS_REUSABLE, IS_MOBILE);
    registerListener();

    //when
    testedObject.close();

    //then
    verify(testedObject).close();
    verify(options).deleteAllCookies();
    assertThat(testedObject.isAlive(), is(true));
    assertListenerReceivedEventWithValue(false);
  }

  @Test
  public void shouldCloseDriverOnCloseWhenNotReusable() {
    //given
    setUp(NOT_MAXIMIZED, NOT_REUSABLE, IS_MOBILE);
    registerListener();

    //when
    testedObject.close();

    //then
    verify(testedObject).close();
    assertThat(testedObject.isAlive(), is(false));
    assertListenerReceivedEventWithValue(true);
  }

  @Test
  public void shouldCleanDriverOnQuitWhenReusable() {
    //given
    setUp(NOT_MAXIMIZED, IS_REUSABLE, IS_MOBILE);
    registerListener();

    //when
    testedObject.quit();

    //then
    verify(testedObject).quit();
    verify(options).deleteAllCookies();
    assertThat(testedObject.isAlive(), is(true));
    assertListenerReceivedEventWithValue(false);
  }

  @Test
  public void shouldQuitDriverOnQuitWhenNotReusable() {
    //given
    setUp(NOT_MAXIMIZED, NOT_REUSABLE, IS_MOBILE);
    registerListener();

    //when
    testedObject.quit();

    //then
    verify(testedObject).quit();
    assertThat(testedObject.isAlive(), is(false));
    assertListenerReceivedEventWithValue(true);
  }

  @Test
  public void shouldTryToCloseAlertWhenNotMobileDuringCleanup() {
    //given
    setUp(IS_MAXIMIZED, IS_REUSABLE, NOT_MOBILE);

    //when
    testedObject.quit();

    //then
    verify(alert).accept();
  }

  @Test
  public void shouldMaximizeDuringCleanupWhenPropertyIsSet() {
    //given
    setUp(IS_MAXIMIZED, IS_REUSABLE, IS_MOBILE);

    //when
    testedObject.quit();

    //then
    verify(window).maximize();
  }

  @Test
  public void shouldAddListener() {
    //given
    setUp(NOT_MAXIMIZED, NOT_REUSABLE, IS_MOBILE);
    listener = new WebDriverListener();

    //when
    testedObject.addListener(listener);

    //then
    verify(testedObject).addListener(listener);
  }

  @Test
  public void shouldQuitOnForceShutdown() {
    //given
    setUp(NOT_MAXIMIZED, IS_REUSABLE, IS_MOBILE);

    //when
    testedObject.forceShutdown();

    //then
    verify(testedObject).forceShutdown();
    assertThat(testedObject.isAlive(), is(false));
  }

  private void setUp(boolean maximize, boolean reusable, boolean mobile) {
    testedObject = spy(
        new ClosingAwareWebDriverWrapper(webDriver, frameSwitcher, maximize, reusable, mobile));

    when(webDriver.manage()).thenReturn(options);
    when(webDriver.manage().window()).thenReturn(window);
    when(webDriver.switchTo()).thenReturn(bobcatTargetLocator);
    when(webDriver.switchTo().alert()).thenReturn(alert);
    doThrow(new NoAlertPresentException()).when(alert).accept();
  }

  private void registerListener() {
    listener = new WebDriverListener();

    testedObject.addListener(listener);
  }

  private void assertListenerReceivedEventWithValue(boolean expected) {
    assertThat(listener.wasTerminated, is(expected));
  }

  private class WebDriverListener implements WebDriverClosedListener {
    boolean wasTerminated;

    @Override
    public void onWebDriverClosed(boolean terminated) {
      wasTerminated = terminated;
    }
  }
}
