package com.cognifide.qa.bb.aem.ui;

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


import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;

/**
 * Exposes functionality of a content tree
 */
@PageObject
public class AemContentTree {

  private static final String CSS_CLASS_ATTRIBUTE_NAME = "class";

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @Inject
  private BobcatWait bobcatWait;

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

    WebElement rootNode = currentScope.findElement(By.cssSelector(".x-tree-node"));
    WebElement rootNodeContent = rootNode.findElement(By.cssSelector(".x-tree-node-el"));
    String pathNoRoot = path.replaceFirst(rootNodeContent.getText(), StringUtils.EMPTY);

    if (pathNoRoot.isEmpty() || "/".equals(pathNoRoot)) {
      clickNodeLabelAndWaitForClass(rootNode, "x-tree-selected");
    } else {
      String[] nodesNames = convertToNodeArray(pathNoRoot);
      WebElement node = rootNode;
      for (int i = 0; i < nodesNames.length; i++) {
        node = findNode(nodesNames[i], node);
        if (i == nodesNames.length - 1) {
          clickNodeLabelAndWaitForClass(node, "x-tree-selected");
        } else {
          clickNodeLabelAndWaitForClass(node, "x-tree-node-expanded");
        }
      }
    }
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
    WebElement clickableArea = node.findElement(By.cssSelector("div > a > span"));

    bobcatWait.withTimeout(Timeouts.SMALL).until(driver -> {
      clickableArea.click();
      return node.findElement(By.cssSelector("div")).getAttribute(CSS_CLASS_ATTRIBUTE_NAME)
          .contains(expectedCssClass);
    });
  }

  private WebElement findNode(String nodeText, WebElement parentElement) {
    return parentElement.findElement(By.xpath(String.format(".//ul/li[div/a/span[text()=%s]]",
        XpathUtils.quote(nodeText))));
  }

}
