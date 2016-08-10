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
package com.cognifide.bdd.demo.tabs;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.utils.BrowserTabsHelper;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class BrowserTabsTest {

  @Inject
  private WebDriver webDriver;

  @Inject
  private BrowserTabsHelper tabsHelper;

  @Inject
  private WebElementUtils webElementUtils;

  @Test
  public void switchBrowserTabs() {
    webDriver.navigate().to(String.format("file://%s",
        new File("src/test/resources/tabs/tab_0.html").getAbsoluteFile()));

    WebElement tabLink = webDriver.findElement(By.id("tabLink"));

    assertTrue("Link1 is not visible", webElementUtils.isDisplayed(tabLink));

    tabLink.click();

    assertTrue("Incorrect opened tabs count", tabsHelper.isExpectedTabsCountOpened(2));

    WebElement tabName = webDriver.findElement(By.id("tabName"));
    assertTrue("Tab0 is not opened",
        webElementUtils.isTextPresentInWebElement(tabName, "Tab0", Timeouts.SMALL));

    tabsHelper.switchToNextTab();
    tabName = webDriver.findElement(By.id("tabName"));
    assertTrue("Tab1 is not opened",
        webElementUtils.isTextPresentInWebElement(tabName, "Tab1", Timeouts.SMALL));

    tabsHelper.switchToNextTab();
    tabName = webDriver.findElement(By.id("tabName"));
    assertTrue("Tab0 is not opened",
        webElementUtils.isTextPresentInWebElement(tabName, "Tab0", Timeouts.SMALL));

    tabsHelper.switchToPreviousTab();
    tabName = webDriver.findElement(By.id("tabName"));
    assertTrue("Tab1 is not opened",
        webElementUtils.isTextPresentInWebElement(tabName, "Tab1", Timeouts.SMALL));
  }
}
