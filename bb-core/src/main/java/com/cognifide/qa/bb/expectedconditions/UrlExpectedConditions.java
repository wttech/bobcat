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
package com.cognifide.qa.bb.expectedconditions;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Class contains custom ExpectedConditions for testing current URL
 *
 * @deprecated use methods from {@link ExpectedConditions}
 */
@Deprecated
public final class UrlExpectedConditions {

  private UrlExpectedConditions() {
    throw new AssertionError();
  }

  /**
   * Check if provided url and current url are the same
   *
   * @param url Url pattern
   * @return false if current url is different than provided url or WebDriver is null
   */
  public static ExpectedCondition<Boolean> pageUrlIs(final String url) {
    return driver -> StringUtils.equals(driver.getCurrentUrl(), url);
  }

  /**
   * Check if current url contains provided url part
   *
   * @param url Url pattern
   * @return false if current url doesn't contain provided url part or WebDriver is null
   */
  public static ExpectedCondition<Boolean> pageUrlContains(final String url) {
    return driver -> StringUtils.contains(driver.getCurrentUrl(), url);
  }
}
