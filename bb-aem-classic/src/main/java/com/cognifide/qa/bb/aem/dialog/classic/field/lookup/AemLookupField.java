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
package com.cognifide.qa.bb.aem.dialog.classic.field.lookup;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.DialogComponent;
import com.cognifide.qa.bb.aem.dialog.classic.field.AbstractTextInput;
import com.cognifide.qa.bb.aem.dialog.classic.field.Configurable;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.CurrentScopeHelper;
import com.google.inject.Inject;

/**
 * Lookup field in a AemDialog.
 */
@PageObject
@Frame("$cq")
@DialogComponent("path field")
public class AemLookupField extends AbstractTextInput<AemLookupField> implements Configurable {

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private CurrentScopeHelper webElementHelper;

  @FindBy(xpath = ".//input")
  private WebElement field;

  @FindBy(css = "img.x-form-search-trigger")
  private WebElement lookupButton;

  @Global
  @FindBy(css = "div.x-window.cq-browsedialog[id^='ext-comp-'][style*='visibility: visible']")
  private AemPathWindow pathWindow;

  /**
   * Perform click at lookup button and get newly opened
   * {@link AemPathWindow}.
   *
   * @return pageObject - {@link AemPathWindow}
   */
  public AemPathWindow openPathWindow() {
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      lookupButton.click();
      return webElementHelper.isCurrentScopeVisible(pathWindow);
    }, 5);
    return pathWindow;
  }

  @Override
  protected WebElement getField() {
    return field;
  }

  @Override
  public void setValue(String value) {
    openPathWindow();
    pathWindow.getContentTree().selectPath(value);
    pathWindow.clickOk();
  }
}
