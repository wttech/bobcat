package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields;

import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.CONTROL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldType;
import com.google.inject.Inject;

@PageObject
public class RichText implements DialogField {

  @FindBy(css = ".coral-RichText")
  private WebElement input;

  @Inject
  private Actions actions;

  @Override
  public void setValue(Object value) {
    actions.keyDown(input, CONTROL) //
        .sendKeys("a") //
        .keyUp(CONTROL) //
        .sendKeys(BACK_SPACE) //
        .sendKeys(String.valueOf(value)) //
        .perform();
  }

  @Override
  public FieldType getType() {
    return FieldType.RICHTEXT;
  }
}
