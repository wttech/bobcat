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

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.cookies.domain.CookieData;
import com.google.inject.Inject;

public class Cookies {

  private List<CookieData> cookies;

  @Inject
  public Cookies(List<CookieData> cookies) {
    this.cookies = cookies;
  }

  public void setCookies(WebDriver webDriver) {
    cookies.forEach(cookie -> {
      webDriver.get(cookie.getUrl());
      webDriver.manage().addCookie(cookie.convertToSeleniumCookie());
    });
  }

  public List<CookieData> getCookies() {
    return cookies;
  }
}
