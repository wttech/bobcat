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
package com.cognifide.qa.bb.pageobject;

import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageObjectMaker {

  @Inject
  protected PageObjectInjector pageObjectInjector;

  @Inject
  protected WebDriver webDriver;

  public <X> X getPageObject(Class<X> pageObject, int order, By selector, WebElement currentScope) {
    List<WebElement> scope = currentScope.findElements(selector);
    return injectPageObject(pageObject, order, scope);
  }

  public <X> X getPageObject(Class<X> pageObject, int order, By selector) {
    List<WebElement> scope = webDriver.findElements(selector);
    return injectPageObject(pageObject, order, scope);
  }

  private <X> X injectPageObject(Class<X> pageObject, int order, List<WebElement> scope) {
    return scope == null
        ? pageObjectInjector.inject(pageObject)
        : pageObjectInjector.inject(pageObject, scope.get(order));
  }

}
