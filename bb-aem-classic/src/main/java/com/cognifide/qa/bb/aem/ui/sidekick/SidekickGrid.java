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
package com.cognifide.qa.bb.aem.ui.sidekick;

import java.util.List;

import com.cognifide.qa.bb.qualifier.FindPageObject;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;

@Frame("/")
@PageObject
public class SidekickGrid {

  @FindPageObject
  private List<SidekickGridRow> gridRows;

  /**
   * Get grid row by row number
   *
   * @param rowNumber row index
   * @return SidekickGridRow sidekick row
   */
  public SidekickGridRow getGridRow(int rowNumber) {
    return gridRows.get(rowNumber);
  }

  /**
   * Get grid row by row content
   *
   * @param rowContent row of content
   * @return SidekickGridRow sidekick row
   */
  public SidekickGridRow getGridRow(String rowContent) {
    for (SidekickGridRow row : gridRows) {
      List<WebElement> cells = row.getCells();
      for (WebElement cell : cells) {
        if (cell.getText().equals(rowContent)) {
          return row;
        }
      }
    }
    throw new IllegalArgumentException(
        "There is no row with content: " + rowContent);
  }
}
