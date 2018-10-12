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
package com.cognifide.qa.bb.core.expectedconditions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.core.TestModule;
import com.cognifide.qa.bb.core.util.PageUtils;
import com.cognifide.qa.bb.expectedconditions.CommonExpectedConditions;
import com.cognifide.qa.bb.junit5.guice.GuiceExtension;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

@ExtendWith(GuiceExtension.class)
@Modules({TestModule.class})
public class CommonExpectedConditionsTest {

  private static final String HIGHEST_CONTAINER = "body";

  private static final String CONTAINER_TAG = "div";

  private static final String PRESENT_TAG = "h1";

  private static final String NOT_PRESENT_TAG = "h2";

  private static final String NOT_PRESENT_ATTRIBUTE = "type";

  private static final String PRESENT_ATTRIBUTE_VALUE = "heading";

  private static final String NOT_PRESENT_ATTRIBUTE_VALUE = "other";

  private static final String CONTAINER_CSS_CLASS = "container";

  private static final String EMTPY_CONTAINER_CSS_CLASS = "empty-container";

  private static final String HIDDEN_CONTAINER_CSS_CLASS = "hidden-container";

  private static final int SMALL_ELEMENT_HEIGHT = 10;

  private static final int BIG_ELEMENT_HEIGHT = 200;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Inject
  private WebDriver webDriver;

  @Inject
  private BobcatWait bobcatWait;

  @Before
  public void setUp() {
    webDriver.get(PageUtils.buildTestPageUrl(this.getClass()));
  }

  @Test
  public void shouldAnswerTrueWhenElementIsNotPresent() {
    //given
    By by = By.tagName(NOT_PRESENT_TAG);

    //when
    boolean actual = waitFor(CommonExpectedConditions.elementNotPresent(by));

    //then
    assertThat(actual).as("check if element is not present").isTrue();
  }

  @Test
  public void shouldThrowExceptionWhenElementIsPresent() {
    //given
    exception.expect(TimeoutException.class);
    By by = By.tagName(PRESENT_TAG);

    //when
    waitFor(CommonExpectedConditions.elementNotPresent(by));
  }

  @Test
  public void shouldAnswerTrueWhenElementHasAttributeWithValue() {
    //given
    WebElement element = findPresentTagElement();

    //when
    boolean actual = waitFor(
        CommonExpectedConditions.elementHasAttributeWithValue(
            element, HtmlTags.Attributes.CLASS, PRESENT_ATTRIBUTE_VALUE));

    //then
    assertThat(actual).as("check if element has attribute with value").isTrue();
  }

  @Test
  public void shouldThrowExceptionWhenElementHasTheAttributeWithDifferentValue() {
    //given
    exception.expect(TimeoutException.class);
    WebElement element = findPresentTagElement();

    //when
    waitFor(
        CommonExpectedConditions.elementHasAttributeWithValue(
            element, HtmlTags.Attributes.CLASS, NOT_PRESENT_ATTRIBUTE_VALUE));
  }

  @Test
  public void shouldThrowExceptionWhenElementDoNotHaveGivenAttribute() {
    //given
    exception.expect(TimeoutException.class);
    WebElement element = findPresentTagElement();

    //when
    waitFor(
        CommonExpectedConditions.elementHasAttributeWithValue(
            element, NOT_PRESENT_ATTRIBUTE, PRESENT_ATTRIBUTE_VALUE));
  }

  @Test
  public void shouldAnswerTrueWhenScopedElementDoesNotContainElement() {
    //given
    WebElement element = webDriver.findElement(By.className(EMTPY_CONTAINER_CSS_CLASS));
    By by = By.tagName(PRESENT_TAG);

    //when
    boolean actual =
        waitFor(CommonExpectedConditions.scopedElementLocatedByNotPresent(element, by));

    //then
    assertThat(actual).as("check if element does not contain element").isTrue();
  }

  @Test
  public void shouldThrowExceptionWhenScopedElementContainsElement() {
    //given
    exception.expect(TimeoutException.class);
    WebElement element = webDriver.findElement(By.className(CONTAINER_CSS_CLASS));
    By by = By.tagName(PRESENT_TAG);

    //when
    waitFor(CommonExpectedConditions.scopedElementLocatedByNotPresent(element, by));
  }

  @Test
  public void shouldAnswerTrueWhenElementIsHidden() {
    //given
    By by = By.className(HIDDEN_CONTAINER_CSS_CLASS);

    //when
    boolean actual = waitFor(CommonExpectedConditions.elementNotPresentOrVisible(by));

    //then
    assertThat(actual).as("check if element is hidden").isTrue();
  }

  @Test
  public void shouldThrowExceptionWhenElementIsNotHidden() {
    //given
    exception.expect(TimeoutException.class);
    By by = By.className(CONTAINER_CSS_CLASS);

    //when
    waitFor(CommonExpectedConditions.elementNotPresentOrVisible(by));
  }

  @Test
  public void shouldAnswerTrueWhenElementHeightIsGratherThanTen() {
    //given
    WebElement element = webDriver.findElement(By.tagName(PRESENT_TAG));

    //when
    boolean actual =
        waitFor(CommonExpectedConditions.heightOfElementGreaterThan(element, SMALL_ELEMENT_HEIGHT));

    //then
    assertThat(actual).as("check if element has height greater than %d", SMALL_ELEMENT_HEIGHT)
        .isTrue();
  }

  @Test
  public void shouldThrowExceptionWhenElementHeightIsSmallerThanHundred() {
    //given
    exception.expect(TimeoutException.class);
    WebElement element = webDriver.findElement(By.tagName(PRESENT_TAG));

    //when
    waitFor(CommonExpectedConditions.heightOfElementGreaterThan(element, BIG_ELEMENT_HEIGHT));
  }

  @Test
  public void shouldAnswerTrueWhenElementsSizeIsConstant() {
    //given
    WebElement element = webDriver.findElement(By.tagName(HIGHEST_CONTAINER));
    By by = By.tagName(CONTAINER_TAG);

    //when
    boolean actual = waitFor(CommonExpectedConditions.listSizeIsConstant(element, by));

    //then
    assertThat(actual).as("check if element contains constant number of %s", CONTAINER_TAG)
        .isTrue();
  }

  private boolean waitFor(final ExpectedCondition<Boolean> condition) {
    return bobcatWait.withTimeout(0).until(condition);
  }

  private WebElement findPresentTagElement() {
    return webDriver.findElement(By.tagName(PRESENT_TAG));
  }

}
