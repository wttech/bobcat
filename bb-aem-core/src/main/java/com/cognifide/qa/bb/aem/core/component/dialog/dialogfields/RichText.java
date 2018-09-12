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

import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.CONTROL;
import static org.openqa.selenium.Keys.RETURN;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

/**
 * This class represents rich text dialog field.
 */
@PageObject
public class RichText implements DialogField {

  @FindBy(css = ".coral-RichText")
  private WebElement input;

  @Inject
  private Actions actions;

  /**
   * This method deleted already set rich text value and sets new one.
   *
   * @param value desired string value of field. \\\\n will be replaced with RETURN key value.
   */
  @Override
  public void setValue(Object value) {
    String text = (String) value;
    actions.keyDown(input, CONTROL) //
          .sendKeys("a") //
          .keyUp(CONTROL) //
          .sendKeys(BACK_SPACE);

    List<String> textDividedByLines = Arrays.asList(text.split("\\\\n"));
    for (int i = 0; i < textDividedByLines.size(); i++) {
      if(i!=0) {
        actions.sendKeys(RETURN);
      }
      actions.sendKeys(textDividedByLines.get(i).trim());
    }
    actions.perform();
  }

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.RICHTEXT.name();
  }
}
