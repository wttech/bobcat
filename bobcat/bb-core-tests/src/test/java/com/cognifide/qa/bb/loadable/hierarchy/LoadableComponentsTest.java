/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.loadable.hierarchy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.exception.LoadableConditionException;
import com.cognifide.qa.test.TestModule;
import com.google.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;

@RunWith(TestRunner.class)
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
    testPage.sendKeysWithSuccess();
    Object firstSubject = captor.getSubjects().getFirst();
    Object secondSubject = captor.getSubjects().getLast();

    assertNotNull(firstSubject);
    assertNotNull(secondSubject);

    assertThat(firstSubject, instanceOf(TestPageObject.class));
    assertThat(secondSubject, instanceOf(WebElement.class));
  }

  @Test
  public void shouldPersistAppropriateDataFromAnnotation() {
    testPage.sendKeysWithSuccess();
    LoadableComponent firstSubject = (LoadableComponent) captor.getConditionInfo().getFirst();
    LoadableComponent secondSubject = (LoadableComponent) captor.getConditionInfo().getLast();

    assertNotNull(firstSubject);
    assertNotNull(secondSubject);

    assertThat(firstSubject.delay(), is(1));
    assertThat(firstSubject.condClass(), equalTo(TestCondition.class));
    assertThat(firstSubject.timeout(), is(10));

    assertThat(secondSubject.delay(), is(5));
    assertThat(secondSubject.condClass(), equalTo(TestCondition.class));
    assertThat(secondSubject.timeout(), is(5));
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
        "com.cognifide.qa.bb.loadable.hierarchy.LoadableComponentsTest -> testPage ~ com.cognifide.qa.bb.loadable.hierarchy.TestCondition (Success)"));
    assertThat(exception.getMessage(), containsString(
        "com.cognifide.qa.bb.loadable.hierarchy.TestPageObject -> invalidElement ~ com.cognifide.qa.bb.loadable.condition.impl.VisibilityCondition (Fail)"));
  }

}
