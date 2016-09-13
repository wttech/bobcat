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
package com.cognifide.qa.bb.scope.selector;

import com.cognifide.qa.bb.scope.SearchContextAwareLocator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Simple locator that consists of search context (for example webdriver) and a selector.
 */
public class SelectorElementLocator implements SearchContextAwareLocator {

  private final SearchContext searchContext;

  private final By selector;

  /**
   * Constructs SelectorElementLocator.
   *
   * @param searchContext A context to use to find the element.
   * @param selector By selector used to find elements
   */
  public SelectorElementLocator(SearchContext searchContext, By selector) {
    this.searchContext = searchContext;
    this.selector = selector;
  }

  @Override
  public WebElement findElement() {
    return selector.findElement(searchContext);
  }

  @Override
  public List<WebElement> findElements() {
    return searchContext.findElements(selector);
  }

  @Override
  public SearchContext getSearchContext() {
    return searchContext;
  }

}
