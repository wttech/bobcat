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
package com.cognifide.qa.bb.aem.ui.wcm.windows;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

/**
 * Class responsible for handling activate and deactivate later dialog in siteadmin.
 */
@PageObject
public class ReplicateLaterWindow implements DecisionWindow {

  @FindBy(xpath = ".//td[@class='ux-datetime-date']//input")
  private WebElement dateField;

  @FindBy(xpath = ".//td[@class='ux-datetime-time']//input")
  private WebElement timeField;

  @FindBy(xpath = ".//button[contains(text(), 'OK')]")
  private WebElement okButton;

  @FindBy(xpath = ".//button[contains(text(), 'Cancel')]")
  private WebElement cancelButton;

  /**
   * Puts date in date field
   * @param date date
   * @return this ReplicateLaterWindow
   */
  public ReplicateLaterWindow fillDay(String date) {
    dateField.sendKeys(date);
    return this;
  }

  /**
   * Puts time in time field
   * @param time time
   * @return this ReplicateLaterWindow
   */
  public ReplicateLaterWindow fillTime(String time) {
    timeField.sendKeys(time);
    return this;
  }

  @Override
  public void confirm() {
    okButton.click();
  }

  @Override
  public void cancel() {
    cancelButton.click();
  }
}
