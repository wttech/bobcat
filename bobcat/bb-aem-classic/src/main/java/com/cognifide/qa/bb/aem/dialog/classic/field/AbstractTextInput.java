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
package com.cognifide.qa.bb.aem.dialog.classic.field;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/**
 * This is an abstract base class for all AemElements that contain some kind of text input.
 */
public abstract class AbstractTextInput<T extends AbstractTextInput<?>> {

  /**
   * Clear value of the field.
   *
   * @return this text input instance.
   */
  public T clear() {
    getField().clear();
    return (T) this;
  }

  /**
   * Get text value of input field.
   *
   * @return value attribute of this text input
   */
  public String read() {
    return getField().getAttribute("value");
  }

  /**
   * Types the text provided as parameter into the field.
   *
   * @param text characters to be typed
   * @return this input instance.
   */
  @SuppressWarnings("unchecked")
  public T type(CharSequence text) {
    if (text != null) {
      getField().sendKeys(Keys.END);
      getField().sendKeys(text);
    }
    return (T) this;
  }

  protected abstract WebElement getField();

}
