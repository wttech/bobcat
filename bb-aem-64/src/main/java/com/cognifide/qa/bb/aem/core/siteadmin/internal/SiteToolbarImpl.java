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
package com.cognifide.qa.bb.aem.core.siteadmin.internal;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import cucumber.api.java.ca.I;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

@PageObject(css = ".foundation-collection-actionbar")
public class SiteToolbarImpl implements SiteToolbar {

  @Inject
  private WebDriver webDriver;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(css = "button.granite-collection-create.foundation-toggleable-control.coral3-Button.coral3-Button--primary>coral-button-label")
  private WebElement createButton;

  @Override public void create(String option) {
    Actions actions = new Actions(webDriver);
    actions.pause(2000).perform();
    createButton.click();
    currentScope.findElement(By.className("cq-siteadmin-admin-create" + StringUtils.lowerCase(option))).click();
  }
}
