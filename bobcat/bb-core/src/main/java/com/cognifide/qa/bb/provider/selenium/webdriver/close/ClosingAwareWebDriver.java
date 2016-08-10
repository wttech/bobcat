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
package com.cognifide.qa.bb.provider.selenium.webdriver.close;

import org.openqa.selenium.WebDriver;

/**
 * Bobcat will inject instances of this class in WebDriver fields of all PageObjects. When
 * ClosingAwareWebDriver is closing or quitting, it will inform all its subscribers that it is closing.
 */
public interface ClosingAwareWebDriver extends WebDriver {
  /**
   * @return State of the driver: true means alive, false means dead.
   */
  boolean isAlive();

  /**
   * Registers new listener
   *
   * @param listener Listener to register
   */
  void addListener(WebDriverClosedListener listener);

  /**
   * Shuts down browser instance even if the webdriver is in reusable mode
   */
  void forceShutdown();
}
