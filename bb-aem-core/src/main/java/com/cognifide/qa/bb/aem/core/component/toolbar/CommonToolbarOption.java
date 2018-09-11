/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.component.toolbar;

import org.openqa.selenium.By;

/**
 * Toolbar option for standard available in AEM options
 */
public class CommonToolbarOption implements ToolbarOption {

  private static final String BUTTON_XPATH_TEMPLATE = ".//button[contains(@title, '%s')]";

  private String title;

  public CommonToolbarOption(String title) {
    this.title = title;
  }

  @Override
  public By getLocator() {
    return By.xpath(String.format(BUTTON_XPATH_TEMPLATE, getTitle()));
  }

  @Override
  public String getTitle() {
    return title;
  }
}
