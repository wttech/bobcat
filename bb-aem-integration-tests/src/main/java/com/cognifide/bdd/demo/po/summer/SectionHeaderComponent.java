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
package com.cognifide.bdd.demo.po.summer;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.bdd.demo.po.ComponentWithDialog;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemTextField;
import com.cognifide.qa.bb.aem.qualifier.DialogField;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.logging.ReportEntryLogger;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class SectionHeaderComponent implements ComponentWithDialog {

  @Inject
  private ReportEntryLogger reportEntryLogger;

  @Inject
  private AemDialog aemDialog;

  @DialogField(label = "Header Text")
  private AemTextField headerText;

  @FindBy(tagName = "span")
  private WebElement sectionHeader;

  @Inject
  private HeaderComponent headerComponent;

  @Override
  public AemDialog getDialog() {
    return aemDialog;
  }

  public String getHeaderText() {
    return sectionHeader.getText();
  }

  public AemTextField getHeaderTextField() {
    return headerText;
  }

  public HeaderComponent getHeaderComponent() {
    return headerComponent;
  }

  public void typeAndCancel(AemTextField textField, String textToType) {
    typeTextIntoDialog(textField, textToType);
    aemDialog.cancel();
  }

  public void typeTextIntoDialog(AemTextField textField, String toBeTyped) {
    String beforeTyping = textField.read();
    reportEntryLogger.info("text field value before typing: '%s'", beforeTyping);
    // typing RIGHT arrow as initially the text is selected..
    textField.type(Keys.RIGHT);
    textField.type(toBeTyped);
    String valueAfterTyping = textField.read();
    reportEntryLogger.info("value after typing: '%s'", valueAfterTyping);
  }

  public int getSectionHeaderListSize() {
    return headerComponent.getSectionHeaderListSize();
  }

  public int getSpansSize() {
    return headerComponent.getSpansSize();
  }
}
