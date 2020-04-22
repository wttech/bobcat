/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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

import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.cognifide.qa.bb.wait.TimingsBuilder;
import com.google.inject.Inject;

/**
 * Default implementation of {@link ContentFragmentPathBrowser}
 */
@PageObject(css = Locators.AUTOCOMPLETE_CSS)
public class DefaultContentFragmentPathBrowser implements ContentFragmentPathBrowser {

  @FindBy(css = ".coral3-Textfield")
  private WebElement input;

  @FindBy(xpath = Locators.ALTERNATE_LABEL_XPATH)
  private List<WebElement> label;

  @FindBy(css = ".foundation-picker-buttonlist.coral3-Overlay.is-open")
  private WebElement firstResult;

  @Global
  @FindBy(css = ".coral3-Dialog--warning.is-open .coral3-Button--primary")
  private WebElement warningConfirmation;

  @Inject
  private BobcatWait bobcatWait;

  @Override
  public void setValue(Object value) {
    input.clear();
    input.sendKeys(String.valueOf(value));
    bobcatWait.until(elementToBeClickable(firstResult));
    input.sendKeys(Keys.ENTER);
    closeWarningDialogIfRequired();
  }

  @Override
  public String getLabel() {
    return label.isEmpty() ? "" : label.get(0).getText();
  }

  //  The warning dialog doesn't always appear, so it's handled within an if-clause
  private void closeWarningDialogIfRequired() {
    if (bobcatWait
        .tweak(new TimingsBuilder().explicitTimeout(1).build())
        .ignoring(NoSuchElementException.class)
        .isConditionMet(elementToBeClickable(warningConfirmation))) {
      warningConfirmation.click();
      bobcatWait.tweak(new TimingsBuilder().explicitTimeout(1).build())
          .isConditionMet(not(elementToBeClickable(warningConfirmation)));
    }
  }
}
