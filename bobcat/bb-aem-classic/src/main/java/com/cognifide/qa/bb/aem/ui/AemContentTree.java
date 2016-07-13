/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb.aem.ui;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;

/**
 * Exposes functionality of a content tree
 */
@PageObject
public class AemContentTree {

  private static final String CSS_CLASS_ATTRIBUTE_NAME = "class";
  public static final String MIDDLE_NODE_CLASS = "x-tree-node-expanded";
  public static final String LAST_NODE_CLASS = "x-tree-selected";

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private WebElementUtils webElementUtils;

  /**
   * Method to expand and select target node in paths tree. Throw
   * {@link java.lang.IllegalArgumentException} if path parameter is null or empty or
   * {@link org.openqa.selenium.NoSuchElementException} if element can not be found.
   *
   * @param path - absolute path to target node without leading slash e.g. Websites/Geometrixx
   *             Mobile Demo Site/English/Mobile Trader/Advisor or Websites/Geometrixx Mobile Demo
   *             Site/English
   */
  public void selectPath(String path) {
    if (StringUtils.isEmpty(path)) {
      throw new IllegalArgumentException();
    }

    WebElement rootNode = getRootNode();

    if (isContentTreeReady()) {
      String pathNoRoot = getPathWithoutRootNode(path);

      if (pathNoRoot.isEmpty() || "/".equals(pathNoRoot)) {
        expandLastNode(rootNode);
      } else {
        String[] nodesNames = convertToNodeArray(pathNoRoot);
        WebElement node = rootNode;
        for (int i = 0; i < nodesNames.length; i++) {
          node = findNode(node, nodesNames[i]);
          if (i == nodesNames.length - 1) {
            expandLastNode(node);
          } else {
            expandMiddleNode(node);
          }
        }
      }
    } else {
      throw new IllegalStateException("AemContentTree was not ready");
    }
  }

  private void expandMiddleNode(WebElement node) {
    clickNodeLabelAndWaitForClass(node, MIDDLE_NODE_CLASS);
  }

  private void expandLastNode(WebElement node) {
    clickNodeLabelAndWaitForClass(node, LAST_NODE_CLASS);
  }

  private String getPathWithoutRootNode(String path) {
    return path.replaceFirst(getRootNodeContent().getText(), StringUtils.EMPTY);
  }

  private WebElement getRootNodeContent() {
    return getRootNode().findElement(By.cssSelector(".x-tree-node-el"));
  }

  private WebElement getNodeContent(WebElement node, By by) {
    return node.findElement(by);
  }

  private WebElement getRootNode() {
    return currentScope.findElement(By.cssSelector(".x-tree-node"));
  }

  private boolean isContentTreeReady() {
    return webElementUtils
        .hasAttributeWithValue(getRootNodeContent().findElement(By.cssSelector(".x-tree-ec-icon")),
            "class", "x-tree-elbow-end-minus");
  }

  private String[] convertToNodeArray(String path) {
    return removeLeadingSlash(path).split("/");
  }

  private boolean pathHasLeadingSlash(String path) {
    return path.charAt(0) == '/';
  }

  private String removeLeadingSlash(String path) {
    if (pathHasLeadingSlash(path)) {
      return path.substring(1);
    }
    return path;
  }

  private void clickNodeLabelAndWaitForClass(final WebElement node, final String expectedCssClass) {
    WebElement clickableArea = getNodeContent(node, By.cssSelector("div > a > span"));

    bobcatWait.withTimeout(Timeouts.SMALL)
        .until(ExpectedConditions.elementToBeClickable(clickableArea));
    bobcatWait.withTimeout(Timeouts.SMALL).until(driver -> {
      clickableArea.click();
      return getNodeContent(node, By.cssSelector("div")).getAttribute(CSS_CLASS_ATTRIBUTE_NAME)
          .contains(expectedCssClass);
    });
  }

  private WebElement findNode(WebElement parentElement, String nodeText) {
    return getNodeContent(parentElement, By.xpath(String.format(".//ul/li[div/a/span[text()=%s]]",
        XpathUtils.quote(nodeText))));
  }

}
