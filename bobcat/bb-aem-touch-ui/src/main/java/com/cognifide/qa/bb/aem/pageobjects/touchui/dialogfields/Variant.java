package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldType;

@PageObject
public class Variant implements DialogField {

  @FindBy(css = ".coral-Select-select")
  private WebElement select;

  @FindBy(css = "ol.coral-TagList li button")
  private List<WebElement> tagButtons;

  @Override
  public void setValue(Object value) {
    org.openqa.selenium.support.ui.Select selectElement = new org.openqa.selenium.support.ui.Select(
        select);
    List<String> values = Arrays.asList(String.valueOf(value).split(","));

    tagButtons.stream().forEach(WebElement::click);
    values.stream().forEach(selectElement::selectByVisibleText);
  }

  @Override
  public FieldType getType() {
    return FieldType.VARIANT;
  }
}
