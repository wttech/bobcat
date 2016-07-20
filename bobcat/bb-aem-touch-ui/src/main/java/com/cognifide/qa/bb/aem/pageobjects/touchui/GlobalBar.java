package com.cognifide.qa.bb.aem.pageobjects.touchui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.util.Conditions;
import com.google.inject.Inject;

@PageObject
public class GlobalBar {

  public static final String IS_SELECTED = "is-selected";

  @Inject
  private Conditions conditions;

  @Inject
  private WebDriver webDriver;

  @FindBy(css = ".js-editor-SidePanel-toggle")
  private WebElement toggleSidePanelButton;

  @FindBy(css = "button[data-layer='Edit'][autocomplete='off']")
  private WebElement editModeButton;

  @FindBy(css = "button[data-layer='Preview']")
  private WebElement previewModeButton;

  public void toggleSidePanel() {
    toggleSidePanelButton.click();
  }

  public void switchToEditMode() {
    if (!isInEditMode()) {
      editModeButton.click();
      webDriver.navigate().refresh();
    }
  }

  public void switchToPreviewMode() {
    if (!isInPreviewMode()) {
      previewModeButton.click();
      webDriver.navigate().refresh();
    }
  }

  private boolean isInPreviewMode() {
    return conditions.classContains(previewModeButton, IS_SELECTED);
  }

  private boolean isInEditMode() {
    return conditions.classContains(editModeButton, IS_SELECTED);
  }
}
