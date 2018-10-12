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
public class FindPageObjectInterfaceTest {

  private static final String FOOD_CSS_CLASS = "food";
  private static final String TITLE_ATTRIBUTE = "title";
  private static final String SALAD_TEXT = "Salad";
  private static final String CHICKEN_TEXT = "Chicken";
  private static final String BUTTER_TEXT = "Butter";

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
    assertThat(masterPage.getFood().getCurrentScope().getAttribute(TITLE_ATTRIBUTE)).isEqualTo(
        FOOD_CSS_CLASS);
  }

  @Test
  public void shouldFindWebElementInPageObject() {
    assertThat(masterPage.getFood().getLiWebElement().getText()).isEqualTo(SALAD_TEXT);
  }

  @Test
  public void shouldFindWebElementListInPageObject() {
    softly.assertThat(masterPage.getFood().getLiWebElementList().size()).isEqualTo(3);
    softly.assertThat(masterPage.getFood().getLiWebElementList().get(2).getText())
        .isEqualTo(BUTTER_TEXT);
  }

  @Test
  public void shoudFindPageObjectInPageObject() {
    assertThat(masterPage.getFood().getLiPageObject().getText()).isEqualTo(SALAD_TEXT);
  }

  @Test
  public void shoudFindListOfPageObjectsInPageObject() {
    softly.assertThat(masterPage.getFood().getLiPageObjectList().size()).isEqualTo(3);
    softly.assertThat(masterPage.getFood().getLiPageObjectList().get(0).getText())
        .isEqualTo(SALAD_TEXT);
    softly.assertThat(masterPage.getFood().getLiPageObjectList().get(1).getText())
        .isEqualTo(CHICKEN_TEXT);
    softly.assertThat(masterPage.getFood().getLiPageObjectList().get(2).getText())
        .isEqualTo(BUTTER_TEXT);
  }
}
