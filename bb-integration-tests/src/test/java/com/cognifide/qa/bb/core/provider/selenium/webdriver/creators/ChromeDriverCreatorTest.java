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
package com.cognifide.qa.bb.core.provider.selenium.webdriver.creators;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.core.modules.ChromeModule;
import com.cognifide.qa.bb.core.util.PageUtils;
import com.cognifide.qa.bb.junit5.BobcatExtension;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.google.inject.Inject;

import io.github.bonigarcia.wdm.WebDriverManager;

@BobcatExtension
@Tag("creator")
@Modules(ChromeModule.class)
class ChromeDriverCreatorTest {

  @Inject
  WebDriver browser;

  @BeforeAll
  static void setup() {
    WebDriverManager.chromedriver().setup();
  }

  @Test
  void chromeDriverCreatorBuildsCorrectBrowser() {
    browser.get(PageUtils.buildTestPageUrl(this.getClass()));

    assertThat(browser.getTitle()).isEqualTo("Creator Test");
  }
}
