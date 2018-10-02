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
package com.cognifide.qa.bb.expectedconditions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.openqa.selenium.WebDriver;

@Deprecated
@RunWith(Parameterized.class)
public class UrlExpectedConditionsPageUrlIsTest {

  private static final String VALID_URL = "http://valid";

  private static final String INVALID_URL = "http://invalid";

  private static final String EMPTY_URL = "";

  private static final String NULL_URL = null;

  @Rule
  public final MockitoRule mockitoRule = MockitoJUnit.rule();

  private final String providedUrl;

  private final boolean expected;

  @Mock
  private WebDriver webDriver;

  public UrlExpectedConditionsPageUrlIsTest(String providedUrl, boolean expected) {
    this.providedUrl = providedUrl;
    this.expected = expected;
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
        {VALID_URL, true},
        {INVALID_URL, false},
        {EMPTY_URL, false},
        {NULL_URL, false}
    });
  }

  @Test
  public void shouldAnswerTrueWhenUrlsAreTheSame() {
    //given
    setUpWebDriver();

    //when
    boolean actual = UrlExpectedConditions.pageUrlIs(providedUrl).apply(webDriver);

    //then
    assertThat(actual).isEqualTo(expected);
  }

  private void setUpWebDriver() {
    when(webDriver.getCurrentUrl()).thenReturn(VALID_URL);
  }
}
