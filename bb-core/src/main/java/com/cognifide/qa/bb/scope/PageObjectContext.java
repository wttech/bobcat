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
package com.cognifide.qa.bb.scope;

import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.scope.frame.FramePath;

/**
 * This class represents context of the PageObject. Context consists of two objects:
 * <ul>
 * <li>the search scope (ElementLocatorFactory),
 * <li>frame path.
 * </ul>
 */
public class PageObjectContext {
  private final ElementLocatorFactory elementLocatorFactory;

  private final FramePath framePath;

  /**
   * Constructs PageObjectContext. Initializes its fields.
   *
   * @param elementLocatorFactory ElementLocatorFactory instance.
   * @param framePath             FramePath instance.
   */
  public PageObjectContext(ElementLocatorFactory elementLocatorFactory, FramePath framePath) {
    this.elementLocatorFactory = elementLocatorFactory;
    this.framePath = framePath;
  }

  /**
   * @return Element locator factory that is stored in the context.
   */
  public ElementLocatorFactory getElementLocatorFactory() {
    return elementLocatorFactory;
  }

  /**
   * @return Frame path that is stored in the context.
   */
  public FramePath getFramePath() {
    return framePath;
  }
}
