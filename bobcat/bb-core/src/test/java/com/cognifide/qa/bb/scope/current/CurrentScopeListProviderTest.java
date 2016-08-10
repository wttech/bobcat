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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.nested.ScopedElementLocatorFactory;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class CurrentScopeListProviderTest {

  @Mock
  private ContextStack contextStack;

  @InjectMocks
  private CurrentScopeListProvider testedObject;

  @Mock
  private PageObjectContext pageObjectContext;

  @Mock
  private ElementLocator currentScope;

  @Test
  public void shouldReturnEmptyListWhenStackIsEmpty() {
    //given
    when(contextStack.isEmpty()).thenReturn(true);

    //when
    List<WebElement> actual = testedObject.get();

    //then
    assertThat(actual).isEmpty();
  }

  @Test
  public void shouldReturnEmptyListWhenFactoryInStackIsNotScoped() {
    //given
    ElementLocatorFactory elementLocatorFactory = mock(ElementLocatorFactory.class);
    when(contextStack.peek()).thenReturn(pageObjectContext);
    when(contextStack.peek().getElementLocatorFactory()).thenReturn(elementLocatorFactory);

    //when
    List<WebElement> actual = testedObject.get();

    //then
    assertThat(actual).isEmpty();
  }

  @Test
  public void shouldReturnScopedWebElementListWhenScopedFactoryIsInStack()
      throws NoSuchFieldException {
    //given
    ScopedElementLocatorFactory elementLocatorFactory = mock(ScopedElementLocatorFactory.class);
    when(elementLocatorFactory.getCurrentScope()).thenReturn(currentScope);
    when(contextStack.peek()).thenReturn(pageObjectContext);
    when(contextStack.peek().getElementLocatorFactory()).thenReturn(elementLocatorFactory);

    //when
    List<WebElement> actual = testedObject.get();

    //then
    assertThat(actual).isEqualTo(Lists.<WebElement>newArrayList());
  }
}
