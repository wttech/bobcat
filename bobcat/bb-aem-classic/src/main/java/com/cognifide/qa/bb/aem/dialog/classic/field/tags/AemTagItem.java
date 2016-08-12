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
package com.cognifide.qa.bb.aem.dialog.classic.field.tags;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Single tag in the tag window.
 */
@PageObject(css = "div.taglabel")
@Frame("$cq")
public class AemTagItem {

  private static final Logger LOG = LoggerFactory.getLogger(AemTagItem.class);

  private static final String DENIEDTAG_CSS_CLASS = "deniedtag";

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(css = "div.parentpath")
  private WebElement parentPath;

  @FindBy(css = "div.tagname")
  private WebElement tagName;

  @FindBy(css = "div.taglabel-tool-remove")
  private WebElement removeButton;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private NamespaceAndPathSplitter splitter;

  @Inject
  private Actions actions;

  /**
   * Clicks the "x" button to remove the tag.
   */
  public void remove() {
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      try {
        tagName.click();
        actions.moveToElement(removeButton).click().perform();
      } catch (StaleElementReferenceException e) {
        LOG.debug("Button is not available at the moment: ", e);
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    }, 2);
  }

  /**
   * @return Namespace of the tag.
   */
  public String getNamespace() {
    return splitter.getNamespaceAndPath(parentPath.getText()).getNamespace();
  }

  /**
   * @return Path of the tag.
   */
  public String getParentPath() {
    return splitter.getNamespaceAndPath(parentPath.getText()).getPath();
  }

  /**
   * @return Name of the tag.
   */
  public String getTagName() {
    return tagName.getText().trim();
  }

  /**
   * @return Unique identifier of the tag e.g. (facebook namespace) Facebook:Location/Majorca,
   * (default namespace) Standard Tags/SuperTag
   */
  public String getIdentifier() {
    return splitter.getNamespaceAndPath(parentPath.getText()).toString() + getTagName();
  }

  /**
   * @return True if tag is denied, false if allowed.
   */
  public boolean isDenied() {
    return currentScope.getAttribute(HtmlTags.Attributes.CLASS).contains(DENIEDTAG_CSS_CLASS);
  }

}
