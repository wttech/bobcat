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
package com.cognifide.qa.bb.pageobject.active;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@PageObject
public class ActivePageObject {

  private static final String ID = "id";

  private static final String CSS = "css";

  private static final String XPATH = "xpath";

  private Map<String, PageObjectConfigPart> pageObjectConfigMap = new HashMap<>();

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Inject
  private WebDriver webDriver;

  public WebElement getWebElement(String name) {
    PageObjectConfigPart pageObjectConfigPart = pageObjectConfigMap.get(name);
    if (pageObjectConfigPart.getPageObjectType().equalsIgnoreCase(PageObjectType.WEB_ELEMENT.name())) {
      By selector = null;
      switch (pageObjectConfigPart.getSelectorType()) {
        case ID:
          selector = By.id(pageObjectConfigPart.getSelector());
          break;
        case CSS:
          selector = By.cssSelector(pageObjectConfigPart.getSelector());
          break;
        case XPATH:
          selector = By.xpath(pageObjectConfigPart.getSelector());
          break;
        default:
          selector = By.xpath("");
      }
      return pageObjectConfigPart.isGlobal() ? webDriver.findElement(selector) : currentScope.findElement(selector);
    }
    return null;
  }

  public ActivePageObject getPageObject(String name){
    return getPageObject(name, ActivePageObject.class);
  }

  public <X> X getPageObject(String name, Class<X> pageObject){
    PageObjectConfigPart pageObjectConfigPart = pageObjectConfigMap.get(name);
    if(pageObjectConfigPart.getPageObjectType().equalsIgnoreCase(PageObjectType.PAGE_OBJECT.name())){
      if(pageObject.equals(ActivePageObject.class)){
        //selector from file
      } else {
        //selector from annotation
      }

    }
    return null;
  }

  public void generatePageObjectConfigMap(
      List<PageObjectConfigPart> pageObjectConfigList) {
    this.pageObjectConfigMap = new HashMap<>();
    for (PageObjectConfigPart pageObjectConfigPart : pageObjectConfigList) {
      this.pageObjectConfigMap.put(pageObjectConfigPart.getName(), pageObjectConfigPart);
    }

  }
}
