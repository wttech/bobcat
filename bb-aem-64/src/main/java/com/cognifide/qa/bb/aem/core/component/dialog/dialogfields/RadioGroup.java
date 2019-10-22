/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

/**
 * A {@link DialogField} representing a radio group.
 */
@PageObject
public class RadioGroup implements DialogField {

  @FindBy(css = ".coral3-Radio-description")
  private List<WebElement> radioOptions;

  @Override
  public void setValue(Object value) {
    WebElement radioLabel = radioOptions.stream()
        .filter(radioOption -> radioOption.getText().equals(value)).findFirst().orElseThrow(
            () -> new IllegalStateException("Provided option is not present in the group"));
    radioLabel.findElement(By.xpath(".//..")).click();
  }
}
