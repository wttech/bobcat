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

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.Whitebox;

public class CurrentScopeHelperTest {

  @InjectMocks
  public CurrentScopeHelper testedObject = new CurrentScopeHelper();

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private WebElement webElement;

  @Mock
  private WebDriver webDriver;

  @Mock
  private WebDriver.Options options;

  @Mock
  private WebDriver.Timeouts timeouts;

  private PageObjectWithCurrentScope pageObjectWithCurrentScope;

  private PageObjectWithoutCurrentScope pageObjectWithoutCurrentScope;

  @Before
  public void setUp() {
    Whitebox.setInternalState(testedObject, "defaultTimeout", 1);
    setUpWebDriver();
  }

  @Test
  public void shouldReturnFalseWhenCurrentScopeIsNotVisible() throws IllegalAccessException {
    //given
    pageObjectWithCurrentScope = new PageObjectWithCurrentScope(webElement);
    when(webElement.isDisplayed()).thenReturn(false);

    //when
    boolean actual = testedObject.isCurrentScopeVisible(pageObjectWithCurrentScope);

    //then
    assertFalse(actual);
  }

  @Test
  public void shouldReturnTrueWhenCurrentScopeIsVisible() throws IllegalAccessException {
    //given
    pageObjectWithCurrentScope = new PageObjectWithCurrentScope(webElement);
    when(webElement.isDisplayed()).thenReturn(true);

    //when
    boolean actual = testedObject.isCurrentScopeVisible(pageObjectWithCurrentScope);

    //then
    assertTrue(actual);
  }

  @Test
  public void shouldReturnFalseWhenCurrentScopeIsStale() {
    //given
    pageObjectWithCurrentScope = new PageObjectWithCurrentScope(webElement);
    when(webElement.isDisplayed())
        .thenThrow(new StaleElementReferenceException("Expected exception"));

    //when
    boolean actual = testedObject.isCurrentScopeVisible(pageObjectWithCurrentScope);

    //then
    verifyIsDisplayedCalledOnce();
    assertFalse(actual);
  }

  @Test
  public void shouldReturnFalseWhenCurrentScopeIsNotPresent() {
    //given
    pageObjectWithCurrentScope = new PageObjectWithCurrentScope(webElement);
    when(webElement.isDisplayed())
        .thenThrow(new StaleElementReferenceException("Expected exception"));

    //when
    boolean actual = testedObject.isCurrentScopeVisible(pageObjectWithCurrentScope);

    //then
    verifyIsDisplayedCalledOnce();
    assertFalse(actual);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionWhenCurrentScopeAnnotationNotPresent() {
    //given
    pageObjectWithoutCurrentScope = new PageObjectWithoutCurrentScope();
    thrown.expect(IllegalArgumentException.class);

    //then
    testedObject.isCurrentScopeVisible(pageObjectWithoutCurrentScope);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionWhenPageObjectAnnotationNotPresent() {
    //given
    pageObjectWithoutCurrentScope = new PageObjectWithoutCurrentScope();
    thrown.expect(IllegalArgumentException.class);

    //then
    testedObject.isCurrentScopeVisible(StringUtils.EMPTY);
  }

  private void setUpWebDriver() {
    when(webDriver.manage()).thenReturn(options);
    when(options.timeouts()).thenReturn(timeouts);
  }

  private void verifyIsDisplayedCalledOnce() {
    verify(webElement).isDisplayed();
    verifyNoMoreInteractions(webElement);
  }

  @PageObject(generateCurrentScope = false)
  private class PageObjectWithCurrentScope {
    @CurrentScope
    private WebElement webElement;

    public PageObjectWithCurrentScope(WebElement webElement) {
      this.webElement = webElement;
    }
  }


  @PageObject(generateCurrentScope = false)
  private class PageObjectWithoutCurrentScope {
    //empty
  }
}
