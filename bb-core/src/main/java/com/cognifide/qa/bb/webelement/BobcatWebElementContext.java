/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.webelement;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.context.ConditionContext;

import java.util.List;

import com.cognifide.qa.bb.mapper.WebElementCachePolicy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

/**
 * Context of internal wrapper for Selenium's {@link WebElement}. For internal use only.
 *
 */
public class BobcatWebElementContext {

  private final WebElement webElement;

  private final Locatable locatable;

  private final List<ConditionContext> contextList;

  private final WebElementCachePolicy cachePolicy;

  /**
   *
   * @param webElement web element
   * @param locatable locatable
   * @param contextList internal context
   * @param cachePolicy caching policy
   */
  public BobcatWebElementContext(WebElement webElement, Locatable locatable,
      List<ConditionContext> contextList, WebElementCachePolicy cachePolicy) {
    this.webElement = webElement;
    this.locatable = locatable;
    this.contextList = contextList;
    this.cachePolicy = cachePolicy;
  }

  /**
   *
   * @return web element
   */
  public WebElement getWebElement() {
    return webElement;
  }

  /**
   *
   * @return locatable
   */
  public Locatable getLocatable() {
    return locatable;
  }

  /**
   *
   * @return Condition list defined by {@link LoadableComponent} annotations.
   */
  public List<ConditionContext> getLoadableConditionContext() {
    return contextList;
  }

  /**
   *
   * @return Cache policy for the concrete WebElement
   */
  public WebElementCachePolicy getCachePolicy() {
    return cachePolicy;
  }
}
