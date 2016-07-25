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
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.touchui.components.text.TextComponent;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.data.pages.Pages;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.aem.pageobjects.touchui.GlobalBar;
import com.cognifide.qa.bb.aem.pageobjects.touchui.GlobalBar.AuthoringMode;
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

  private static final String PAGE_TITLE = "Summer Blockbuster";

  @Inject
  private AemLogin aemLogin;

  @Inject
  private GlobalBar globalBar;

  @Inject
  private Pages pages;

  @Inject
  private AuthorPageFactory authorPageFactory;

  private AuthorPage blockbusterPage;

  private String parsys;

  @Before
  public void before() {
    aemLogin.authorLogin();
    blockbusterPage = authorPageFactory.create(pages.getPath(PAGE_TITLE));
    parsys = pages.getParsys(PAGE_TITLE);
    blockbusterPage.open();
    assertThat("Page has not loaded", blockbusterPage.isLoaded(), is(true));
    blockbusterPage.addComponent(parsys, "Text");
  }

   @Test
   public void shouldContainEnteredText() {
   blockbusterPage.configureComponent(parsys, "Text", "plain_text");
   String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
   assertThat(contents, containsString("<p>test test test</p>"));
   }

  @Test
  public void shouldRenderUnderlinedTextProperly() {
    blockbusterPage.configureComponent(parsys, "Text", "underline");
    String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
    assertThat(contents, containsString("<p><u>test test test</u></p>"));
  }

   @Test
   public void shouldRenderBoldedTextProperly() {
    blockbusterPage.configureComponent(parsys, "Text", "bold");
    String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
    assertThat(contents, containsString("<p><b>test test test</b></p>"));
   }

   @Test
   public void shouldRenderItalicTextProperly() {
    blockbusterPage.configureComponent(parsys, "Text", "italic");
    String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
    assertThat(contents, containsString("<p><i>test test test</i></p>"));
   }

   @Test
   public void shouldRenderTextJustifiedToTheRightSideProperly() {
    blockbusterPage.configureComponent(parsys, "Text", "justify_right");
    String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
    assertThat(contents, containsString("<p style=\"text-align: right;\">test test test</p>"));
   }

   @Test
   public void shouldRenderTextJustifiedToTheCenterProperly() {
    blockbusterPage.configureComponent(parsys, "Text", "justify_center");
    String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
    assertThat(contents, containsString("<p style=\"text-align: center;\">test test test</p>"));
   }

   @Test
   public void shouldRenderTextJustifiedToTheLeftSideProperly() {
    blockbusterPage.configureComponent(parsys, "Text", "justify_left");
    String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
    assertThat(contents, containsString("<p>test test test</p>"));
   }

  @Test
  public void shouldRenderBulletListProperly() {
    blockbusterPage.configureComponent(parsys, "Text", "bullet_list");
    String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
    //@formatter:off
    assertThat(contents, containsString("<ul>\n"
                                        + "<li>test test test</li>\n"
                                        + "<li>test test test</li>\n"
                                      + "</ul>"));
    //@formatter:on
  }

  @Test
  public void shouldRenderNumberedListProperly() {
    blockbusterPage.configureComponent(parsys, "Text", "numbered_list");
    String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
    //@formatter:off
    assertThat(contents, containsString("<ol>\n"
                                        + "<li>test test test</li>\n"
                                        + "<li>test test test</li>\n"
                                      + "</ol>"));
    //@formatter:on
  }

  @Test
  public void shouldRenderIndentProperly() {
    blockbusterPage.configureComponent(parsys, "Text", "indent");
    String contents = blockbusterPage.getContent(TextComponent.class).getInnerHTML();
    assertThat(contents, containsString("<p style=\"margin-left: 40.0px;\">test test test</p>"));
  }

  @After
  public void cleanUp() {
    if (AuthoringMode.PREVIEW == globalBar.getCurrentMode()) {
      globalBar.switchToEditMode();
      parsys = pages.getParsys(PAGE_TITLE);
    }
    blockbusterPage.deleteComponent(parsys, "Text");
  }
}
