/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.sidepanel.internal;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper that locates components in Side Panel
 */
public class ComponentTreeLocatorHelper {

  private static final Logger LOG = LoggerFactory.getLogger(ComponentTreeLocatorHelper.class);
  //Beautiful xpath that can be really broken in any new AEM version
  private static final String COMPONENT_ITEM_XPATH_FORMAT =
      "./coral-tree-item/div/div/coral-tree-item-content/span/span[@class='editor-ContentTree-itemTitle'and (text()='%s' or text()='%s: ') ]";

  /**
   * Search component on tree
   *
   * @param path          how many containers is between
   * @param componentName component name
   * @param elementNumber which component (default 0) it there is more then one
   * @param currentScope
   * @return
   */
  public static WebElement getComponentWebElement(String path, String componentName,
      int elementNumber,
      WebElement currentScope) {
    String[] containers = StringUtils.split(path, "/");
    WebElement component = currentScope;
    for (String container : containers) {
      component = setComponent(component, container);
    }

    List<WebElement> elements = component.findElement(By.cssSelector(".coral3-Tree-subTree"))
        .findElements(
            By.xpath(String.format(COMPONENT_ITEM_XPATH_FORMAT, componentName, componentName)));
    if (!elements.isEmpty()) {
      component = elements.get(elementNumber);
    }
    return component;
  }

  private static WebElement setComponent(WebElement component, String container) {
    List<WebElement> elements = component
        .findElements(By.className("coral3-Tree-item--drilldown"));
    if (!elements.isEmpty()) {
      return elements.get(calculateElementNumber(container));
    }
    return component;
  }

  private static int calculateElementNumber(String element) {
    int toReturn = 0;
    String elementNumber = StringUtils.substringBetween(element, "[", "]");
    if (null != elementNumber) {
      try {
        toReturn = Integer.parseInt(elementNumber);
      } catch (NumberFormatException e) {
        LOG.error("Error in component settings", e);
      }
    }
    return toReturn;
  }

  private ComponentTreeLocatorHelper() {
    //util
  }

}
