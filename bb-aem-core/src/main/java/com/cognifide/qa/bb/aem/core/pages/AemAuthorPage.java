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
package com.cognifide.qa.bb.aem.core.pages;

import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;
import io.qameta.allure.Step;
import javax.inject.Named;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Interface that marks page as being from AEM
 */
public abstract class AemAuthorPage {

  @Inject
  protected WebElementUtils webElementUtils;

  @Inject
  protected WebDriver webDriver;

  @Inject
  @Named("author.url")
  protected String authorUrl;

  @Inject
  @Named("page.title.timeout")
  protected int pageTitleTimeout;

  /**
   * open the page in browser
   */
  @Step("Open page")
  public AemAuthorPage open() {
    webDriver.get(authorUrl + getFullUrl());
    return this;
  }

  /**
   * return full url
   */
  public abstract String getFullUrl();

  /**
   * return title
   */
  public abstract String getTitle();

  public boolean isDisplayed() {
    return webElementUtils
        .isConditionMet(ExpectedConditions.titleIs(getTitle()), pageTitleTimeout);
  }
}
