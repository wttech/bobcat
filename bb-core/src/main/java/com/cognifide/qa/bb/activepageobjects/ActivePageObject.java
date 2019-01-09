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
package com.cognifide.qa.bb.activepageobjects;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@PageObject
public class ActivePageObject {

  private final static String ID = "id";

  private final static String CSS = "css";

  private final static String XPATH =  "xpath";

  private Map<String, PageObjectConfigPart> pageObjectConfigMap = new HashMap<>();

  @CurrentScope
  private WebElement currentScope;

  WebElement getWebElement(String name) {
    PageObjectConfigPart pageObjectConfigPart = pageObjectConfigMap.get(name);
    if (pageObjectConfigPart.getPageObjectType().equals(PageObjectType.WEB_ELEMENT)) {
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
      }
      return currentScope.findElement(selector);
    }
    return null;
  }

}
