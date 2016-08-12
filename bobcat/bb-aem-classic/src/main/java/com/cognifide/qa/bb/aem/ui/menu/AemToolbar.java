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
package com.cognifide.qa.bb.aem.ui.menu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.XpathUtils;

/**
 * Class representing component toolbar. To open a dialog user has to click edit button.
 */
@PageObject
@Frame("$cq")
public class AemToolbar {

  @FindBy(css = ".x-toolbar.x-small-editor.x-toolbar-layout-ct")
  private WebElement toolbar;

  /**
   * Clicks the "edit" button.
   *
   * @return This AemToolbar instance.
   */
  public AemToolbar edit() {
    click(MenuOption.EDIT);
    return this;
  }

  /**
   * Clicks the "delete" button.
   *
   * @return This AemToolbar instance.
   */
  public AemToolbar delete() {
    click(MenuOption.DELETE);
    return this;
  }

  /**
   * Clicks the "new" button.
   *
   * @return This AemToolbar instance.
   */
  public AemToolbar newComponent() {
    click(MenuOption.NEW);
    return this;
  }

  /**
   * Clicks the selected toolbar item.
   *
   * @param menuOption options in context menu
   */
  public void click(MenuOption menuOption) {
    toolbar
        .findElement(
                By.xpath(String.format(".//button[text()=%s]",
                        XpathUtils.quote(menuOption.getLabel())))).click();
  }

  /**
   * Clicks the selected submenu item.
   *
   * @param menuOption options in context menu
   */
  public void clickSubmenu(MenuOption menuOption) {
    toolbar.findElement(
            By.xpath(String.format("//span[text()=%s]", XpathUtils.quote(menuOption.getLabel()))))
        .click();
  }

  /**
   * @return True if the toolbar is displayed, false otherwise.
   */
  public boolean isToolbarVisible() {
    return toolbar.isDisplayed();
  }
}
