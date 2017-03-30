/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.webelement;

import org.openqa.selenium.By;

class LocatorUtil {

  static By produceLocatorFromTypeAndValue(String how, String value) {
    By byLocator;

    switch (how) {
      case "id":
        byLocator = By.id(value.trim());
        break;

      case "xpath":
        byLocator = By.xpath(value.trim());
        break;

      case "link text":
        byLocator = By.linkText(value.trim());
        break;

      case "tag name":
        byLocator = By.tagName(value.trim());
        break;

      case "class name":
        byLocator = By.className(value.trim());
        break;

      case "partial link text":
        byLocator = By.partialLinkText(value.trim());
        break;

      case "name":
        byLocator = By.name(value.trim());
        break;

      case "css selector":
        byLocator = By.cssSelector(value.trim());
        break;

      default:
        throw new RuntimeException("Invalid locator type: " + how.trim());
    }
    return byLocator;
  }
}
