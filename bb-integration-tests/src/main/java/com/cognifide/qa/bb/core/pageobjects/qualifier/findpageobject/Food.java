package com.cognifide.qa.bb.core.pageobjects.qualifier.findpageobject;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject(css = "div[title='food']")
public class Food implements Item{

  @Inject
  @CurrentScope
  private WebElement currentScope;

  @FindBy(css = "li")
  private WebElement liWebElement;

  @FindBy(css = "li")
  private List<WebElement> liWebElementList;

  @FindPageObject
  private ListItem liPageObject;

  @FindPageObject
  private List<ListItem> liPageObjectList;

  public WebElement getLiWebElement() {
    return liWebElement;
  }

  public List<WebElement> getLiWebElementList() {
    return liWebElementList;
  }

  public ListItem getLiPageObject() {
    return liPageObject;
  }

  public List<ListItem> getLiPageObjectList() {
    return liPageObjectList;
  }

  public WebElement getCurrentScope() {
    return currentScope;
  }

}
