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
package com.cognifide.qa.bb.aem.dialog.classic.field;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.DialogComponent;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Textfield in a AemDialog.
 */
@PageObject
@Frame("$cq")
@DialogComponent("text field")
public class AemTextField extends AbstractTextInput<AemTextField> implements Configurable {

  @FindBy(xpath = ".//input[@type='text']")
  private WebElement thisElement;

  @Inject
  private BobcatWait bobcatWait;

  @Override
  public void setValue(String value) {
    clear().type(value);
  }

  @Override
  protected WebElement getField() {
    return thisElement;
  }

  /**
   * Removes all text from the text field.
   *
   * @return This AemTextField instance.
   */
  @Override
  public AemTextField clear() {
    super.clear();
    bobcatWait.withTimeout(Timeouts.MEDIUM)
        .until(driver -> "".equals(thisElement.getAttribute("value")));
    return this;
  }
}
