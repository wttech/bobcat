package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldType;
import com.cognifide.qa.bb.aem.data.componentconfigs.MultifieldEntry;

@PageObject
public class Multifield implements DialogField {

  @FindBy(css = "button.js-coral-Multifield-add")
  private WebElement addButton;

  @FindBy(css = "li.coral-Multifield-input")
  private List<MultifieldItem> items;

  @Override
  public void setValue(Object value) {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    List<MultifieldEntry> cfg =
        mapper.convertValue(value, new TypeReference<List<MultifieldEntry>>() {
        });

    items.stream().forEach(MultifieldItem::deleteItem);

    cfg.stream().forEach(entry -> addField());

    Iterator<MultifieldItem> itemsIterator = items.iterator();
    cfg.stream().forEach(entry -> itemsIterator.next().setValue(entry));
  }

  @Override
  public FieldType getType() {
    return FieldType.MULTIFIELD;
  }

  public void addField() {
    addButton.click();
  }
}
