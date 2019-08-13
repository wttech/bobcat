/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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
package com.cognifide.qa.bb.appium.modules;

import com.cognifide.qa.bb.appium.webdriver.creators.AndroidDriverCreator;
import com.cognifide.qa.bb.appium.webdriver.creators.IosDriverCreator;
import com.cognifide.qa.bb.provider.selenium.webdriver.creators.WebDriverCreator;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Registers WebDriverCreators from this module
 */
public class WebdriverCreatorsModule extends AbstractModule {
  @Override
  protected void configure() {
    Multibinder<WebDriverCreator> creators =
        Multibinder.newSetBinder(binder(), WebDriverCreator.class);

    creators.addBinding().to(AndroidDriverCreator.class);
    creators.addBinding().to(IosDriverCreator.class);
  }
}
