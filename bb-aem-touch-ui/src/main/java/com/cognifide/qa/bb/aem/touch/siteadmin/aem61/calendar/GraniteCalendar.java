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

import java.time.LocalDateTime;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.Loadable;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.CurrentScopeHelper;
import com.google.inject.Inject;

@PageObject
public class GraniteCalendar implements Loadable {

  @Inject
  private CurrentScopeHelper currentScopeHelper;

  @Inject
  private BobcatWait wait;

  @FindBy(css = ".coral-DatePicker-calendarHeader")
  private YearMonthPicker yearMonthPicker;

  @FindBy(css = ".coral-DatePicker-timeControls")
  private TimePicker timePicker;

  @FindBy(css = ".coral-DatePicker-calendarBody > table > tbody > tr > td > a")
  private List<WebElement> daysOnCalendar;

  public void selectDateAndTime(LocalDateTime localDateTime) {
    yearMonthPicker.selectDate(localDateTime.getYear(), localDateTime.getMonth());
    wait.withTimeout(Timeouts.MEDIUM).until(input -> isLoaded(), Timeouts.MINIMAL);
    timePicker.selectTime(localDateTime.getHour(), localDateTime.getMinute());
    selectDay(localDateTime.getDayOfMonth());
  }

  private void selectDay(int dayOfMonth) {
    String day = String.valueOf(dayOfMonth);
    daysOnCalendar.stream().filter(t -> StringUtils.equals(t.getText(), day)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("No clickable day of month %s found in the current context", day)))
        .click();
  }

  public void closeCalendar() {
    currentScope.sendKeys(Keys.ESCAPE);
  }

  @Override
  public boolean isLoaded() {
    return currentScopeHelper.isCurrentScopeVisible(this);
  }

}
