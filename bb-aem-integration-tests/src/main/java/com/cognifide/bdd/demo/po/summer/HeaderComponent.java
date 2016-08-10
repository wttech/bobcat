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
package com.cognifide.bdd.demo.po.summer;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class HeaderComponent {
  @FindBy(tagName = "span")
  private List<WebElement> sectionHeader;

  @FindBy(tagName = "span")
  @Global
  private List<WebElement> spans;

  public List<WebElement> getSectionHeader() {
    return sectionHeader;
  }

  public List<WebElement> getSpans() {
    return spans;
  }

  public int getSectionHeaderListSize() {
    return sectionHeader.size();
  }

  public int getSpansSize() {
    return spans.size();
  }
}
