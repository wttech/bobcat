/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.cookies;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.cookies.domain.CookieData;

@RunWith(MockitoJUnitRunner.class)
public class CookiesTest {

  @Mock
  private WebDriver webDriver;

  @Mock
  private WebDriver.Options options;

  private List<CookieData> testData = Arrays.asList(
      new CookieData("", "", "", "", null, false, false),
      new CookieData("", "", "", "", null, false, false),
      new CookieData("", "", "", "", null, false, false)
  );

  private Cookies tested = new Cookies(testData);

  @Before
  public void setUp() {
    when(webDriver.manage()).thenReturn(options);
  }

  @Test
  public void shouldSetAllCookiesInWebDriver() {
    tested.setCookies(webDriver);

    verify(webDriver, times(testData.size())).get(any());
    verify(options, times(testData.size())).addCookie(any());
  }
}
