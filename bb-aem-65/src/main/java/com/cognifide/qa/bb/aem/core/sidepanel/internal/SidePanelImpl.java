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
package com.cognifide.qa.bb.aem.core.sidepanel.internal;

import static com.cognifide.qa.bb.constants.HtmlTags.Attributes.CLASS;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.core.component.GlobalBarImpl;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.cognifide.qa.bb.wait.Timings;
import com.google.inject.Inject;

/**
 * The default Bobcat implementation of {@link SidePanel} for AEM 6.4
 */
@PageObject(css = ".sidepanel")
public class SidePanelImpl implements SidePanel {

  private static final String IS_CLOSED = "sidepanel-closed";

  @Inject
  private DragAndDropFactory dragAndDropFactory;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private PageObjectInjector pageObjectInjector;

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(id = "assetsearch")
  private WebElement searchInput;

  @FindBy(css = ".content-panel .resultspinner")
  private WebElement resultsLoader;

  @FindBy(css = ".content-panel .card-asset")
  private List<WebElement> searchResults;

  @FindPageObject
  private SidePanelTabBar sidePanelTabBar;

  @Override
  public WebElement selectComponentToEdit(String path, String component, int elementNumber) {
    return ComponentTreeLocatorHelper
        .getComponentWebElement(path, component, elementNumber, currentScope);
  }

  @Override
  public void selectTab(String tab) {
    if (isClosed()) {
      pageObjectInjector.inject(GlobalBarImpl.class).toggleSidePanel();
    }
    sidePanelTabBar.switchTab(tab);
  }

  @Override
  public Draggable searchForAsset(String asset) {
    if (isClosed()) {
      pageObjectInjector.inject(GlobalBarImpl.class).toggleSidePanel();
    }

    verifyResultsVisible();
    searchInput.clear();
    searchInput.sendKeys(asset);
    searchInput.sendKeys(Keys.ENTER);
    verifyResultsVisible();

    return dragAndDropFactory.createDraggable(getResult(asset), FramePath.parsePath("/"));
  }

  private boolean isClosed() {
    return bobcatWait.isConditionMet(attributeContains(currentScope, CLASS, IS_CLOSED));
  }

  private void verifyResultsVisible() {
    bobcatWait.tweak(Timings.MEDIUM_EXPLICIT).until(invisibilityOf(resultsLoader));

    bobcatWait.tweak(Timings.MEDIUM_EXPLICIT).ignoring(StaleElementReferenceException.class)
        .until(visibilityOfAllElements(searchResults));
  }

  private WebElement getResult(String asset) {
    return searchResults.stream() //
        .filter(element -> StringUtils
            .contains(element.getAttribute(HtmlTags.Attributes.DATA_PATH), asset)) //
        .findFirst() //
        .orElseThrow(() -> new IllegalStateException(asset + " asset was not found"));
  }
}
