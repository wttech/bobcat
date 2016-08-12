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
package com.cognifide.qa.bb.aem.ui.messages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@Frame("/")
@PageObject
public class AemBubbleMessage {

  private static final By BUBBLE_MSG_BY_SELECTOR = By.cssSelector(
      "#cq-notification-div > div[style*=static] .cq-notification-msg-content");

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private WebDriver webDriver;

  /**
   * Waits for presence of notification message in author mode
   *
   * @return This AemBubbleMessage instance
   */
  public AemBubbleMessage waitForAemBubbleMessage() {
    bobcatWait.withTimeout(Timeouts.MEDIUM)
        .until(ExpectedConditions.presenceOfElementLocated(BUBBLE_MSG_BY_SELECTOR));
    return this;
  }

  /**
   * Get's notification message text
   *
   * @return notification message text
   */
  public String getBubbleMessageText() {
    return webDriver.findElement(BUBBLE_MSG_BY_SELECTOR).getText();
  }
}
