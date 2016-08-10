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
package com.cognifide.qa.bb.aem.ui.wcm.constants;

import org.openqa.selenium.By;

/**
 * Contains all Site Admin buttons located on the ActionBar
 */
public enum SiteAdminButtons {
  REFRESH("button.x-btn-text.cq-siteadmin-refresh"),
  NEW("button.x-btn-text.cq-siteadmin-create-page-icon"),
  NEW_SITE(".cq-siteadmin-create-site"),
  COPY("table.x-btn.cq-siteadmin-copy.x-btn-noicon button.x-btn-text"),
  PASTE("table.x-btn.cq-siteadmin-paste.x-btn-noicon button.x-btn-text"),
  DELETE("table.x-btn.cq-siteadmin-delete.x-btn-noicon button.x-btn-text"),
  MOVE("table.x-btn.cq-siteadmin-move.x-btn-noicon button.x-btn-text"),
  ACTIVATE("table.x-btn.cq-siteadmin-activate.x-btn-noicon button.x-btn-text"),
  ACTIVATE_LATER(".cq-siteadmin-activate-later"),
  DEACTIVATE("table.x-btn.cq-siteadmin-deactivate.x-btn-noicon button.x-btn-text"),
  DEACTIVATE_LATER(".cq-siteadmin-deactivate-later");

  private final By.ByCssSelector locator;

  SiteAdminButtons(String locator) {
    this.locator = (By.ByCssSelector) By.cssSelector(locator);
  }

  /**
   * @return locator of the button
   */
  public By getLocator() {
    return locator;
  }
}
