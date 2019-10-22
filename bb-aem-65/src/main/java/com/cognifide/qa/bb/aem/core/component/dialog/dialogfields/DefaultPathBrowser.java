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

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Default implementation of {@link PathBrowser}
 */
@PageObject(css = Locators.AUTOCOMPLETE_CSS)
public class DefaultPathBrowser implements PathBrowser {

  @FindBy(css = ".coral3-Textfield")
  private WebElement input;

  @FindBy(xpath = Locators.ALTERNATE_LABEL_XPATH)
  private List<WebElement> label;

  @FindBy(css = ".foundation-picker-buttonlist.coral3-Overlay.is-open")
  private WebElement firstResult;

  @Inject
  private BobcatWait bobcatWait;

  @Override
  public void setValue(Object value) {
    input.clear();
    input.sendKeys(String.valueOf(value));
    bobcatWait.until(elementToBeClickable(firstResult));
    input.sendKeys(Keys.ENTER);
  }

  @Override
  public String getLabel() {
    return label.isEmpty() ? "" : label.get(0).getText();
  }
}
