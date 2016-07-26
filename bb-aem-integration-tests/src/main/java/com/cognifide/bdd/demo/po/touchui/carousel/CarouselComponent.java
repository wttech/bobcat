package com.cognifide.bdd.demo.po.touchui.carousel;

import java.util.List;

import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class CarouselComponent {

  public static final String CSS = ".parbase.section.carousel.list";

  @FindBy(css = CarouselItem.CSS)
  private List<CarouselItem> items;

  public List<CarouselItem> getItems() {
    return items;
  }

  public int getSize() {
    return items.size();
  }
}
