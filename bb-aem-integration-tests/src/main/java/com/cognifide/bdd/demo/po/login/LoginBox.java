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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class LoginBox {

  @FindBy(id = "username")
  private WebElement usernameField;

  @FindBy(id = "password")
  private WebElement passwordField;

  @FindBy(css = "button[type=submit]")
  private WebElement loginButton;

  @FindBy(id = "error")
  private WebElement error;

  @Inject
  private BobcatWait bobcatWait;

  public LoginBox enterLogin(final String login) {
    usernameField.sendKeys(login);

    bobcatWait.withTimeout(Timeouts.BIG)
        .until(driver -> usernameField.getAttribute(HtmlTags.Attributes.VALUE).equals(login));
    return this;
  }

  public LoginBox enterPassword(String password) {
    passwordField.sendKeys(password);
    return this;
  }

  public LoginBox clickSignIn() {
    loginButton.click();
    return this;
  }

  public boolean isErrorMessageVisible() {
    return error.isDisplayed();
  }
}
