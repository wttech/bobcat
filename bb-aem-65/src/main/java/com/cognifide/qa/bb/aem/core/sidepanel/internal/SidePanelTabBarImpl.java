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
package com.cognifide.qa.bb.aem.core.sidepanel.internal;

import static com.cognifide.qa.bb.webelement.WebElementConditions.elementIsReady;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

/**
 * The default Bobcat implementation of {@link SidePanelTabBar} for AEM 6.5
 */
@PageObject(css = ".coral3-TabList")
public class SidePanelTabBarImpl implements SidePanelTabBar {

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Inject
  private BobcatWait bobcatWait;

  @Override
  public void switchTab(String tab) {
    bobcatWait.until(elementIsReady(currentScope.findElement(By.className(tab)))).click();
  }
}
