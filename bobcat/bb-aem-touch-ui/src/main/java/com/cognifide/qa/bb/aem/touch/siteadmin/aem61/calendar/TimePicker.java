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
package com.cognifide.qa.bb.aem.touch.siteadmin.aem61.calendar;

import com.cognifide.qa.bb.qualifier.PageObject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

@PageObject
public class TimePicker {

  @FindBy(css = "div.coral-DatePicker-hour > select.coral-Select-select")
  private WebElement hourSelectElement;

  @FindBy(css = "div.coral-DatePicker-minute > select.coral-Select-select")
  private WebElement minuteSelectElement;

  public void selectTime(int hour, int minute) {
    Select hourSelect = new Select(hourSelectElement);
    Select minuteSelect = new Select(minuteSelectElement);
    hourSelect.selectByVisibleText(String.format("%02d", hour));
    minuteSelect.selectByVisibleText(String.format("%02d", minute));
  }

}
