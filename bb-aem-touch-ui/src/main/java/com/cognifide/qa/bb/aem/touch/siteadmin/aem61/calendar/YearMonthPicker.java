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

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import java.time.Month;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class YearMonthPicker {

  @FindBy(css = ".coral-Heading")
  @LoadableComponent(condClass = WaitForDynamicRedraw.class)
  private WebElement yearMonthHeader;

  @FindBy(css = ".coral-DatePicker-nextMonth")
  private WebElement nextMonthButton;

  public void selectDate(int year, Month month) {
    while (!isDesiredDateSelected(year, month)) {
      nextMonthButton.click();
    }
  }

  private boolean isDesiredDateSelected(int year, Month month) {
    String[] headerParts = yearMonthHeader.getText().toUpperCase().split(" ");
    Month selectedMonth = Month.valueOf(headerParts[0]);
    int selectedYear = Integer.parseInt(headerParts[1].toUpperCase());
    return year == selectedYear && selectedMonth == month;
  }
}
