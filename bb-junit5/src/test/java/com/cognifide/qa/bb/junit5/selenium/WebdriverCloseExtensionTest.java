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
package com.cognifide.qa.bb.junit5.selenium;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

@ExtendWith(MockitoExtension.class)
class WebdriverCloseExtensionTest {

  @Mock
  private WebDriver webdriver;

  @Spy
  private WebdriverCloseExtension tested;

  @BeforeEach
  void setup() {
    doReturn(webdriver).when(tested).getWebDriver(any());
    tested.beforeTestExecution(any());
  }

  @Test
  void shouldCloseWebDriverIfInjectorPresent() {
    tested.afterTestExecution(any());

    verify(webdriver).quit();
  }

  @Test
  void afterAll() {
    tested.afterAll(any());

    verify(webdriver).quit();
  }
}
