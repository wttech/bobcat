/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.aem.touch.siteadmin.aem61;

import java.util.ArrayList;
import java.util.List;

import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class MovePageWizard extends CoralWizard {

  private static final String COOKIE_VAL_PREFIX = "o%3Awidth%3Dn%253A240%5EselectedPath%3Ds%253A";

  private static final String COOKIE_NAME = "ys-cq-siteadmin-tree";
  private static final String BREADCRUMBS_SELECTOR = "a.endor-Crumbs-item";
  public static final String PAGES_SELECTOR =
      "div.foundation-collection-container article.card-page";

  @FindBy(css = "nav.endor-Crumbs")
  @Global
  private WebElement browsePathBreadcrumb;

  @Inject
  private WebDriver driver;

  @CurrentScope
  @Inject
  private WebElement currentScope;

  @Inject
  private BobcatWait wait;

  public void moveToDestination(String destination) {
    this.next();
    browseToDestination(destination);
    this.next();
  }

  private void browseToDestination(String destination) {
    ArrayList<String> destinationTree = getDestinationTree(destination);

    browseToContentRoot();
    String actualPath = "/content";

    int iteration = 1;
    while (!actualPath.equals(destination) && iteration < destinationTree.size()) {
      String nextPath = actualPath.concat("/" + destinationTree.get(iteration));
      List<WebElement> pages = getPagesList();
      pages
          .stream()
          .filter(n -> n.getAttribute("data-path").equals(nextPath))
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Requested page not found in current scope"))
          .click();

      actualPath = nextPath;
      iteration++;
    }
  }

  private List<WebElement> getPagesList() {
    return currentScope.findElements(By.cssSelector(PAGES_SELECTOR));
  }

  private void browseToContentRoot() {
    List<WebElement> crumbs =
        browsePathBreadcrumb.findElements(By.cssSelector(BREADCRUMBS_SELECTOR));
    crumbs.get(0).click();
  }

  private ArrayList<String> getDestinationTree(String destination) {
    ArrayList<String> destinationPath = Lists.newArrayList(destination.split("/"));

    if (destination.startsWith("/")) {
      destinationPath.remove(0);
    }
    return destinationPath;
  }

}
