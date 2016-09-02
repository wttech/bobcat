/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.aem.touch.siteadmin.aem61.list;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.touch.siteadmin.common.ActivationStatus;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.PageActivationStatus;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;

@PageObject
public class PageActivationStatusImpl implements PageActivationStatus {

  private static final String TITLE_ATTR = "title";

  private static final String NOT_PUBLISHED_STATUS_TITLE = "Not published";

  @CurrentScope
  @Inject
  private WebElement currentScope;

  @Inject
  private WebElementUtils webElementUtils;

  @FindBy(css = "span.date")
  private WebElement replicationActionDate;

  @FindBy(css = "span.user")
  private WebElement replicationActionUser;

  @FindBy(css = ".scheduledpublication-info")
  private WebElement scheduledPublicationInfo;

  @Override
  public String getReplicationActionDate() {
    if (getActivationStatus() == ActivationStatus.NOT_PUBLISHED) {
      throw new IllegalStateException(
          "Page is not activate thus it desn't contain such information");
    }
    return replicationActionDate.getText();
  }

  @Override
  public String getReplicationActionUser() {
    if (getActivationStatus() == ActivationStatus.NOT_PUBLISHED) {
      throw new IllegalStateException(
          "Page is not activated thus it doesn't contain such information");
    }
    return replicationActionUser.getText();
  }

  @Override
  public ActivationStatus getActivationStatus() {
    if (webElementUtils.isDisplayed(scheduledPublicationInfo, Timeouts.MINIMAL)) {
      return ActivationStatus.SCHEDULED;
    }
    return currentScope.getAttribute(TITLE_ATTR).contains(NOT_PUBLISHED_STATUS_TITLE)
        ? ActivationStatus.NOT_PUBLISHED
        : ActivationStatus.PUBLISHED;
  }
}
