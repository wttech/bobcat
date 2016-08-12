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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.feedback.TitleComponent;
import com.cognifide.bdd.demo.po.product.CirclePage;
import com.cognifide.bdd.demo.suite.SmokeTests;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.logging.ReportEntryLogger;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemDropdownTest {

  private static final Logger LOG = LoggerFactory.getLogger(AemDropdownTest.class);

  private static final int OPENING_PAGE_TIMEOUT_IN_SECONDS = 200;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private ReportEntryLogger reportEntryLogger;

  @Inject
  private CirclePage circlePage;

  @Inject
  private AemLogin aemLogin;

  private TitleComponent titleComponent;

  private AemDialog dialog;

  @Before
  public void before() {
    aemLogin.authorLogin();
    openPageToTest();

    titleComponent = circlePage.getTitleComponent();
    dialog = titleComponent.getDialog();
  }

  @Category(SmokeTests.class)
  @Test
  public void dropdownTest_selectByText() {
    selectByTextSetTextSizeCheckHtml("Small", "h3");
    selectByTextSetTextSizeCheckHtml("Large", "h2");
    selectByTextSetTextSizeCheckHtml("Extra Large", "h1");
  }

  @Test
  public void dropdownTest_selectByPartialText() {
    selectByPartialTextSetTextSizeCheckHtml("Smal", "h3");
    selectByPartialTextSetTextSizeCheckHtml("Larg", "h2");
    selectByPartialTextSetTextSizeCheckHtml("Extra Larg", "h1");
  }

  @Test
  public void dropdownTest_selectByIndex() {
    selectByIndexSetTextSizeCheckHtml(1, "h3");
    selectByIndexSetTextSizeCheckHtml(2, "h2");
    selectByIndexSetTextSizeCheckHtml(3, "h1");
  }

  private void openPageToTest() {
    boolean isPageOpened = circlePage.openPageWithRefresh(OPENING_PAGE_TIMEOUT_IN_SECONDS);
    if (!isPageOpened) {
      reportEntryLogger.error("error while opening the page: '{}'", circlePage.getFullUrl());
    }
    assertTrue("page should be opened", isPageOpened);
  }

  private void selectByTextSetTextSizeCheckHtml(String size, String expectedTagName) {
    dialog.open();
    titleComponent.getTypeSizeDropdown().selectByText(size);
    dialog.ok();
    checkFormattingTag(expectedTagName);
  }

  private void selectByPartialTextSetTextSizeCheckHtml(String size, String expectedTagName) {
    dialog.open();
    titleComponent.getTypeSizeDropdown().selectByPartialText(size);
    dialog.ok();
    checkFormattingTag(expectedTagName);
  }

  private void selectByIndexSetTextSizeCheckHtml(int index, String expectedTagName) {
    dialog.open();
    titleComponent.getTypeSizeDropdown().selectByIndex(index);
    dialog.ok();
    checkFormattingTag(expectedTagName);
  }

  private void checkFormattingTag(String expectedTagName) {
    String tagName = bobcatWait.withTimeout(Timeouts.MEDIUM).until(input -> {
      String result = null;
      String actualTagName = titleComponent.findTitleFormattingTag();
      if (expectedTagName.equals(actualTagName)) {
        result = actualTagName;
      } else {
        LOG.debug("Waiting for expected tag: '{}'", expectedTagName);
      }
      return result;
    });
    String msg = String.format("Title tag name should be as expected: '%s'", expectedTagName);
    assertNotNull(msg, tagName);
  }
}
