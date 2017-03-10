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
package com.cognifide.qa.bb.scope.webelement;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

/**
 * The most basic locator. Wraps a webElement and returns it when the "find" methods are called.
 */
public class WebElementLocator implements ElementLocator {

  private final WebElement webElement;

  /**
   * Constructs WebElementLocator.
   *
   * @param webElement element on the page.
   */
  public WebElementLocator(WebElement webElement) {
    this.webElement = webElement;
  }

  @Override
  public WebElement findElement() {
    return webElement;
  }

  @Override
  public List<WebElement> findElements() {
    return Arrays.asList(webElement);
  }

}
