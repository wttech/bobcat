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
package com.cognifide.qa.bb.core.wait;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.core.modules.TestModule;
import com.cognifide.qa.bb.core.util.PageUtils;
import com.cognifide.qa.bb.junit5.BobcatExtension;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

@BobcatExtension
@Modules(TestModule.class)
class BobcatWaitTest {

  @Inject
  private WebDriver webDriver;

  @Inject
  private BobcatWait wait;

  @BeforeEach
  public void setUp() {
    webDriver.get(PageUtils.buildTestPageUrl(this.getClass()));
  }

  @Test
  void waitEvaluatesSimpleConditionsCorrectly() {
    wait.until(titleIs("BobcatWait Test"));
  }
}
