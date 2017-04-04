package com.cognifide.qa.test.pageobjects.qualifier.ignorecache;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.IgnoreCache;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class IgnoreCachePage {

  @CacheLookup
  @IgnoreCache
  @FindBy(css = ".notcached")
  private WebElement testElement;

  @CacheLookup
  @FindBy(css = ".cached")
  private WebElement cachedTestElement;

  public void typeIntoTextField(String text) {
    testElement.sendKeys(text);
  }

  public void typeIntoCachedTextField(String text) {
    cachedTestElement.sendKeys(text);
  }
}
