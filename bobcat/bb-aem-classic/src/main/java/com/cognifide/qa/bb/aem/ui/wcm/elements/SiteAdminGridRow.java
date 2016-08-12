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
package com.cognifide.qa.bb.aem.ui.wcm.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.ui.wcm.constants.ActivationStatus;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Exposes functionality of a single row in Siteadmin's grid
 */
@PageObject(css = "div.x-grid3-row")
public class SiteAdminGridRow {

  private static final String CELL = "div.x-grid3-col-";

  @Inject
  private Actions actions;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(css = CELL + "numberer")
  private WebElement pageNumber;

  @FindBy(css = CELL + "title")
  private WebElement title;

  @FindBy(css = CELL + "name")
  private WebElement name;

  @FindBy(css = CELL + "published > div.status")
  private WebElement published;

  @FindBy(css = CELL + "status > span")
  private WebElement status;

  @FindBy(css = CELL + "impressions > div")
  private WebElement impressions;

  @FindBy(css = CELL + "template")
  private WebElement template;

  /**
   * Returns number of the specified row.
   *
   * @return number of the row
   */
  public int getRowNumber() {
    String rowNumber = pageNumber.getText();
    return Integer.parseInt(rowNumber);
  }

  /**
   * Returns Title of the row
   *
   * @return title of the row
   */
  public String getTitle() {
    return title.getText();
  }

  /**
   * Returns Name of the row
   *
   * @return name of the row
   */
  public String getName() {
    return name.getText();
  }

  /**
   * Returns activation status based on the css classes in the Published column.
   *
   * @return ActivationStatus
   */
  public ActivationStatus getActivationStatus() {
    String className = published.getAttribute(HtmlTags.Attributes.CLASS);
    return ActivationStatus.getStatus(className);
  }

  /**
   * Returns impressions count of the row.
   *
   * @return impressions count of the row
   */
  public int getImpressions() {
    String impressionsCount = impressions.getText();
    return Integer.parseInt(impressionsCount);
  }

  /**
   * Returns template name of the row.
   *
   * @return template name of the row
   */
  public String getTemplateName() {
    return template.getText();
  }

  /**
   * Selects the row by clicking on the index column.
   *
   * @return This SiteAdminGridRow
   */
  public SiteAdminGridRow select() {
    pageNumber.click();
    return this;
  }

  /**
   * Selects and opens the page described by this row by double-clicking on the index column.
   */
  public void open() {
    select();
    actions.doubleClick(pageNumber).perform();
  }

  /**
   * Checks if the row is selected.
   *
   * @return true if selected
   */
  public boolean isSelected() {
    return currentScope.getAttribute(HtmlTags.Attributes.CLASS).contains("x-grid3-row-selected");
  }

  /**
   * Returns page status tooltip from the Status column.
   *
   * @return ToolTip body
   */
  public String getPageStatusToolTip() {
    return status.getAttribute(HtmlTags.Properties.OUTER_HTML);
  }
}
