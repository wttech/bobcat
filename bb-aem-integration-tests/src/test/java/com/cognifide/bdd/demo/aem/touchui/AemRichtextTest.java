/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.bdd.demo.aem.touchui;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.touchui.TextComponent;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.touch.data.pages.Pages;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.GlobalBar;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemRichtextTest {

  private static final String PAGE_TITLE = "Text - Update&Read";

  private static final String COMPONENT_NAME = "Text";

  @Inject
  private AemLogin aemLogin;

  @Inject
  private GlobalBar globalBar;

  @Inject
  private Pages pages;

  @Inject
  private AuthorPageFactory authorPageFactory;

  private AuthorPage page;

  private String parsys;

  @Before
  public void setUp() {
    aemLogin.authorLogin();
    page = authorPageFactory.create(pages.getPath(PAGE_TITLE));
    parsys = pages.getParsys(PAGE_TITLE);
    page.open();
    assertTrue("Page has not loaded", page.isLoaded());
    page.clearParsys(parsys, COMPONENT_NAME);
    assertFalse(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME));
    page.addComponent(parsys, COMPONENT_NAME);
    assertTrue(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME));
  }

  @After
  public void tearDown() {
    page.clearParsys(parsys, COMPONENT_NAME);
  }

  @Test
  public void shouldContainEnteredText() {
    page.configureComponent(parsys, COMPONENT_NAME, "plain_text");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    assertThat(contents, containsString("<p>test test test</p>"));
  }

  @Test
  public void shouldRenderUnderlinedTextProperly() {
    page.configureComponent(parsys, COMPONENT_NAME, "underline");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    assertThat(contents, containsString("<p><u>test test test</u></p>"));
  }

  @Test
  public void shouldRenderBoldedTextProperly() {
    page.configureComponent(parsys, COMPONENT_NAME, "bold");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    assertThat(contents, containsString("<p><b>test test test</b></p>"));
  }

  @Test
  public void shouldRenderItalicTextProperly() {
    page.configureComponent(parsys, COMPONENT_NAME, "italic");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    assertThat(contents, containsString("<p><i>test test test</i></p>"));
  }

  @Test
  public void shouldRenderTextJustifiedToTheRightSideProperly() {
    page.configureComponent(parsys, COMPONENT_NAME, "justify_right");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    assertThat(contents, containsString("<p style=\"text-align: right;\">test test test</p>"));
  }

  @Test
  public void shouldRenderTextJustifiedToTheCenterProperly() {
    page.configureComponent(parsys, COMPONENT_NAME, "justify_center");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    assertThat(contents, containsString("<p style=\"text-align: center;\">test test test</p>"));
  }

  @Test
  public void shouldRenderTextJustifiedToTheLeftSideProperly() {
    page.configureComponent(parsys, COMPONENT_NAME, "justify_left");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    assertThat(contents, containsString("<p>test test test</p>"));
  }

  @Test
  public void shouldRenderBulletListProperly() {
    page.configureComponent(parsys, COMPONENT_NAME, "bullet_list");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    //@formatter:off
    assertThat(contents, containsString("<ul>\n"
            + "<li>test test test</li>\n"
            + "<li>test test test</li>\n"
            + "</ul>"));
    //@formatter:on
  }

  @Test
  public void shouldRenderNumberedListProperly() {
    page.configureComponent(parsys, COMPONENT_NAME, "numbered_list");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    //@formatter:off
    assertThat(contents, containsString("<ol>\n"
            + "<li>test test test</li>\n"
            + "<li>test test test</li>\n"
            + "</ol>"));
    //@formatter:on
  }

  @Test
  public void shouldRenderIndentProperly() {
    page.configureComponent(parsys, COMPONENT_NAME, "indent");
    globalBar.switchToPreviewMode();
    String contents = page.getContent(TextComponent.class).getOuterHTML();
    assertThat(contents, containsString("<p style=\"margin-left: 40.0px;\">test test test</p>"));
  }
}
