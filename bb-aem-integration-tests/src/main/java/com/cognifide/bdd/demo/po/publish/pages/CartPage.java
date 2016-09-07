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
package com.cognifide.bdd.demo.po.publish.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.page.PublishPage;
import com.cognifide.qa.bb.logging.ReportEntryLogger;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class CartPage extends PublishPage {

  private static final String CONTENT_PATH = "/content/geometrixx-outdoors/en/user/cart.html";

  private static final String TITLE = "My Cart";

  @Inject
  private ReportEntryLogger reportEntryLogger;

  @FindBy(css = "div.recommendations-default")
  private WebElement defaultRecommendationsElement;

  @FindBy(css = "p.cq-checkout a")
  private WebElement checkoutButton;

  @Inject
  private BobcatWait bobcatWait;

  @Override
  public String getContentPath() {
    return adjustContentPath(CONTENT_PATH);
  }

  @Override
  public String getPageTitle() {
    return TITLE;
  }

  public boolean recommendationsAreLoaded() {
    boolean replacedWithAjaxCall = !defaultRecommendationsElement.isDisplayed();
    reportEntryLogger
        .info("checking if recommendations were loaded with AJAX: '{}'", replacedWithAjaxCall);
    return replacedWithAjaxCall;
  }

  public void checkout() {
    bobcatWait.withTimeout(Timeouts.MEDIUM)
        .until(driver -> ExpectedConditions.elementToBeClickable(checkoutButton));
    reportEntryLogger.info("checkout link href attribute: '{}'",
        checkoutButton.getAttribute(HtmlTags.Attributes.HREF));
    //clicking on an anchor tag is sometimes problematic when using WebElement#click
    ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", checkoutButton);
    reportEntryLogger.info("checkout link clicked");
  }

}
