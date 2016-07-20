/*-
 * #%L
 * Bobcat Parent
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
  public String getType() {
    return FieldType.MULTIFIELD.name();
  }

  public void addField() {
    addButton.click();
  }
}
