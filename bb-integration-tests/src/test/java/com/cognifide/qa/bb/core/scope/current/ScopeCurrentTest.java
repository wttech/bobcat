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
package com.cognifide.qa.bb.core.scope.current;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.core.TestModule;
import com.cognifide.qa.bb.core.pageobjects.current.scope.ScopedElements;
import com.cognifide.qa.bb.core.util.PageUtils;
import com.cognifide.qa.bb.junit5.guice.GuiceExtension;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.google.inject.Inject;

@ExtendWith(GuiceExtension.class)
@Modules({TestModule.class})
public class ScopeCurrentTest {

  @Inject
  private WebDriver webDriver;

  @Inject
  private ScopedElements scopedElements;

  @Before
  public void setUp() {
    webDriver.get(PageUtils.buildTestPageUrl(this.getClass()));
  }

  @Test
  public void shouldProvideCurrentScopeToAnnotatedList() {
    assertThat(scopedElements.getCurrentScopeList().getElementsText())
        .contains("1. outside", "2. outside", "3. outside", "4. outside");
  }

  @Test
  public void shouldProvideCurrentScopeToAnnotatedElement() {
    assertThat(scopedElements.getCurrentScopeElement().getElementText()).isEqualToIgnoringCase
        ("outside");
  }
}
