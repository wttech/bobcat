package com.cognifide.bdd.demo.po.touchui;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class CreditCardComponent {

  public static final String CSS = ".section.creditcard";

  public static final String CARD_TYPE_SELECT_ID = "new_form_test-credit-card-type";

  @FindBy(tagName = "label")
  private WebElement label;

  @FindBy(id = CARD_TYPE_SELECT_ID)
  private WebElement cardTypeSelect;

  public String getLabelText() {
    return label.getText();
  }

  public WebElement getCardTypeSelect() {
    return cardTypeSelect;
  }
}
