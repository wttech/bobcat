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
package com.cognifide.qa.bb.aem.core.login;

import com.cognifide.qa.bb.aem.core.constants.AemConfigKeys;
import com.cognifide.qa.bb.aem.core.pages.AemAuthorPage;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

/**
 * Implementation of {@link AemAuthenticationController} for author instance
 */
public class AuthorAuthenticationController implements AemAuthenticationController {

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

  @Override
  @Step("Login to AEM")
  public void login() {
    webDriver.get(authorUrl + "/libs/granite/core/content/login.html");
    webDriver.manage()
        .addCookie(aemAuthCookieFactory.getCookie(authorUrl, authorLoginProperty, authorPassword));
  }

  @Override
  @Step("Login to AEM and open page {aemAuthorPage.fullUrl}")
  public void login(AemAuthorPage aemAuthorPage) {
    this.login();
    aemAuthorPage.open();
  }

  @Override
  public void logout() {
    webDriver.get(authorUrl + "/system/sling/logout.html");
    aemAuthCookieFactory.removeCookie(authorUrl);
  }
}
