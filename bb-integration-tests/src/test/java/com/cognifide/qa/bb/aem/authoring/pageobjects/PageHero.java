package com.cognifide.qa.bb.aem.authoring.pageobjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject(css = ".page__hero--overlay")
public class PageHero {

  @FindBy(css = ".btn.btn--light-outline.btn--large")
  private WebElement button;

  //todo think about returning a wrapper here (QuestionTarget?) that will wrap WebElement
  // and allow easier assertions of attributes, condition validation and general state probing
  public WebElement button() {
    return button;
  }

  public String buttonText() {
    return button.getText();
  }
}
