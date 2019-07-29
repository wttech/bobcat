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
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.rte;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Describes the Hyperlink popover opened after clicking Hyperlink widget in RTE toolbar.
 */
@PageObject(css = "coral-popover.is-open")
public class HyperlinkPopover {

  @FindBy(css = "foundation-autocomplete input.coral-InputGroup-input")
  private WebElement pathInput;

  @FindBy(css = "input[is=coral-textfield]:not(.coral-InputGroup-input)")
  private WebElement altTextInput;

  @FindBy(css = "coral-select button")
  private WebElement targetSelect;

  @FindBy(css = "coral-select coral-selectlist-item")
  private List<WebElement> targetSelectOptions;

  @FindBy(css = "button[data-type='apply']")
  private WebElement okButton;

  @Inject
  private BobcatWait wait;

  public HyperlinkPopover enterPath(String path) {
    wait.until(visibilityOf(pathInput)).sendKeys(path);
    return this;
  }

  public HyperlinkPopover enterAltText(String altText) {
    wait.until(visibilityOf(altTextInput)).sendKeys(altText);
    return this;
  }

  public HyperlinkPopover setTarget(String target) {
    wait.until(visibilityOf(targetSelect)).click();
    wait.until(visibilityOfAllElements(targetSelectOptions)).stream()
        .filter(option -> StringUtils.equals(option.getText(), target))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            "Following option not found in the Target select: " + target))
        .click();
    return this;
  }

  public HyperlinkPopover confirm() {
    wait.until(visibilityOf(okButton)).click();
    return this;
  }

}
