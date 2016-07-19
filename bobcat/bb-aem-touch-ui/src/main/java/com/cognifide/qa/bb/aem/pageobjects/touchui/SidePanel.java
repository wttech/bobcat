package com.cognifide.qa.bb.aem.pageobjects.touchui;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.util.Conditions;
import com.google.inject.Inject;

@PageObject
public class SidePanel {
  public static final String CSS = "#SidePanel";

  public static final String IS_CLOSED = "sidepanel-closed";

  @Inject
  private DragAndDropFactory dragAndDropFactory;

  @Inject
  private Conditions conditions;

  @Inject
  private PageObjectInjector pageObjectInjector;

  @Inject
  @CurrentScope
  private WebElement sidePanel;

  @FindBy(css = "#assetsearch")
  private WebElement searchInput;

  @FindBy(css = ".content-panel .resultspinner")
  private WebElement resultsLoader;

  @FindBy(css = ".content-panel article.card-asset")
  private List<WebElement> searchResults;

  public boolean isClosed() {
    return conditions.classContains(sidePanel, IS_CLOSED);
  }

  public Draggable searchForAsset(String asset) {
    if (isClosed()) {
      pageObjectInjector.inject(GlobalBar.class).toggleSidePanel();
    }
    verifyResultsVisible();

    searchInput.sendKeys(asset);
    searchInput.sendKeys(Keys.ENTER);
    verifyResultsVisible();

    return dragAndDropFactory.createDraggable(getResult(asset), FramePath.parsePath("/"));
  }

  private void verifyResultsVisible() {
    conditions.optionalWait(visibilityOf(resultsLoader));
    conditions.verify(not(visibilityOf(resultsLoader)), Timeouts.MEDIUM);
    searchResults.stream().forEach(result -> conditions.verify(ignored -> {
      try {
        return result.isDisplayed();
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        return false;
      }
    }, Timeouts.MEDIUM));
  }

  private WebElement getResult(String asset) {
    return searchResults.stream() //
        .filter(element -> StringUtils.contains(element.getAttribute("data-path"), asset)) //
        .findFirst() //
        .orElseThrow(() -> new IllegalStateException(asset + " asset was not found"));
  }
}
