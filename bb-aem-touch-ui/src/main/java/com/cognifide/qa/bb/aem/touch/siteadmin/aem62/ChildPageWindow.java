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

import com.cognifide.qa.bb.aem.touch.siteadmin.common.Loadable;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class ChildPageWindow implements Loadable {

  @FindBy(css = "tr.foundation-collection-item.foundation-collection-navigator")
  private List<ChildPageRow> childPageRows;

  @FindBy(css = "coral-checkbox.coral-Checkbox")
  private WebElement selectAllPagesButton;

  public ChildPageRow getChildPageRow(String title) {
    return childPageRows.stream().filter(t -> t.getTitle().equals(title)).findFirst().
        orElseThrow(() -> new IllegalStateException("Page not found in the current context"));
  }

  public ChildPageRow selectPage(String title) {
    return getChildPageRow(title).select();
  }

  public void pressSelectAllPages() {
    selectAllPagesButton.click();
  }

  @Override
  public boolean isLoaded() {
    return currentScope.isDisplayed();
  }

  public boolean containsPage(String title) {
    return !childPageRows.isEmpty() && childPageRows.stream()
        .filter(t -> t.getTitle().equals(title)).findFirst().isPresent();
  }

  public int getPageCount() {
    return childPageRows.size();
  }

  public boolean hasSubPages() {
    return !childPageRows.isEmpty();
  }

  public List<ChildPageRow> getChildPageRows() {
    return childPageRows;
  }
}
