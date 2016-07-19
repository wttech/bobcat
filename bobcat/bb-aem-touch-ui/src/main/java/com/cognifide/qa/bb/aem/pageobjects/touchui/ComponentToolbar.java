package com.cognifide.qa.bb.aem.pageobjects.touchui;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.util.Conditions;
import com.google.inject.Inject;

@PageObject
public class ComponentToolbar {

  public static final String CSS = "#EditableToolbar";

  private static final String BUTTON_XPATH_TEMPLATE = ".//button[contains(@title, '%s')]";

  @Inject
  private Conditions conditions;

  @Inject
  @CurrentScope
  private WebElement toolbar;

  public void clickOption(ToolbarOptions option) {
    By locator = By.xpath(String.format(BUTTON_XPATH_TEMPLATE, option.getTitle()));
    toolbar.findElement(locator).click();
  }

  public void verifyIsDisplayed() {
    conditions.verify(visibilityOf(toolbar));
  }
}
