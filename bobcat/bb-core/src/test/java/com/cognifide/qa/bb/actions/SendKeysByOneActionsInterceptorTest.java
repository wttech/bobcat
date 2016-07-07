package com.cognifide.qa.bb.actions;

/*-
 * #%L
 * Bobcat Parent
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


import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class SendKeysByOneActionsInterceptorTest {

  private static final String EXAMPLE_TEXT = "TEST ACTION";

  private Actions actions = mock(Actions.class);
  private WebElement webElement = mock(WebElement.class);
  private Actions tested = SendKeysByOneActionsInterceptor.wrap(actions);

  @Before
  public void setUp() {
    Mockito.reset(actions, webElement);
  }

  @Test
  public void shouldCallEachLetterSoManyTimesHowInputHas() throws Exception {
    // when
    tested.sendKeys(EXAMPLE_TEXT);

    // then
    verify(actions, times(3)).sendKeys(any(WebElement.class), eq("T"));
  }

  @Test
  public void shouldProvideWebElementObjectToOriginalMethodCall() throws Exception {
    // when
    tested.sendKeys(webElement, EXAMPLE_TEXT);

    // then
    verify(actions, times(11)).sendKeys(eq(webElement), any(CharSequence.class));
  }

  @Test
  public void shouldReturnStillSameProxyObjectEvenIfDoesntCallSendKeys() throws Exception {
    // when
    Actions result = tested.click();

    // then
    assertTrue(result == tested);
  }

  @Test
  public void shouldSendThisSameArgumentsWhenSpecialKeyProvided() throws Exception {
    // when
    Actions result = tested.sendKeys(Keys.ENTER + EXAMPLE_TEXT);

    // then
    verify(actions).sendKeys(any(WebElement.class), eq(Keys.ENTER + EXAMPLE_TEXT));
  }

  @Test
  public void shouldCallWrappedObjectDIrectlyWhenOtherMethodAreCalled() throws Exception {
    // when
    tested.click();

    // then
    verify(actions).click();
  }
}
