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
package com.cognifide.qa.bb.aem.touch.siteadmin.aem62;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

@PageObject
public class NavigatorDropdown {

  private static final String PATH_ATTR = "data-granite-collection-navigator-collectionid";

  private static final String DROPDOWN_ITEMS_SELECTOR = "coral-popover.granite-collection-navigator coral-selectlist-item";

  @FindBy(css = "button.granite-breadcrumbs-button")
  private WebElement revealNavigatorDropdownBtn;

  @Inject
  private WebDriver driver;

  @Inject
  private BobcatWait wait;

  public void selectByPath(String path) {
    wait.withTimeout(Timeouts.SMALL).until(
        ExpectedConditions.presenceOfElementLocated(By.cssSelector(DROPDOWN_ITEMS_SELECTOR)));
    revealNavigatorDropdownBtn.click();
    getDropdownOptions().stream()
        .filter(webElement -> webElement.getAttribute(PATH_ATTR).equals(path))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException(
            String.format("Dropdown option not found using path \"%s\"", path)))
        .click();
  }

  private List<WebElement> getDropdownOptions() {
    return driver.findElements(By.cssSelector(DROPDOWN_ITEMS_SELECTOR));
  }

  public void selectByTitle(String title) {
    wait.withTimeout(Timeouts.SMALL).until(
        ExpectedConditions.presenceOfElementLocated(By.cssSelector(DROPDOWN_ITEMS_SELECTOR)));
    revealNavigatorDropdownBtn.click();
    getDropdownOptions().stream()
        .filter(webElement -> webElement.getText().equals(title))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException(
            String.format("Dropdown option not found using title \"%s\"", title)))
        .click();
  }

  public List<String> getAvailablePaths() {
    wait.withTimeout(Timeouts.SMALL).until(
        ExpectedConditions.presenceOfElementLocated(By.cssSelector(DROPDOWN_ITEMS_SELECTOR)));
    return getDropdownOptions().stream()
        .map(webElement -> webElement.getAttribute(PATH_ATTR))
        .collect(Collectors.toList());
  }

  public List<String> getAvailableTitles() {
    wait.withTimeout(Timeouts.SMALL).until(
        ExpectedConditions.presenceOfElementLocated(By.cssSelector(DROPDOWN_ITEMS_SELECTOR)));
    return getDropdownOptions().stream()
        .map(webElement -> webElement.getAttribute(HtmlTags.Properties.INNER_HTML))
        .collect(Collectors.toList());
  }
}
