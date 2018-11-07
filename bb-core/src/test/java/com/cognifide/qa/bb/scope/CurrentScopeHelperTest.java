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
package com.cognifide.qa.bb.scope;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;

@ExtendWith(MockitoExtension.class)
public class CurrentScopeHelperTest {

  @Mock
  private WebElement webElement;

  @Mock
  private BobcatWait bobcatWait;

  @InjectMocks
  public CurrentScopeHelper testedObject = new CurrentScopeHelper();

  private PageObjectWithCurrentScope pageObjectWithCurrentScope;

  private PageObjectWithoutCurrentScope pageObjectWithoutCurrentScope;

  @Test
  public void isCurrentScopeVisibleShouldIgnoreStaleElementReferenceException() {
    when(bobcatWait.ignoring(any())).thenReturn(bobcatWait);
    when(bobcatWait.isConditionMet(any())).thenReturn(true);
    testedObject.isCurrentScopeVisible(new PageObjectWithCurrentScope(webElement));

    verify(bobcatWait).ignoring(Collections.singletonList(StaleElementReferenceException.class));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionWhenCurrentScopeAnnotationNotPresent() {
    //given
    pageObjectWithoutCurrentScope = new PageObjectWithoutCurrentScope();

    //then
    assertThrows(IllegalArgumentException.class,
        () -> testedObject.isCurrentScopeVisible(pageObjectWithoutCurrentScope));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionWhenPageObjectAnnotationNotPresent() {
    //given
    pageObjectWithoutCurrentScope = new PageObjectWithoutCurrentScope();

    //then
    assertThrows(IllegalArgumentException.class,
        () -> testedObject.isCurrentScopeVisible(StringUtils.EMPTY));
  }

  private void verifyIsDisplayedCalledOnce() {
    verify(webElement).isDisplayed();
    verifyNoMoreInteractions(webElement);
  }

  @PageObject(generateCurrentScope = false)
  private class PageObjectWithCurrentScope {
    @CurrentScope
    private WebElement webElement;

    PageObjectWithCurrentScope(WebElement webElement) {
      this.webElement = webElement;
    }
  }


  @PageObject(generateCurrentScope = false)
  private class PageObjectWithoutCurrentScope {
    //empty
  }
}
