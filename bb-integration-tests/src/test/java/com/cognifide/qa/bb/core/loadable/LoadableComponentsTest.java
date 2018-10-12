/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.core.loadable;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.core.TestModule;
import com.cognifide.qa.bb.junit5.guice.GuiceExtension;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.exception.LoadableConditionException;
import com.google.inject.Inject;

@ExtendWith(GuiceExtension.class)
@Modules(TestModule.class)
public class LoadableComponentsTest {

  @Inject
  private ResultCaptor captor;

  @Inject
  @LoadableComponent(condClass = TestCondition.class)
  private TestPageObject testPage;

  @Before
  public void setUp() {
    captor.reset();
    testPage.open();
  }

  @Test
  public void shouldEvaluateConditionsFromTopToBottom() {
    testPage.sendKeysWithSuccessToSecondSubj();
    Object firstSubject = captor.getSubjects().getFirst();
    Object lastSubject = captor.getSubjects().getLast();

    assertNotNull(firstSubject);
    assertNotNull(lastSubject);

    assertThat(firstSubject, instanceOf(TestPageObject.class));
    assertThat(lastSubject, instanceOf(WebElement.class));
  }

  @Test
  public void shouldPersistAppropriateDataFromAnnotation() {
    testPage.sendKeysWithSuccessToFirstSubj();
    assertProperContext(3, 3);

    captor.reset();

    testPage.sendKeysWithSuccessToSecondSubj();
    assertProperContext(8, 8);
  }

  @Test
  public void shouldHandleErrorsProperly() {
    Exception exception = null;
    try {
      testPage.sendKeysWithFailure();
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertThat(exception, instanceOf(LoadableConditionException.class));

    assertThat(exception.getMessage(), containsString(
        "com.cognifide.qa.bb.core.loadable.LoadableComponentsTest -> testPage ~ com.cognifide.qa.bb.core.loadable.TestCondition (Success)"));
    assertThat(exception.getMessage(), containsString(
        "com.cognifide.qa.bb.core.loadable.TestPageObject -> invalidElement ~ com.cognifide.qa.bb.loadable.condition.impl.VisibilityCondition (Fail)"));
  }

  private void assertProperContext(int delayForMiddleObj, int timeoutForMiddleObj) {
    LoadableComponent firstSubject = (LoadableComponent) captor.getConditionInfo().get(0);
    LoadableComponent middleSubject = (LoadableComponent) captor.getConditionInfo().get(1);
    LoadableComponent lastSubject = (LoadableComponent) captor.getConditionInfo().get(2);

    assertNotNull(firstSubject);
    assertNotNull(middleSubject);
    assertNotNull(lastSubject);

    assertThat(firstSubject.delay(), is(1));
    assertThat(firstSubject.condClass(), equalTo(TestCondition.class));
    assertThat(firstSubject.timeout(), is(10));

    assertThat(middleSubject.delay(), is(delayForMiddleObj));
    assertThat(middleSubject.condClass(), equalTo(TestCondition.class));
    assertThat(middleSubject.timeout(), is(timeoutForMiddleObj));

    assertThat(lastSubject.delay(), is(1));
    assertThat(lastSubject.condClass(), equalTo(TestCondition.class));
    assertThat(lastSubject.timeout(), is(10));
  }

}
