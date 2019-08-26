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
package com.cognifide.qa.bb.provider.selenium.webdriver.creators;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

/**
 * Describes a creator of a specific WebDriver implementation.
 */
public interface WebDriverCreator {

  /**
   * Creates an instance of {@link WebDriver} with provided {@link Capabilities}
   *
   * @param capabilities map of capabilities passed
   * @return an instance of WebDriver with provided capabilities
   */
  WebDriver create(Capabilities capabilities);

  /**
   * @return ID of the creator, must match the corresponding {@code webdriver.type} System property
   */
  String getId();
}
