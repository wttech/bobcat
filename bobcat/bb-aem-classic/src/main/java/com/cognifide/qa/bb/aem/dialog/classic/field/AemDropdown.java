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
package com.cognifide.qa.bb.aem.dialog.classic.field;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.DialogComponent;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;

/**
 * Represents a dropdown control in a AemDialog.
 */
@PageObject
@Frame("$cq")
@DialogComponent("dropdown")
public class AemDropdown implements Configurable {

  private static final String ITEMS_LOCATOR =
      "div.x-combo-list[style*='visible'] > div > div.x-combo-list-item";

  private static final String ITEM_BY_TEXT_PARTIAL_LOCATOR =
      "//div[contains(@class, 'x-combo-list') and contains(@style, 'visible')]/div";

  @Inject
  private WebDriver webDriver;

  @FindBy(xpath = ".//input[@type='text']")
  private WebElement thisElement;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Clicks the item in the dropdown list. Item is identified by the provided index. First item has
   * index 0.
   *
   * @param index starts from 0
   * @return this Aem dropdown object
   */
  public AemDropdown selectByIndex(int index) {
    expandDropdown();
    List<WebElement> items = bobcatWait.withTimeout(Timeouts.MEDIUM).until(
        ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(ITEMS_LOCATOR)));
    items.get(index).click();
    return this;
  }

  /**
   * Opens the dropdown and clicks the item that has text equal to the provided parameter.
   *
   * @param text text for the option label
   * @return this AEM dropdown object
   */
  public AemDropdown selectByText(String text) {
    if (text == null) {
      return this;
    }
    expandDropdown();
    getItemByTextEquals(text).click();
    return this;
  }

  /**
   * Opens the dropdown and clicks the item that contains the provided text.
   *
   * @param text text that is contained by option label
   * @return this AEM dropdown object
   */
  public AemDropdown selectByPartialText(String text) {
    if (text == null) {
      return this;
    }
    expandDropdown();
    getItemByTextContaining(text).click();
    return this;
  }

  @Override
  public void setValue(String value) {
    selectByText(value);
  }

  private WebElement getItemByTextContaining(String text) {
    String itemXpath = ITEM_BY_TEXT_PARTIAL_LOCATOR
        + String.format("/div[contains(text(), %s)]", XpathUtils.quote(text));
    return webDriver.findElement(By.xpath(itemXpath));
  }

  private WebElement getItemByTextEquals(String text) {
    String itemXpath =
        ITEM_BY_TEXT_PARTIAL_LOCATOR + String.format("/div[text()=%s]", XpathUtils.quote(text));
    return webDriver.findElement(By.xpath(itemXpath));
  }

  private void expandDropdown() {
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      thisElement.click();
      return !webDriver.findElements(By.cssSelector(ITEMS_LOCATOR)).isEmpty();
    }, 5);
  }
}
