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

import org.openqa.selenium.By;

public final class SelectorCreator {

  private static final String ID = "id";

  private static final String CSS = "css";

  private static final String XPATH = "xpath";

  private SelectorCreator(){
  }

  public static By getSelector(String selectorType, String selector) {
    By selectorBy = null;
    switch (selectorType) {
      case ID:
        selectorBy = By.id(selector);
        break;
      case CSS:
        selectorBy = By.cssSelector(selector);
        break;
      case XPATH:
        selectorBy = By.xpath(selector);
        break;
      default:
        selectorBy = By.xpath("");
    }
    return selectorBy;
  }
}
