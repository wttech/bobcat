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
package com.cognifide.bdd.demo.po.feedback;

import java.util.Arrays;

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.bdd.demo.po.ComponentWithDialog;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemDropdown;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemRichText;
import com.cognifide.qa.bb.aem.qualifier.DialogField;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class RichtextComponent implements ComponentWithDialog {

  @Inject
  private AemDialog aemDialog;

  @DialogField(name = "./text")
  private AemRichText aemRichText;

  @DialogField(label = "Spacer")
  private AemDropdown spacerDropdown;

  @DialogField(label = "Text Style")
  private AemDropdown textStyleDropdown;

  @Inject
  @CurrentScope
  private WebElement thisComponent;

  @Override
  public AemDialog getDialog() {
    return aemDialog;
  }

  public String readFromPage() {
    return thisComponent.getText();
  }

  public String getPageHTML() {
    return thisComponent.getAttribute(HtmlTags.Properties.INNER_HTML);
  }

  public boolean hasClass(String cssClass) {
    return checkIfHasClass(thisComponent, cssClass);
  }

  public AemDropdown getSpacerDropdown() {
    return spacerDropdown;
  }

  public AemDropdown getTextStyleDropdown() {
    return textStyleDropdown;
  }

  public AemRichText getAemRichText() {
    return aemRichText;
  }

  // package scope for unit test
  boolean checkIfHasClass(WebElement element, String className) {
    boolean hasClass = false;
    String classAttributeValue = element.getAttribute(HtmlTags.Attributes.CLASS);
    if (classAttributeValue != null) {
      if (className == null || className.isEmpty()) {
        hasClass = true;
      } else {
        String[] cssClasses = classAttributeValue.split(" ");
        hasClass = Arrays.asList(cssClasses).contains(className);
      }
    }
    return hasClass;
  }
}
