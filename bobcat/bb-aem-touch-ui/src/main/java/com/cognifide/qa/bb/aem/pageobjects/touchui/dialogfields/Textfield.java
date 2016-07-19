package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldType;

@PageObject
public class Textfield implements DialogField {

  @FindBy(css = ".coral-Textfield")
  private WebElement input;

  @Override
  public void setValue(Object value) {
    input.clear();
    input.sendKeys(String.valueOf(value));
  }

  @Override
  public FieldType getType() {
    return FieldType.TEXTFIELD;
  }
}
