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

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@PageObject
public class ProjectsScreen {

  private static final String PAGE_TITLE = "AEM Projects";

  private static final Logger LOG = LoggerFactory.getLogger(ProjectsScreen.class);

  @Inject
  private WebDriver webDriver;

  @Inject
  @Named(AemConfigKeys.AUTHOR_URL)
  private String authorUrl;

  @Inject
  private BobcatWait wait;

  @FindBy(css = "a.user.icon-user, a.endor-BlackBar-item.endor-UserProfile, coral-icon.coral-Icon--user")
  private WebElement userIcon;

  @FindBy(css = "#user_dialog, #granite-user-properties, coral-popover.is-open")
  private UserDialog userDialog;

  @Inject
  @Named(AemConfigKeys.PAGE_TITLE_TIMEOUT)
  private int pageTitleTimeout;

  public boolean projectScreenIsDisplayed() {
    try {
      wait.withTimeout(pageTitleTimeout).until(ExpectedConditions.titleIs(PAGE_TITLE));
    } catch (TimeoutException te) {
      LOG.error("Timeout while waiting for page title to appear. Expected title: '{}'", PAGE_TITLE,
          te);
      return false;
    }
    return true;
  }

  public boolean projectScreenIsNotDisplayed() {
    try {
      wait.withTimeout(pageTitleTimeout)
          .until(ExpectedConditions.not(ExpectedConditions.titleIs(PAGE_TITLE)));
    } catch (TimeoutException te) {
      LOG.error("Timeout while waiting for page title to be different than '{}'", PAGE_TITLE, te);
      return false;
    }
    return true;
  }

  public UserDialog openUserDialog() {
    userIcon.click();
    return userDialog;
  }

  public ProjectsScreen open() {
    webDriver.get(authorUrl + "/projects.html");
    wait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.titleIs("AEM Projects"));
    return this;
  }
}
