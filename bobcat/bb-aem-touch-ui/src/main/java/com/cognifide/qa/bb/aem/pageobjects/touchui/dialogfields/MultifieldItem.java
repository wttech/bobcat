package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldType;
import com.cognifide.qa.bb.aem.data.componentconfigs.MultifieldEntry;
import com.cognifide.qa.bb.aem.pageobjects.touchui.DialogConfigurer;
import com.google.inject.Inject;

@PageObject
public class MultifieldItem implements DialogField {

  @Inject
  private DialogConfigurer dialogConfigurer;

  @Inject
  @CurrentScope
  private WebElement item;

  @FindBy(css = "button.coral-Multifield-remove")
  private WebElement deleteButton;

  @Override
  public FieldType getType() {
    return FieldType.MULTIFIELD_ITEM;
  }

  @Override
  public void setValue(Object value) {
    MultifieldEntry entry = (MultifieldEntry) value;
    entry.getItem().stream().forEach(this::setFieldInMultifield);
  }

  public void deleteItem() {
    deleteButton.click();
  }

  private void setFieldInMultifield(FieldConfig fieldConfig) {
    dialogConfigurer.getDialogField(item, fieldConfig.getLabel(), fieldConfig.getType())
        .setValue(fieldConfig.getValue());
  }
}
