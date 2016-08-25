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

import com.cognifide.qa.bb.aem.touch.siteadmin.common.PageActivationStatus;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.PageModificationInfo;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.SiteadminChildPage;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class ChildPageRow implements SiteadminChildPage {

  private static final int SELECT_PAGE_COLUMN_INDEX = 0;

  @CurrentScope
  @Inject
  private WebElement currentScope;

  @Inject
  private WebElementUtils webElementUtils;

  @FindBy(css = "td.coral-Table-cell:nth-of-type(4)")
  private PageActivationStatusImpl pageActivationStatus;

  @FindBy(css = "td.coral-Table-cell:nth-of-type(3)")
  private PageModificationInfoImpl pageModificationInfo;

  @FindBy(tagName = "a")
  private WebElement anchor;

  @FindBy(css = "td")
  private List<WebElement> cells;

  @FindBy(css = "td.foundation-collection-item-title")
  private WebElement titleCell;

  public ChildPageRow select() {
    cells.get(SELECT_PAGE_COLUMN_INDEX).click();
    return this;
  }

  public String getTitle() {
    return titleCell.getAttribute("value");
  }

  public String getHref() {
    return anchor.getAttribute("href");
  }

  @Override public PageModificationInfo getModificationInfo() {
    return pageModificationInfo;
  }

  @Override public PageActivationStatus getPageActivationStatus() {
    return pageActivationStatus;
  }

  public void click() {
    currentScope.click();
  }

}
