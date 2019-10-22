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
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields;

import static org.apache.commons.lang3.StringUtils.contains;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * This class represents path browser dialog field.
 */
@PageObject
public class PathBrowser implements DialogField {

  private static final String TEXT_FIELD_CLASS = "coral3-Textfield";

  @FindBy(className = TEXT_FIELD_CLASS)
  private WebElement input;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Sets path in path browser.
   *
   * @param value string path value.
   */
  @Override
  public void setValue(Object value) {
    if (contains(currentScope.getAttribute(HtmlTags.Attributes.CLASS), TEXT_FIELD_CLASS)) {
      currentScope.clear();
      currentScope.sendKeys(String.valueOf(value));
    } else {
      input.clear();
      input.sendKeys(String.valueOf(value));
    }
    bobcatWait.until(ExpectedConditions.visibilityOfElementLocated(
        By.cssSelector(".foundation-picker-buttonlist.coral3-Overlay.is-open")));
    currentScope.findElement(By.className("coral-Form-fieldlabel")).click();
  }
}
