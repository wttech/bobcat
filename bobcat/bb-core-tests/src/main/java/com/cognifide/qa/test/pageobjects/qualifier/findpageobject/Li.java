package com.cognifide.qa.test.pageobjects.qualifier.findpageobject;

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject(css = "li")
public class Li {

  @Inject
  @CurrentScope
  private WebElement scope;

  public String getText() {
    return scope.getText();
  }
}
