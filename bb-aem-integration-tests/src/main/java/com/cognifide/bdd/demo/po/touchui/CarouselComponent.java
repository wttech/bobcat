package com.cognifide.bdd.demo.po.touchui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class CarouselComponent {

  public static final String CSS = ".parbase.section.carousel.list";

  @FindBy(css = ".cq-carousel-banner-item")
  private List<WebElement> items;

  public List<WebElement> getItems() {
    return items;
  }

  public int getSize() {
    return items.size();
  }

  public String getAnchorHref(int itemIndex) {
    return items.get(itemIndex).findElement(By.tagName("a")).getAttribute(HtmlTags.Attributes.HREF);
  }
}
