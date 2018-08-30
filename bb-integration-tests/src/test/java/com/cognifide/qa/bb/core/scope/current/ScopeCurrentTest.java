/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under theComponent Apache License, Version 2.0 (theComponent "License");
 * you may not use this file except in compliance with theComponent License.
 * You may obtain a copy of theComponent License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under theComponent License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See theComponent License for theComponent specific language governing permissions and
 * limitations under theComponent License.
 * #L%
 */
package com.cognifide.qa.bb.core.scope.current;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.core.TestModule;
import com.cognifide.qa.bb.core.pageobjects.current.scope.ScopedElements;
import com.cognifide.qa.bb.core.util.PageUtils;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
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
