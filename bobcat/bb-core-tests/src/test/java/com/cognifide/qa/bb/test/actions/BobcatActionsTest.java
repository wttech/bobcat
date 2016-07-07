package com.cognifide.qa.bb.test.actions;

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


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.test.TestModule;
import com.cognifide.qa.test.util.PageUtils;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules({TestModule.class})
public class BobcatActionsTest {

  private static final String INPUT_TAG_NAME = "input";

  private static final String TEXT_TO_SEND = "test";

  private static final String VALUE_ATTRIBUTE = "value";

  @Inject
  private WebDriver webDriver;

  @Inject
  private Actions actions;

  private WebElement inputElement;

  @Before
  public void setUp() {
    webDriver.get(PageUtils.buildTestPageUrl(this.getClass()));
    inputElement = webDriver.findElement(By.tagName(INPUT_TAG_NAME));
  }

  @Test
  public void shouldContainTextAfterTextSendKeys() {
    //when
    actions.sendKeys(inputElement, TEXT_TO_SEND).perform();

    //then
    assertThat(getTextFromInputElement()).isEqualTo(TEXT_TO_SEND);
  }

  @Test
  public void shouldBeBlankAfterClear() {
    //given
    shouldContainTextAfterTextSendKeys();

    //when
    actions.sendKeys(inputElement, Keys.chord(Keys.CONTROL, "a")).sendKeys(Keys.BACK_SPACE)
        .perform();

    //then
    assertThat(getTextFromInputElement()).isEmpty();
  }

  @Test
  public void shouldBeBlankAfterEmptyStringSendKeys() {
    //when
    actions.sendKeys(inputElement, StringUtils.EMPTY).perform();

    //then
    assertThat(getTextFromInputElement()).isEmpty();
  }

  @Test
  public void shouldContainTextAfterMultiLineSent() {
    //when
    actions.sendKeys(inputElement, TEXT_TO_SEND, StringUtils.SPACE, TEXT_TO_SEND).perform();

    //then
    assertThat(getTextFromInputElement())
        .isEqualTo(TEXT_TO_SEND + StringUtils.SPACE + TEXT_TO_SEND);
  }

  @Test
  public void whenIPressBackSpaceLastLetterIsRemoved() {
    //when
    actions.sendKeys(inputElement, TEXT_TO_SEND, StringUtils.SPACE, TEXT_TO_SEND, Keys.BACK_SPACE)
        .perform();

    //then
    assertThat(getTextFromInputElement()).endsWith(StringUtils.chop(TEXT_TO_SEND));
  }

  private String getTextFromInputElement() {
    return inputElement.getAttribute(VALUE_ATTRIBUTE);
  }

}
