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
package com.cognifide.bdd.demo.po.frame;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
@Frame("main-frame-1")
public class FrameWithName {

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(id = "frame-title")
  private SubFrame11 subFrame11;

  @FindBy(id = "frame-subtitle")
  private WebElement subTitle;

  public SubFrame11 getSubFrame11() {
    return subFrame11;
  }

  public String getAttribute(String attribute) {
    return currentScope.getAttribute(attribute);
  }

  public String getSubTitleText() {
    return subTitle.getText();
  }
}
