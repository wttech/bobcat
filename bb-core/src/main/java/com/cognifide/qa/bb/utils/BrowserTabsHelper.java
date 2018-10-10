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
package com.cognifide.qa.bb.utils;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * This is helper class that provides methods for checking count and switching between opened web
 * browser tabs.
 */
@ThreadScoped
public final class BrowserTabsHelper {

  @Inject
  private WebDriver webDriver;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Checks if expected tabs count is opened with timeout defined in
   * {@link com.cognifide.qa.bb.constants.Timeouts#BIG} constant (in seconds).
   *
   * @param tabsCount expected opened tabs count.
   * @return value indicating if expected tabs count is opened.
   */
  public boolean isExpectedTabsCountOpened(int tabsCount) {
    return bobcatWait.isConditionMet(driver -> getOpenedTabsCount() == tabsCount);
  }

  /**
   * Gets the count of currently opened browser tabs.
   *
   * @return Count of currently opened browser tabs.
   */
  public int getOpenedTabsCount() {
    return webDriver.getWindowHandles().size();
  }

  /**
   * Switches to the next browser tab. If called when last tab is active, then switches to the
   * first one.
   */
  public void switchToNextTab() {
    switchToTab(1);
  }

  /**
   * Switches to the previous browser tab. If called when first tab is active, then switches to the
   * last one.
   */
  public void switchToPreviousTab() {
    switchToTab(-1);
  }

  /**
   * Switches to the browser tab with specified index offset from active tab. If resulting index
   * exceeds the index of first or last tab counting continues from another end.
   *
   * @param tabOffset tab index offset from active tab.
   */
  private void switchToTab(int tabOffset) {
    String currentTab = webDriver.getWindowHandle();
    List<String> openedTabs = new ArrayList<>(webDriver.getWindowHandles());
    if (openedTabs.size() > 1) {
      int currentTabIndex = openedTabs.indexOf(currentTab);
      int tabToOpenIndex = (openedTabs.size() + currentTabIndex + tabOffset) % openedTabs.size();
      webDriver.switchTo().window(openedTabs.get(tabToOpenIndex));
    } else {
      throw new IllegalStateException("There is no tab to switch");
    }
  }
}
