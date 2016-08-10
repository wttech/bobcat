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
package com.cognifide.qa.bb.scope.current;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.ParentElementLocatorProvider;

@RunWith(MockitoJUnitRunner.class)
public class CurrentWebElementProviderTest {

  @Mock
  private ContextStack contextStack;

  @InjectMocks
  private CurrentWebElementProvider testedObject;

  @Mock
  private WebDriver webDriver;

  @Mock
  private PageObjectContext pageObjectContext;

  @Mock
  private WebElement webElement;

  @Mock
  private ElementLocator currentScope;

  @Test
  public void shouldProvideTopElementWhenStackIsEmpty() {
    //given
    WebElement topElement = webDriver.findElement(By.xpath(".//"));
    when(contextStack.isEmpty()).thenReturn(true);

    //when
    WebElement tested = testedObject.get();

    //then
    assertThat(tested).isEqualTo(topElement);
  }

  @Test
  public void shouldProvideTopElementWhenFactoryOnStackIsNotParentElementLocator() {
    //given
    WebElement topElement = webDriver.findElement(By.xpath(".//"));
    TestElementLocatorFactory elementLocatorFactory = mock(TestElementLocatorFactory.class);

    when(contextStack.peek()).thenReturn(pageObjectContext);
    when(contextStack.peek().getElementLocatorFactory()).thenReturn(elementLocatorFactory);

    //when
    WebElement tested = testedObject.get();

    //then
    assertThat(tested).isEqualTo(topElement);
  }

  @Test
  public void shouldProvideCurrentElementWhenParentElementLocatorIsOnStack() {
    //given
    TestParentElementLocatorFactory testFactory = mock(TestParentElementLocatorFactory.class);
    when(testFactory.getCurrentScope()).thenReturn(currentScope);
    when(currentScope.findElement()).thenReturn(webElement);
    when(contextStack.peek()).thenReturn(pageObjectContext);
    when(contextStack.peek().getElementLocatorFactory()).thenReturn(testFactory);

    //when
    WebElement actual = testedObject.get();

    //then
    assertThat(actual).isEqualTo(webElement);
  }

  private class TestElementLocatorFactory
      implements ElementLocatorFactory {

    @Override
    public ElementLocator createLocator(Field field) {
      return null;
    }
  }

  private class TestParentElementLocatorFactory
      implements ElementLocatorFactory, ParentElementLocatorProvider {

    @Override
    public ElementLocator createLocator(Field field) {
      return null;
    }

    @Override
    public ElementLocator getCurrentScope() {
      return null;
    }
  }
}
