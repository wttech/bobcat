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
package com.cognifide.qa.bb.junit;

import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;

import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * When the test is finished, this test rule will fetch the webDrive instance and kill it by calling quit.
 */
public class WebDriverClosingRule extends ExternalResource {

  private final Injector injector;

  /**
   * Constructs the WebDriverClosingRule.
   *
   * @param injector - injector
   */
  public WebDriverClosingRule(Injector injector) {
    this.injector = injector;
  }

  @Override
  protected void after() {
    injector.getInstance(Key.get(WebDriver.class)).quit();
  }

}
