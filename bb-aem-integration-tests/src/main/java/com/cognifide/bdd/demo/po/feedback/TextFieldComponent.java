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
package com.cognifide.bdd.demo.po.feedback;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.dialog.classic.field.AemCheckbox;
import com.cognifide.qa.bb.aem.qualifier.DialogField;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.aem.ui.component.AemComponent;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
@AemComponent(cssClassName = "text", group = "Form", name = "Text Field")
public class TextFieldComponent {
  @Inject
  private AemDialog aemDialog;

  @DialogField(label = "Hide Title")
  private AemCheckbox hideTitleCheckbox;

  @FindBy(css = ".form_leftcol")
  private WebElement label;

  public AemCheckbox getHideTitleCheckbox() {
    return hideTitleCheckbox;
  }

  public AemDialog getDialog() {
    return aemDialog;
  }

  public boolean isLabelVisible() {
    String cssValue = label.getCssValue("display");
    return !("none".equals(cssValue));
  }
}
