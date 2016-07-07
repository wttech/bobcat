package com.cognifide.qa.bb.aem.ui.sidekick;

/*-
 * #%L
 * Bobcat Parent
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


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@Frame("/")
@PageObject
public class SidekickGridRow {

  private static final String ROW_SELECTED_CLASS = "row-selected";

  private static final String GRID_CHECKBOX_CSS = ".x-grid3-row-checker";

  private static final String GRID_CELLS_CSS = "div.x-grid3-cell-inner";

  private static final String CLASS_ATTRIBUTE = "class";

  @Inject
  @CurrentScope
  private WebElement gridRow;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Checks if checkbox in AemSidekick grid row is selected
   *
   * @return true if checkbox is selected, false if checkbox is not selected
   */
  public boolean isGridRowCheckboxSelected() {
    if (gridRow.getAttribute(CLASS_ATTRIBUTE).contains(ROW_SELECTED_CLASS)) {
      return true;
    }
    return false;
  }

  /**
   * Selects checbox in AemSidekick grid based
   *
   * @return instance of AemSidekick
   */
  public SidekickGridRow selectGridCheckbox() {
    if (!isGridRowCheckboxSelected()) {
      bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
        gridRow.findElement(By.cssSelector(GRID_CHECKBOX_CSS)).click();
        return isGridRowCheckboxSelected();
      }, 5);
    }
    return this;
  }

  /**
   * Get cells from grid row
   *
   * @return List of row cells
   */
  public List<WebElement> getCells() {
    return gridRow.findElements(By.cssSelector(GRID_CELLS_CSS));
  }
}
