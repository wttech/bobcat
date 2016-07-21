package com.cognifide.bdd.demo.po.touchui;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class ImageComponent {

  public static final String CSS = ".image";

  @Inject
  @CurrentScope
  private WebElement component;

  @FindBy(tagName = "img")
  private WebElement img;

  @FindBy(tagName = "small")
  private WebElement description;

  @FindBy(tagName = "script")
  private WebElement imgScript;

  @FindBy(tagName = "a")
  private WebElement linkTo;

  public String getImgScript() {
    return imgScript.getAttribute("outerHTML");
  }

  public String getImagePath() {
    return StringUtils.substringBetween(getImgScript(), "imageAsset: \"", "\"");
  }

  public String getLinkTo() {
    return linkTo.getAttribute("href");
  }

  public String getTitle() {
    return img.getAttribute("title");
  }

  public String getAltText() {
    return img.getAttribute("alt");
  }

  public String getDescription() {
    return description.getText();
  }

}
