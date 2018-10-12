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
package com.cognifide.qa.bb.core.qualifier.findpageobject;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.core.TestModule;
import com.cognifide.qa.bb.core.pageobjects.qualifier.findpageobject.MasterPage;
import com.cognifide.qa.bb.core.util.PageUtils;
import com.cognifide.qa.bb.junit5.guice.GuiceExtension;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.google.inject.Inject;

@ExtendWith(GuiceExtension.class)
@Modules({TestModule.class})
public class FindPageObjectTest {

  private static final String DRINKS_CSS_CLASS = "drinks";
  private static final String TITLE_ATTRIBUTE = "title";
  private static final String COFFEE_TEXT = "Coffee";
  private static final String MILK_TEXT = "Milk";
  private static final String TEA_TEXT = "Tea";

  @Rule
  public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

  @Inject
  private WebDriver webDriver;

  @Inject
  private MasterPage masterPage;

  @Before
  public void setUp() {
    webDriver.get(PageUtils.buildTestPageUrl(this.getClass()));
  }

  @Test
  public void shouldFindCurrentScopeInPageObject() {
    assertThat(masterPage.getDrinks().getCurrentScope().getAttribute(TITLE_ATTRIBUTE)).isEqualTo(
        DRINKS_CSS_CLASS);
  }

  @Test
  public void shouldFindWebElementInPageObject() {
    assertThat(masterPage.getDrinks().getLiWebElement().getText()).isEqualTo(COFFEE_TEXT);
  }

  @Test
  public void shouldFindWebElementListInPageObject() {
    softly.assertThat(masterPage.getDrinks().getLiWebElementList().size()).isEqualTo(3);
    softly.assertThat(masterPage.getDrinks().getLiWebElementList().get(2).getText())
        .isEqualTo(MILK_TEXT);
  }

  @Test
  public void shoudFindPageObjectInPageObject() {
    assertThat(masterPage.getDrinks().getLiPageObject().getText()).isEqualTo(COFFEE_TEXT);
  }

  @Test
  public void shoudFindListOfPageObjectsInPageObject() {
    softly.assertThat(masterPage.getDrinks().getLiPageObjectList().size()).isEqualTo(3);
    softly.assertThat(masterPage.getDrinks().getLiPageObjectList().get(0).getText())
        .isEqualTo(COFFEE_TEXT);
    softly.assertThat(masterPage.getDrinks().getLiPageObjectList().get(1).getText())
        .isEqualTo(TEA_TEXT);
    softly.assertThat(masterPage.getDrinks().getLiPageObjectList().get(2).getText())
        .isEqualTo(MILK_TEXT);
  }
}
