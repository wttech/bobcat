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
package com.cognifide.qa.bb.aem.touch.siteadmin.aem62.calendar;

import java.time.LocalDateTime;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.touch.siteadmin.common.Loadable;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class CoralCalendar implements Loadable {

  @Inject
  private WebDriver driver;

  @Inject
  private BobcatWait wait;

  @FindBy(css = ".coral-Calendar-calendarHeader")
  private YearMonthPicker yearMonthPicker;

  @FindBy(css = "coral-clock.coral-Clock")
  private TimePicker timePicker;

  public void selectDateAndTime(LocalDateTime localDateTime) {
    yearMonthPicker.selectDate(localDateTime.getYear(), localDateTime.getMonth());
    wait.withTimeout(Timeouts.MEDIUM).until(input -> currentScope.isDisplayed(), 3);
    timePicker.chooseTime(localDateTime.getHour(), localDateTime.getMinute());
    chooseDay(localDateTime.getDayOfMonth());
    closeCalendar();
  }

  @Override
  public boolean isLoaded() {
    return currentScope.isDisplayed();
  }

  private void chooseDay(int dayOfMonth) {
    String day = String.valueOf(dayOfMonth);
    List<WebElement> daysToSelect = driver.findElements(
        By.cssSelector(".coral-Calendar-calendarBody > table > tbody > tr > td > a"));
    daysToSelect.stream().filter(t -> StringUtils.equals(t.getText(), day)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            "No clickable day of month found in the current context"))
        .click();
  }

  private void closeCalendar() {
    currentScope.sendKeys(Keys.ESCAPE);
  }

}
