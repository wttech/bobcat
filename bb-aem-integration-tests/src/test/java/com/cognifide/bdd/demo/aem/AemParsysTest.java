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
package com.cognifide.bdd.demo.aem;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.pageobjects.ArticleComponent;
import com.cognifide.bdd.demo.po.feedback.TitleComponent;
import com.cognifide.bdd.demo.po.summer.SummerBlockbusterHitsPage;
import com.cognifide.bdd.demo.suite.SmokeTests;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.ui.parsys.AemParsys;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemParsysTest {

  private static final String VISIBLE_DIALOG_CSS_SELECTOR =
      "div[id^='ext-comp-'][style*='visibility: visible'].x-window";

  private static final String LIST_COMPONENT = "List";

  private static final String IMAGE_COMPONENT = "Image";

  private static final String GENERAL_GROUP = "General";

  private static final String SECOND_TEST_TEXT = "My another test String";

  private static final String TEST_TEXT = "My test String";

  private static final int WAIT_TIME = 5;

  private static final By ARTICLE_COMPONENT_CLICKABLE_AREA = By.cssSelector("img.cq-dd-file");

  private static final By TITLE_COMPONENT_CLICKABLE_AREA = By.cssSelector("h1");

  @Inject
  private WebDriver webDriver;

  @Inject
  private SummerBlockbusterHitsPage page;

  @Inject
  private AemLogin aemLogin;

  @Inject
  private FrameSwitcher switcher;

  private AemParsys topParsys;

  @Before
  public void before() {
    webDriver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
    webDriver.manage().window().maximize();
    aemLogin.authorLogin();
    openPageToTest();
    topParsys = page.getTopParsys().clear();
  }

  @Category(SmokeTests.class)
  @Test
  public void insertComponent() {
    assertTrue(topParsys.isComponentNotPresent(ArticleComponent.class));
    topParsys.insertFirstComponentType(ArticleComponent.class);
    assertTrue(topParsys.isComponentPresent(ArticleComponent.class));
  }

  @Test
  public void insertComponentUsingString() {
    assertTrue(topParsys.isComponentNotPresent(IMAGE_COMPONENT.toLowerCase()));
    topParsys.insertComponent(GENERAL_GROUP, IMAGE_COMPONENT);
    assertTrue(topParsys.isComponentPresent(IMAGE_COMPONENT.toLowerCase()));
  }

  @Test
  public void insertComponentByContextMenu() {
    assertTrue(topParsys.isComponentNotPresent(IMAGE_COMPONENT.toLowerCase()));
    topParsys.openInsertWindowByContextMenu().insertComponent(GENERAL_GROUP, IMAGE_COMPONENT);
    assertTrue(topParsys.isComponentPresent(IMAGE_COMPONENT.toLowerCase()));
  }

  @Test
  public void clearAndCount() {
    assertThat(topParsys.componentsCount(), is(0));
    topParsys.insertFirstComponentType(ArticleComponent.class);
    topParsys.insertComponent(GENERAL_GROUP, IMAGE_COMPONENT);
    topParsys.insertComponent(ArticleComponent.class);
    assertThat(topParsys.componentsCount(), is(3));
    topParsys.clear();
    assertThat(topParsys.componentsCount(), is(0));
  }

  @Test
  public void removeByGlobalIndex() {
    topParsys.insertComponent(GENERAL_GROUP, LIST_COMPONENT);
    topParsys.insertFirstComponentType(ArticleComponent.class);
    topParsys.insertComponent(GENERAL_GROUP, LIST_COMPONENT);
    topParsys.removeComponent(1);
    assertTrue(topParsys.isComponentNotPresent(ArticleComponent.class));
  }

  @Test
  public void removeByComponentTypeAndClickableArea() {
    topParsys.insertComponent(GENERAL_GROUP, LIST_COMPONENT);
    topParsys.insertFirstComponentType(ArticleComponent.class);
    topParsys.insertComponent(GENERAL_GROUP, LIST_COMPONENT);
    topParsys.removeFirstComponentOfType(ArticleComponent.class, ARTICLE_COMPONENT_CLICKABLE_AREA);
    assertTrue(topParsys.isComponentNotPresent(ArticleComponent.class));
  }

  @Test
  public void removeByComponentType() {
    topParsys.insertComponent(GENERAL_GROUP, LIST_COMPONENT);
    topParsys.insertFirstComponentType(ArticleComponent.class);
    topParsys.insertComponent(GENERAL_GROUP, LIST_COMPONENT);
    topParsys.removeFirstComponentOfType(ArticleComponent.class);
    assertTrue(topParsys.isComponentNotPresent(ArticleComponent.class));
  }

  @Test
  public void removeByComponentTypeAndIndex() {
    topParsys.insertFirstComponentType(TitleComponent.class);
    topParsys.insertComponent(GENERAL_GROUP, LIST_COMPONENT);
    TitleComponent title = topParsys.insertComponent(TitleComponent.class);
    changeTitle(title, TEST_TEXT);

    topParsys.removeNthComponentOfType(TitleComponent.class, 0);

    title = topParsys.getFirstComponentOfType(TitleComponent.class);
    assertTitleEquals(TEST_TEXT, title);
  }

  @Test
  public void removeByComponentTypeAndIndexAndClickableArea() {
    topParsys.insertFirstComponentType(TitleComponent.class);
    topParsys.insertComponent(GENERAL_GROUP, LIST_COMPONENT);
    TitleComponent title = topParsys.insertComponent(TitleComponent.class);
    changeTitle(title, TEST_TEXT);

    topParsys.removeNthComponentOfType(TitleComponent.class, 0, TITLE_COMPONENT_CLICKABLE_AREA);

    title = topParsys.getFirstComponentOfType(TitleComponent.class);
    assertTitleEquals(TEST_TEXT, title);
  }

  @Test
  public void testGetComponent() {
    topParsys.insertFirstComponentType(ArticleComponent.class);
    topParsys.insertFirstComponentType(TitleComponent.class);
    TitleComponent title = topParsys.getFirstComponentOfType(TitleComponent.class);
    changeTitle(title, TEST_TEXT);

    topParsys.insertComponent(ArticleComponent.class);
    topParsys.insertComponent(TitleComponent.class);
    topParsys.insertComponent(ArticleComponent.class);

    title = topParsys.getNthComponentOfType(TitleComponent.class, 1);
    assertTitleEquals("", title);
    changeTitle(title, SECOND_TEST_TEXT);

    title = topParsys.getNthComponentOfType(TitleComponent.class, 0);
    assertTitleEquals(TEST_TEXT, title);

    title = topParsys.getComponent(TitleComponent.class, 3);
    assertTitleEquals(SECOND_TEST_TEXT, title);

  }

  @Test
  public void openComponentDialog() {
    ArticleComponent articleComponent = topParsys.insertFirstComponentType(ArticleComponent.class);
    articleComponent.getDialog().open();

    switcher.switchTo("$cq");
    assertTrue(webDriver.findElement(By.cssSelector(VISIBLE_DIALOG_CSS_SELECTOR)).isDisplayed());
  }

  private void openPageToTest() {
    page.open();
    assertTrue(page.isDisplayed());
  }

  private void changeTitle(TitleComponent titleComponent, String textToType) {
    titleComponent.getDialog().open();
    titleComponent.getTitle().type(textToType);
    titleComponent.getDialog().ok();
  }

  private void assertTitleEquals(String text, TitleComponent titleComponent) {
    titleComponent.getDialog().open();
    assertThat(titleComponent.getTitle().read(), is(text));
    titleComponent.getDialog().ok();
  }
}
