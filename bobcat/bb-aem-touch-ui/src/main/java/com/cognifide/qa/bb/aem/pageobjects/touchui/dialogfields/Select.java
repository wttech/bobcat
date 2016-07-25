/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldType;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;

@PageObject
public class Select implements DialogField {

  @FindBy(css = ".coral-Select-select")
  private WebElement select;

  @Inject
  private Actions actions;

  @Inject
  private WebDriver driver;

  @Override
  public void setValue(Object value) {
    /*org.openqa.selenium.support.ui.Select selectElement = new org.openqa.selenium.support.ui.Select(
     select);*/
    List<String> values = Arrays.asList(String.valueOf(value).split(","));
    //values.stream().forEach(selectElement::selectByVisibleText);
    //   actions.moveToElement(select);

    WebElement parent = select.findElement(By.xpath(".."));
    List<WebElement> elements = parent.findElements(By.tagName("li"));
    JavascriptExecutor jse = (JavascriptExecutor) driver;
    actions.moveToElement(select);
   select.click();
    actions.sendKeys((String) value).perform();
    jse.executeScript("arguments[0].click();",
            elements.stream().filter(element -> element.getAttribute("className").contains("is-highlighted")).
            findFirst().get());

  }

  @Override
  public String getType() {
    return FieldType.SELECT.name();
  }

}
