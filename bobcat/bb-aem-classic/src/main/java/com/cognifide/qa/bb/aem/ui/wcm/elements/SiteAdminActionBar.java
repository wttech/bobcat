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
package com.cognifide.qa.bb.aem.ui.wcm.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.ui.wcm.constants.SiteAdminButtons;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.CurrentScopeHelper;
import com.google.inject.Inject;

@PageObject(css = "td.x-toolbar-left")
public class SiteAdminActionBar {

  private static final String DISABLED_BUTTON_CLASS = "x-item-disabled";

  private static final String BUTTON_TABLE_PARENT_XPATH = "./../../../../..";

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Inject
  private WebDriver webDriver;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private CurrentScopeHelper webElementHelper;

  @Inject
  private Actions actions;

  /**
   * Clicks on one of the Action Bar's buttons.
   *
   * @param button selected button from SiteadminButtons
   * @return this SiteadminActionBar
   */
  public SiteAdminActionBar clickOnButton(SiteAdminButtons button) {
    getButton(button).click();
    return this;
  }

  /**
   * Clicks on one of the Action Bar's buttons, and wait's for desired page object.
   *
   * @param button     button on Siteadmin action bar
   * @param pageObject desired page object
   * @return this SiteadminActionBar
   */
  public SiteAdminActionBar clickOnButton(final SiteAdminButtons button, final Object pageObject) {
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      getButton(button).click();
      return webElementHelper.isCurrentScopeVisible(pageObject);
    }, 2);
    return this;
  }

  /**
   * Expands drop down menu related with action bar button
   *
   * @param button action bar button
   * @return this SiteAdminActionBar
   */
  public SiteAdminActionBar expandDropDown(SiteAdminButtons button) {
    actions.moveToElement(getButton(button)).perform();
    actions.moveByOffset(30, 0).perform();
    actions.click().perform();
    return this;
  }

  /**
   * Waits for the ActionBar to be displayed.
   *
   * @return this SiteadminActionBar
   */
  public SiteAdminActionBar waitToBeDisplayed() {
    bobcatWait.withTimeout(Timeouts.BIG).until(ExpectedConditions.visibilityOf(currentScope));
    return this;
  }

  /**
   * Verify of button on Siteadmin action bar is enabled
   *
   * @param button button on Siteadmin action bar
   * @return true if button is enabled
   */
  public boolean isButtonEnabled(SiteAdminButtons button) {
    return !getButton(button).findElement(By.xpath(BUTTON_TABLE_PARENT_XPATH))
        .getAttribute(HtmlTags.Attributes.CLASS).contains(DISABLED_BUTTON_CLASS);
  }

  /**
   * Clicks on button in dropdown on Siteadmin action bar
   *
   * @param button button in drop down
   */
  public void clickDropDownOption(SiteAdminButtons button) {
    webDriver.findElement(button.getLocator()).click();
  }

  private WebElement getButton(SiteAdminButtons button) {
    return currentScope.findElement(button.getLocator());
  }
}
