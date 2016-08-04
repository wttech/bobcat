/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb.aem.publish;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This class represents page on publish instance.
 */
public abstract class PublishPage {
  @Inject
  private WebDriver webDriver;

  @Inject
  @Named(AemConfigKeys.PAGE_TITLE_TIMEOUT)
  private int pageTitleTimeout;

  @Inject
  @Named(AemConfigKeys.PUBLISH_URL)
  private String publishUrl;

  @Inject
  @Named(ConfigKeys.WEBDRIVER_MOBILE)
  private boolean isMobile;

  @Inject
  private WebElementUtils webElementUtils;

  /**
   * @return Url of the page, without domain part.
   */
  public abstract String getContentPath();

  /**
   * @return Title of the page.
   */
  public abstract String getPageTitle();

  /**
   * @return Full URL of the page which means: domain plus "content path".
   */
  public String getFullUrl() {
    return publishUrl + getContentPath();
  }

  /**
   * @return True if the page is displayed, false otherwise.
   * <br>
   * Assumes that the page is displayed when title of the currently displayed page
   * is the same as the title stored in this object.
   */
  public boolean isDisplayed() {
    return webElementUtils
        .isConditionMet(ExpectedConditions.titleIs(getPageTitle()), pageTitleTimeout);
  }

  /**
   * Opens the page in a new browser window.
   */
  public void open() {
    webDriver.get(getFullUrl());
  }

  /**
   * Removers '-mobile' from path if test does not run on mobile device.
   *
   * @param path path to adjust
   * @return correctly path
   */
  protected String adjustContentPath(String path) {
    return isMobile ? path : path.replaceFirst("-mobile", "");
  }

}
