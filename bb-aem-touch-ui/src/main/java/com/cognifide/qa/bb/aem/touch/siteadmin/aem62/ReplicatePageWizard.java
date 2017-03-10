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
package com.cognifide.qa.bb.aem.touch.siteadmin.aem62;

import java.time.LocalDateTime;

import com.cognifide.qa.bb.aem.touch.siteadmin.aem62.calendar.CoralCalendar;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.IsLoadedCondition;
import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class ReplicatePageWizard {

  @Global
  @FindBy(css = "button.foundation-wizard-control[type='submit']")
  private WebElement submitBtn;

  @FindBy(css = "button.coral-Button.coral-Button--square[icon='calendar']")
  private WebElement calendarButton;

  @Global
  @FindBy(css = "coral-popover.coral3-Popover")
  @LoadableComponent(condClass = IsLoadedCondition.class)
  private CoralCalendar calendar;

  public void selectDateAndTime(LocalDateTime dateTime) {
    if (dateTime.isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Selected date and time should be placed in future");
    }

    calendarButton.click();
    calendar.selectDateAndTime(dateTime);
  }

  public void submit() {
    submitBtn.click();
  }

}
