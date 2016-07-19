package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.data.componentconfigs.FieldType;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class Checkbox implements DialogField {

  @FindBy(css = ".coral-Checkbox-input")
  private WebElement checkbox;

  public void select() {
    checkbox.click();
  }

  @Override
  public void setValue(Object value) {
    if (Boolean.valueOf(String.valueOf(value))) {
      select();
    }
  }

  @Override
  public FieldType getType() {
    return FieldType.CHECKBOX;
  }
}
