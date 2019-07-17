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

import static org.apache.commons.lang3.StringUtils.contains;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElements;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.DialogField;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.javascriptexecutor.JsScripts;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * Describes a simple widget button in RTE toolbar, e.g. Bold, Italic, Unlink that do not require any additional options or popups handling.
 */
@PageObject(css = ".rte-ui")
public class RteOption implements DialogField {

  @FindBy(css = Options.TOOLBAR_ITEM_CSS)
  private List<WebElement> buttons;

  @Inject
  private JavascriptExecutor javascriptExecutor;

  @Inject
  private BobcatWait wait;

  @Override
  public void setValue(Object value) {
    String option = (String) value;
    javascriptExecutor.executeScript(JsScripts.SELECT_ALL);

    wait.until(visibilityOfAllElements(buttons)).stream()
        .filter(button -> contains(button.getAttribute(HtmlTags.Attributes.TITLE), option))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            "Could not find the requested button on the toolbar: " + option))
        .click();
  }
}
