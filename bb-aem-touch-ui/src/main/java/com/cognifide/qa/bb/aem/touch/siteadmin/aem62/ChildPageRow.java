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

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.touch.siteadmin.common.PageActivationStatus;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.PageModificationInfo;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.SiteadminChildPage;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class ChildPageRow implements SiteadminChildPage {

  private static final int SELECT_PAGE_COLUMN_INDEX = 0;

  private static final String PATH_ATTR = "data-foundation-collection-item-id";

  @FindBy(css = "td.coral-Table-cell:nth-of-type(4)")
  private PageActivationStatusImpl pageActivationStatus;

  @FindBy(css = "td.coral-Table-cell:nth-of-type(3)")
  private PageModificationInfoImpl pageModificationInfo;

  @FindBy(css = "td")
  private List<WebElement> cells;

  @FindBy(css = "td.foundation-collection-item-title")
  private WebElement titleCell;

  @Override
  public ChildPageRow select() {
    cells.get(SELECT_PAGE_COLUMN_INDEX).click();
    return this;
  }

  @Override
  public String getTitle() {
    return titleCell.getAttribute("value");
  }

  @Override
  public String getHref() {
    return currentScope.getAttribute(PATH_ATTR);
  }

  @Override
  public PageModificationInfo getModificationInfo() {
    return pageModificationInfo;
  }

  @Override
  public PageActivationStatus getPageActivationStatus() {
    return pageActivationStatus;
  }

  @Override
  public void click() {
    currentScope.click();
  }

}
