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
 * Static methods that provides actions on Content Finder wrapped in ExpectedCondition.
 */
public final class ContentFinderActions {

  private static final String TAB_ACTIVE = "x-tab-strip-active";

  private static final String VIEW_ACTIVE = "x-btn-pressed";

  private static final String VIEW_WRAPPER_XPATH = "./../../../../..";

  private static final String COLLAPSE_BUTTON_CSS = "#cq-cf-west-xsplit div";

  private static final String EXPAND_BUTTON_CSS = "#cq-cf-west-xcollapsed div";

  private static final String VIEW_IS_NOT_READY = "View is not ready";

  private ContentFinderActions() {
  }

  /**
   * Clicks contentFinder tab and checks if it is active.
   *
   * @param tab tab to be showed
   * @return condition for tab to be active
   */
  public static ExpectedCondition<Boolean> showContentFinderTab(final WebElement tab) {
    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        tab.click();
        return tab.getAttribute(HtmlTags.Attributes.CLASS).contains(
            TAB_ACTIVE);
      }

      @Override
      public String toString() {
        return "Tab is not ready";
      }
    };
  }

  /**
   * Clicks contentFinder view and checks is it the active one now.
   *
   * @param view view to be showed
   * @return condition for the view to be active
   */
  public static ExpectedCondition<Boolean> showContentFinderView(final WebElement view) {
    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        view.click();
        return view.findElement(By.xpath(VIEW_WRAPPER_XPATH))
            .getAttribute(HtmlTags.Attributes.CLASS)
            .contains(VIEW_ACTIVE);
      }

      @Override
      public String toString() {
        return String.format(VIEW_IS_NOT_READY);
      }
    };
  }

  /**
   * Collapses content finder and checks if collapse button hides.
   *
   * @return condition for content finder to be collapsed
   */
  public static ExpectedCondition<Boolean> collapse() {
    return driver -> {
      WebElement collapseButton = driver.findElement(By.cssSelector(COLLAPSE_BUTTON_CSS));
      collapseButton.click();
      return !collapseButton.isDisplayed();
    };
  }

  /**
   * Expands content finder and checks if expand button hides.
   *
   * @return condition for content finder to be expanded
   */
  public static ExpectedCondition<Boolean> expand() {
    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        WebElement expandButton = driver.findElement(By.cssSelector(EXPAND_BUTTON_CSS));
        expandButton.click();
        return !expandButton.isDisplayed();
      }

      @Override
      public String toString() {
        return String.format(VIEW_IS_NOT_READY);
      }
    };
  }
}
