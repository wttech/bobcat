package com.cognifide.qa.bb.aem.pageobjects.touchui;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

import org.openqa.selenium.By;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.util.Conditions;
import com.google.inject.Inject;

@PageObject
public class AuthorLoader {

  private static final By LOADER_LOCATOR = By.cssSelector( //
      ".coral-Modal-backdrop .coral-Wait.coral-Wait--center.coral-Wait--large");

  @Inject
  private Conditions conditions;

  public void verifyIsHidden() {
    conditions.verify(invisibilityOfElementLocated(LOADER_LOCATOR), Timeouts.MEDIUM);
  }
}
