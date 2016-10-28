package com.cognifide.qa.test.pageobjects.qualifier.findpageobject;

import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class MasterPage {

  @FindPageObject
  private Drinks drinks;

  public Drinks getDrinks() {
    return drinks;
  }
}
