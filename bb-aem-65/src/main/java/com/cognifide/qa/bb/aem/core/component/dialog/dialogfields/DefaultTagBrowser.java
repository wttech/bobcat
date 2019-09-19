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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Default implementation of {@link TagBrowser}
 */
@PageObject(css = Locators.AUTOCOMPLETE_CSS)
public class DefaultTagBrowser implements TagBrowser {

  @FindBy(css = ".coral3-Textfield")
  private WebElement input;

  @FindBy(xpath = Locators.ALTERNATE_LABEL_XPATH)
  private List<WebElement> label;

  @FindBy(css = ".coral3-Tag-removeButton")
  private List<WebElement> removeTagButtons;

  @FindBy(css = ".foundation-picker-buttonlist.coral3-Overlay.is-open")
  private WebElement firstResult;

  @Inject
  private BobcatWait bobcatWait;

  @Override
  public void setValue(Object value) {
    List<String> tags = Arrays
        .asList(String.valueOf(value).trim().replaceAll("\\[|\\]|\\s", "").split(","));

    removeTagButtons.forEach((element) -> element.sendKeys(Keys.ENTER));

    tags.forEach((tag) -> {
      input.clear();
      input.sendKeys(tag);
      bobcatWait.until(elementToBeClickable(firstResult)).click();
    });
  }

  @Override
  public String getLabel() {
    return label.isEmpty() ? "" : label.get(0).getText();
  }
}
