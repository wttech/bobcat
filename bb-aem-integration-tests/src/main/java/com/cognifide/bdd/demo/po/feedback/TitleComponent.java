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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.dialog.classic.field.AemTextField;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemDropdown;
import com.cognifide.qa.bb.aem.qualifier.DialogField;
import com.cognifide.qa.bb.aem.ui.component.AemComponent;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
@AemComponent(cssClassName = "title", group = "General", name = "Title")
public class TitleComponent {

  private static final Logger LOG = LoggerFactory.getLogger(TitleComponent.class);

  private static final String TITLE_TAG_XPATH = ".//*";

  private static final By TITLE_TAG_BY = By.xpath(TITLE_TAG_XPATH);

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(xpath = TITLE_TAG_XPATH)
  private WebElement titleTag;

  @Inject
  private AemDialog dialog;

  @DialogField(label = "Type / Size")
  private AemDropdown typeSizeDropdown;

  @DialogField(label = "Title")
  private AemTextField title;

  public AemDialog getDialog() {
    return dialog;
  }

  public AemDropdown getTypeSizeDropdown() {
    return typeSizeDropdown;
  }

  public String getTitleFormattingTag() {
    LOG.debug("titleTag property: '{}', hashcode: '{}'", title.toString(), titleTag.hashCode());
    return titleTag.getTagName();
  }

  public AemTextField getTitle() {
    return title;
  }

  /**
   * Search for the web element of title as it could be replaced by JavaScript in the meantime.
   *
   * @return tag name for the title, most likely something like: H1, H2,..
   */
  public String findTitleFormattingTag() {
    bobcatWait.withTimeout(Timeouts.MEDIUM).until(ExpectedConditions.visibilityOf(currentScope));
    WebElement titleTagFound = currentScope.findElement(TITLE_TAG_BY);
    return titleTagFound.getTagName();
  }
}
