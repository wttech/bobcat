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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.summer.SectionHeaderComponent;
import com.cognifide.bdd.demo.suite.SmokeTests;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemTextField;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemTextFieldTest extends AemDialogTestBase {

  private SectionHeaderComponent sectionHeader;

  private AemDialog aemDialogForSectionHeader;

  @Category(SmokeTests.class)
  @Test
  public void fetchFieldByLabelTypeAndConfirm() {
    openDialog(aemDialogForSectionHeader);
    typeAndConfirm(getFieldByLabel());
  }

  @Test
  public void fetchFieldByLabelTypeAndCancel() {
    openDialog(aemDialogForSectionHeader);
    typeAndCancel(getFieldByLabel());
  }

  @Test
  public void fetchFieldByTabNameAndIndexTypeAndConfirm() {
    openDialog(aemDialogForSectionHeader);
    typeAndConfirm(getFieldByTabNameAndIndex());
  }

  @Test
  public void fetchFieldByTabNameAndIndexTypeAndCancel() {
    openDialog(aemDialogForSectionHeader);
    typeAndCancel(getFieldByTabNameAndIndex());
  }

  @Test
  public void fetchFieldByAnnotationTypeAndConfirm() {
    openDialog(aemDialogForSectionHeader);
    typeAndConfirm(sectionHeader.getHeaderTextField());
  }

  @Test
  public void fetchFieldByAnnotationTypeAndCancel() {
    openDialog(aemDialogForSectionHeader);
    typeAndCancel(sectionHeader.getHeaderTextField());
  }

  @Category(SmokeTests.class)
  @Test
  public void globalAnnotationTest() {
    int sectionHeaderListSize = sectionHeader.getSectionHeaderListSize();
    assertThat(sectionHeaderListSize, is(1));
    int spansSize = sectionHeader.getSpansSize();
    assertTrue(spansSize > 1);
  }

  @Override
  protected void initializeFields() {
    sectionHeader = summerBlockbusterHitsPage.getSectionHeader();
    assertNotNull(sectionHeader);
    aemDialogForSectionHeader = findDialog(sectionHeader);
  }

  private void typeAndConfirm(AemTextField textField) {
    assertNotNull(textField);
    clearTextField(textField);

    String newTitleToBeTyped = "aaa";
    sectionHeader.typeTextIntoDialog(textField, newTitleToBeTyped);
    aemDialogForSectionHeader.ok();
    assertThat(sectionHeader.getHeaderText(), is(newTitleToBeTyped));
  }

  private void typeAndCancel(AemTextField textField) {
    assertNotNull(textField);
    String beforeTyping = textField.read();
    sectionHeader.typeAndCancel(textField, "bbb");
    assertThat("header text should be not changed", sectionHeader.getHeaderText(),
        is(beforeTyping));
  }

  private void clearTextField(AemTextField textField) {
    textField.clear();
    assertTrue(textField.read().isEmpty());
  }

  private AemTextField getFieldByTabNameAndIndex() {
    return aemDialogForSectionHeader.getField("Section Header", 0, AemTextField.class);
  }

  private AemTextField getFieldByLabel() {
    return sectionHeader.getHeaderTextField();
  }
}
