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
package com.cognifide.bdd.demo.po.login;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@PageObject
public class LoginPage {

  private static final String PAGE_TITLE = "AEM Sign In";

  private static final Logger LOG = LoggerFactory.getLogger(LoginPage.class);

  @Inject
  private WebDriver webDriver;

  @Inject
  private PageObjectInjector injector;

  @Inject
  @Named(AemConfigKeys.AUTHOR_LOGIN)
  private String authorLogin;

  @Inject
  @Named(AemConfigKeys.AUTHOR_PASSWORD)
  private String authorPassword;

  @Inject
  @Named(AemConfigKeys.AUTHOR_URL)
  private String authorUrl;

  @Inject
  @Named(AemConfigKeys.PAGE_TITLE_TIMEOUT)
  private int pageTitleTimeout;

  @Inject
  private BobcatWait wait;

  @FindBy(id = "login-box")
  private LoginBox loginBox;

  public LoginBox getLoginBox() {
    return loginBox;
  }

  public LoginPage openLoginPage() {
    webDriver.get(authorUrl);
    wait.withTimeout(Timeouts.MEDIUM)
        .until(ExpectedConditions.visibilityOfElementLocated(By.id("login-box")));
    return this;
  }

  public ProjectsScreen loginAsAuthor() {
    getLoginBox().enterLogin(authorLogin).enterPassword(authorPassword).clickSignIn();
    return injector.inject(ProjectsScreen.class, "/").open();
  }

  public boolean loginPageIsDisplayed() {
    try {
      wait.withTimeout(pageTitleTimeout).until(ExpectedConditions.titleIs(PAGE_TITLE));
    } catch (TimeoutException te) {
      LOG.error("TimeoutException thrown when waiting for page title to appear. Expected title: "
          + PAGE_TITLE, te);
      return false;
    }
    return true;
  }
}
