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
package com.cognifide.qa.bb.cookies.modifier;

import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.cookies.Cookies;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.webdriver.WebDriverModifier;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class CookiesModifier implements WebDriverModifier {

  @Inject
  private Cookies cookies;

  @Inject
  @Named(ConfigKeys.COOKIE_LOAD_AUTOMATICALLY)
  private boolean loadAutomaticallyProperty;

  @Override
  public boolean shouldModify() {
    return loadAutomaticallyProperty && (getClass().getResource(Cookies.FILE_NAME) != null);
  }

  @Override
  public WebDriver modify(WebDriver webDriver) {
    cookies.setCookies(webDriver);
    return webDriver;
  }
}
