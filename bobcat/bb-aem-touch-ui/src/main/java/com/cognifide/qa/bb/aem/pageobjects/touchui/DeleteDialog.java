package com.cognifide.qa.bb.aem.pageobjects.touchui;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.util.Conditions;
import com.google.inject.Inject;

@PageObject
public class DeleteDialog {

  public static final String CSS = "[class='coral-Modal']";

  @Inject
  private Conditions conditions;

  @FindBy(css = "button.cq-deleteconfirm")
  private WebElement deleteButton;

  public void confirmDelete() {
    By dialogLocator = By.cssSelector(CSS);
    conditions.verifyPostAjax(visibilityOfElementLocated(dialogLocator));
    deleteButton.click();
    conditions.verifyPostAjax(invisibilityOfElementLocated(dialogLocator));
  }
}
