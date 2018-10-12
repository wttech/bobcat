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
package com.cognifide.qa.bb.core.cookies;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.core.TestModule;
import com.cognifide.qa.bb.junit5.guice.GuiceExtension;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.google.inject.Inject;

@ExtendWith(GuiceExtension.class)
@Modules(TestModule.class)
public class CookiesWithDisabledAutoLoadTest {

  @Inject
  private WebDriver webDriver;

  private Cookie expectedCookie = new Cookie("test-cookie", "value", "/");

  @Test
  public void shouldNotSetCookiesFromCookiesYamlAutomaticallyWhenDisabledViaProperty() {
    assertThat(webDriver.manage().getCookies()).doesNotContain(expectedCookie);
  }
}
