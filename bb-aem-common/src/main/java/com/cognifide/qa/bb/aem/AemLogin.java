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
package com.cognifide.qa.bb.aem;

import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Provides fast AEM login method.
 */
@Deprecated
public class AemLogin {

  @Inject
  private WebDriver webDriver;

  @Inject
  private AemAuthCookieFactory aemAuthCookieFactory;

  @Inject
  @Named(AemConfigKeys.AUTHOR_URL)
  private String authorUrl;

  @Inject
  @Named(AemConfigKeys.AUTHOR_LOGIN)
  private String authorLoginProperty;

  @Inject
  @Named(AemConfigKeys.AUTHOR_PASSWORD)
  private String authorPassword;

  /**
   * Opens login page and authenticates the author using cookieFactory. Stay on login page.
   * @param login to AEM instance
   * @param password to AEM instance
   */
  public void authorLogin(String login, String password) {
    login(authorUrl, login, password);
  }

  /**
   * Opens login page and authenticates the author using cookieFactory. Stay on login page.
   */
  public void authorLogin() {
    login(authorUrl, authorLoginProperty, authorPassword);
  }

  /**
   * Logout from the author instance.
   */
  public void authorLogout() {
    logout(authorUrl);
  }

  public void login(String url, String login, String password) {
    webDriver.get(url + "/libs/granite/core/content/login.html");
    webDriver.manage().addCookie(aemAuthCookieFactory.getCookie(url, login, password));
  }

  public void logout(String url) {
    webDriver.get(url + "/system/sling/logout.html");
    aemAuthCookieFactory.removeCookie(url);
  }
}
