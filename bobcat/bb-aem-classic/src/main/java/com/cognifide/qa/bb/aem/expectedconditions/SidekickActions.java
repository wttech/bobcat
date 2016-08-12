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
package com.cognifide.qa.bb.aem.expectedconditions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.cognifide.qa.bb.constants.HtmlTags;

/**
 * Static methods that provides actions on Sidekick wrapped in ExpectedCondition.
 */
public final class SidekickActions {

  private static final String TAB_WRAPPER_XPATH = "./../../../..";

  private static final String TAB_ACTIVE = "x-tab-strip-active";

  private static final String SECTION_TOGGLE_CSS = ".x-tool-toggle";

  private static final String COLLAPSED_CLASS = "collapsed";

  private SidekickActions() {
  }

  /**
   * Shows sidekick tab and checks if its active one now.
   *
   * @param tab tab to show
   * @return condition for active tab
   */
  public static ExpectedCondition<Boolean> showSidekickTab(final WebElement tab) {
    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        tab.click();
        return tab.findElement(By.xpath(TAB_WRAPPER_XPATH)).getAttribute(HtmlTags.Attributes.CLASS)
            .contains(
                TAB_ACTIVE);
      }

      @Override
      public String toString() {
        return "Tab is not ready";
      }
    };
  }

  /**
   * Expands section of sidekick and checks if its expanded now.
   *
   * @param section section to expand
   * @return condition for expanded section
   */
  public static ExpectedCondition<Boolean> expandSection(final WebElement section) {
    return driver -> {
      if (isSectionExpanded(section)) {
        return Boolean.TRUE;
      }
      section.findElement(By.cssSelector(SECTION_TOGGLE_CSS)).click();
      return isSectionExpanded(section);
    };
  }

  /**
   * Expands fieldset of sidekick and checks if its expanded.
   *
   * @param fieldset fieldset to expand
   * @return condition for expanded fieldset
   */
  public static ExpectedCondition<Boolean> expandFieldset(final WebElement fieldset) {
    return driver -> {
      if (isFieldsetExpanded(fieldset)) {
        return Boolean.TRUE;
      }
      fieldset.findElement(By.cssSelector(SECTION_TOGGLE_CSS)).click();
      return isFieldsetExpanded(fieldset);
    };
  }

  /**
   * Checks if section in sidekick is expanded based on section WebElement
   *
   * @param section tested for expansion
   * @return true if sections is expanded, false if section is collapsed
   */
  public static boolean isSectionExpanded(WebElement section) {
    boolean isExpanded = false;
    if (!section.getAttribute(HtmlTags.Attributes.CLASS).contains(COLLAPSED_CLASS)) {
      isExpanded = true;
    }
    return isExpanded;
  }

  /**
   * Checks if fieldset in sidekick is expanded based on fieldset WebElement
   *
   * @param fieldset tested for expansion
   * @return true if sections is expanded, false if section is collapsed
   */
  public static boolean isFieldsetExpanded(WebElement fieldset) {
    boolean isExpanded = false;
    if (!fieldset.getAttribute(HtmlTags.Attributes.CLASS).contains(COLLAPSED_CLASS)) {
      isExpanded = true;
    }
    return isExpanded;
  }
}
