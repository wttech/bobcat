package com.cognifide.qa.bb.core.pageobjects.qualifier.findpageobject;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.qualifier.PageObjectInterface;
import java.util.List;
import org.openqa.selenium.WebElement;

@PageObjectInterface
public interface Item {

  public WebElement getLiWebElement();

  public List<WebElement> getLiWebElementList();

  public ListItem getLiPageObject();

  public List<ListItem> getLiPageObjectList();

  public WebElement getCurrentScope();
}