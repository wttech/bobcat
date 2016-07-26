package com.cognifide.bdd.demo.po.touchui.carousel;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class CarouselItem {

  public static final String CSS = ".cq-carousel-banner-item";

  @FindBy(tagName = "img")
  private WebElement image;

  @FindBy(tagName = "a")
  private WebElement anchor;

  public String getHref() {
    return anchor.getAttribute(HtmlTags.Attributes.HREF);
  }

  public String getTitle() {
    return anchor.getAttribute(HtmlTags.Attributes.TITLE);
  }

  public String getImageSrc() {
    return image.getAttribute(HtmlTags.Attributes.SRC);
  }

  public String getImageAlt() {
    return image.getAttribute(HtmlTags.Attributes.ALT);
  }

  public String getImageStyle() {
    return image.getAttribute(HtmlTags.Attributes.STYLE);
  }
}
