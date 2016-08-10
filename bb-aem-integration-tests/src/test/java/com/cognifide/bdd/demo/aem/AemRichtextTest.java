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

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.feedback.FeedbackPage;
import com.cognifide.bdd.demo.po.feedback.RichtextComponent;
import com.cognifide.qa.bb.SystemType;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemDropdown;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemRichText;
import com.cognifide.qa.bb.aem.dialog.classic.field.RtButton;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemRichtextTest {

  @Inject
  private AemLogin aemLogin;

  @Inject
  private FeedbackPage feedbackPage;

  private RichtextComponent richtextComponent;

  private AemRichText aemRichText;

  private AemDialog aemDialog;

  @BeforeClass
  public static void ignoreOnMacAndLinux() {
    Assume.assumeFalse(SystemType.current() == SystemType.MAC);
    Assume.assumeFalse(SystemType.current() == SystemType.LINUX);
  }

  @Before
  public void before() {
    aemLogin.authorLogin();
    openPageToTest();

    richtextComponent = feedbackPage.getRichtextComponent();
    aemRichText = richtextComponent.getAemRichText();
    aemDialog = richtextComponent.getDialog();
  }

  @Test
  public void richtextTest_clear() {
    clear();
  }

  @Test
  public void richtextTest_type() {
    clear();
    aemRichText.type("xyz");
    assertThat(aemRichText.read(), containsString("xyz"));
    aemDialog.ok();
    assertThat(richtextComponent.readFromPage(), containsString("xyz"));
  }

  @Test
  public void richtextTest_typeLine() {
    clear();
    aemRichText.typeLine("xyz");
    aemRichText.typeLine("abc");
    assertThat(aemRichText.read(), containsString("xyz\nabc"));
    aemDialog.ok();
    assertThat(richtextComponent.readFromPage(), containsString("xyz\nabc\n "));
  }

  @Test
  public void richtextTest_typeNewLine() {
    clear();
    aemRichText.type("xyz");
    aemRichText.typeNewLine();
    aemRichText.type("abc");
    assertThat(aemRichText.read(), containsString("xyz\nabc"));
    aemDialog.ok();
    assertThat(richtextComponent.readFromPage(), containsString("xyz\nabc"));
  }

  @Test
  public void richtextTest_selectText() {
    clear();
    aemRichText.type("xyz");
    aemRichText.selectText(1, 2);
    aemRichText.type("abc");
    assertThat(aemRichText.read(), containsString("xabcz"));
    aemDialog.ok();
    assertThat(richtextComponent.readFromPage(), containsString("xabcz"));
  }

  @Test
  public void richtextTest_selectAll() {
    clear();
    aemRichText.type("xyz");
    aemRichText.selectAll();
    aemRichText.type("abc");
    assertThat(aemRichText.read(), containsString("abc"));
    aemDialog.ok();
    assertThat(richtextComponent.readFromPage(), containsString("abc"));
  }

  @Test
  public void richtextTest_setCursorAtStartingPos() {
    clear();
    aemRichText.type("xyz");
    aemRichText.setCursorAtStartingPos();
    aemRichText.type("abc");
    assertThat(aemRichText.read(), containsString("abcxyz"));
    aemDialog.ok();
    assertThat(richtextComponent.readFromPage(), containsString("abcxyz"));
  }

  @Test
  public void richtextTest_setCursorAtEndPos() {
    clear();
    aemRichText.type("xyz");
    aemRichText.setCursorAtStartingPos();
    aemRichText.type("abc");
    aemRichText.setCursorAtEndPos();
    aemRichText.type("def");
    assertThat(aemRichText.read(), containsString("abcxyzdef"));
    aemDialog.ok();
    assertThat(richtextComponent.readFromPage(), containsString("abcxyzdef"));
  }

  @Test
  public void richtextTest_boldButton() {
    testButton(RtButton.BOLD, "xyz", "<p>x<b>y</b>z</p>");
  }

  @Test
  public void richtextTest_italicButton() {
    testButton(RtButton.ITALIC, "xyz", "<p>x<i>y</i>z</p>");
  }

  @Test
  public void richtextTest_underlineButton() {
    testButton(RtButton.UNDERLINE, "xyz", "<p>x<u>y</u>z</p>");
  }

  @Test
  public void richtextTest_alignLeftButton() {
    testButton(RtButton.JUSTIFY_LEFT, "xyz", "<p>xyz</p>");
  }

  @Test
  public void richtextTest_alignCenterButton() {
    testButton(RtButton.JUSTIFY_CENTER, "xyz", "<p style=\"text-align: center;\">xyz</p>");
  }

  @Test
  public void richtextTest_alignRightButton() {
    testButton(RtButton.JUSTIFY_RIGHT, "xyz", "<p style=\"text-align: right;\">xyz</p>");
  }

  @Test
  public void richtextTest_bulletListButton() {
    testButton(RtButton.BULLET_LIST, "xyz", "<ul><li>xyz</li></ul>", "<ul>\n<li>xyz</li>\n</ul>");
  }

  @Test
  public void richtextTest_indentButton() {
    testButton(RtButton.INDENT, "xyz", "<p style=\"margin-left: 40px;\">xyz</p>",
            "<p style=\"margin-left: 40.0px;\">xyz</p>");
  }

  @Test
  public void richtextTest_numberedListButton() {
    testButton(RtButton.NUMBERED_LIST, "xyz", "<ol><li>xyz</li></ol>", "<ol>\n<li>xyz</li>\n</ol>");
  }

  @Test
  public void richtextTest_outdentButton() {
    clear();
    aemRichText.type("xyz");
    aemRichText.click(RtButton.INDENT);
    aemRichText.click(RtButton.OUTDENT);
    assertThat(aemRichText.getInnerHTML(),
            anyOf(containsString("<p>xyz</p>"), containsString("<p style=\"\">xyz</p>")));
    aemDialog.ok();
    assertThat(richtextComponent.getPageHTML().trim(),
            anyOf(containsString("<p>xyz</p>"), containsString("<p style=\"\">xyz</p>")));
  }

  @Test
  public void richtextTest_spacer_before() {
    selectSpacer("Before", "spacer_before");
  }

  @Test
  public void richtextTest_spacer_after() {
    selectSpacer("After", "spacer_after");
  }

  @Test
  public void richtextTest_spacer_beforeAndAfter() {
    selectSpacer("Before and after", "spacer_both");
  }

  @Test
  public void richtextTest_spacer_none() {
    clear();
    aemDialog.clickTab("Styles");
    AemDropdown spacerDropdown = richtextComponent.getSpacerDropdown();
    spacerDropdown.selectByText("None");
    aemDialog.ok();
    String[] cssClasses = new String[]{"spacer_before", "spacer_after"};
    for (String cssClass : cssClasses) {
      String msg = "rich text component should NOT have the class: '" + cssClass + "'";
      assertFalse(msg, richtextComponent.hasClass(cssClass));
    }
  }

  @Test
  public void richtextTest_textStyle_large() {
    selectTextStyle("Large", "text_large");
  }

  @Test
  public void richtextTest_textStyle_quote() {
    selectTextStyle("Quote", "text_quote");
  }

  @Test
  public void richtextTest_textStyle_normal() {
    clear();
    aemDialog.clickTab("Styles");
    AemDropdown textStyleDropdown = richtextComponent.getTextStyleDropdown();
    textStyleDropdown.selectByText("Normal");
    aemDialog.ok();
    String[] cssClasses = {"text_large", "text_quote"};
    for (String cssClass : cssClasses) {
      assertFalse(richtextComponent.hasClass(cssClass));
    }
  }

  private void openPageToTest() {
    feedbackPage.open();
    assertTrue(feedbackPage.isDisplayed());
  }

  private void testButton(RtButton rtButton, String textToType, String expectedText) {
    testButton(rtButton, textToType, expectedText, expectedText);
  }

  private void testButton(RtButton rtButton, String textToType, String expectedText1,
          String expectedText2) {
    clear();
    aemRichText.type(textToType);
    aemRichText.selectText(1, 2);
    aemRichText.click(rtButton);
    assertThat(aemRichText.getInnerHTML(), containsString(expectedText1));
    aemDialog.ok();
    assertThat(richtextComponent.getPageHTML().trim(), containsString(expectedText2));
  }

  private void selectSpacer(String text, String... cssClasses) {
    clear();
    aemDialog.clickTab("Styles");
    AemDropdown spacerDropdown = richtextComponent.getSpacerDropdown();
    spacerDropdown.selectByText(text);
    aemDialog.ok();
    for (String cssClass : cssClasses) {
      assertTrue(richtextComponent.hasClass(cssClass));
    }
  }

  private void selectTextStyle(String text, String... cssClasses) {
    clear();
    aemDialog.clickTab("Styles");
    AemDropdown textStyleDropdown = richtextComponent.getTextStyleDropdown();
    textStyleDropdown.selectByText(text);
    aemDialog.ok();
    for (String cssClass : cssClasses) {
      assertTrue(richtextComponent.hasClass(cssClass));
    }
  }

  private void clear() {
    aemDialog.open();
    aemRichText.clear();
    assertThat("text should be cleared", aemRichText.read(), isEmptyString());
  }
}
