package com.cognifide.qa.test.pageobjects.qualifier.findpageobject;

import java.util.List;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject(css = "div[title='drinks']")
public class Drinks {

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(css = "li")
  private WebElement liWebElement;

  @FindBy(css = "li")
  private List<WebElement> liWebElementList;

  @FindPageObject
  private Li liPageObject;

  @FindPageObject
  private List<Li> liPageObjectList;

  public WebElement getLiWebElement() {
    return liWebElement;
  }

  public List<WebElement> getLiWebElementList() {
    return liWebElementList;
  }

  public Li getLiPageObject() {
    return liPageObject;
  }

  public List<Li> getLiPageObjectList() {
    return liPageObjectList;
  }

  public WebElement getCurrentScope() {
    return currentScope;
  }
}
