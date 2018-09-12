/*-
 * #%L
 * Bobcat
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
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields;

import com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.configuration.MultifieldEntry;
import com.cognifide.qa.bb.exceptions.BobcatRuntimeException;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * This class represents TouchUI dialog multifield.
 */
@PageObject
public class Multifield implements DialogField {

  @FindBy(css = "button.js-coral-Multifield-add")
  private WebElement addButton;

  @FindBy(css = "li.coral-Multifield-input")
  private List<MultifieldItem> items;

  /**
   * Sets next element in dialog multifield.
   *
   * @param value yaml configuration containing list of {@link MultifieldEntry} representation.
   */
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

  /**
   * Returns MultifieldItem at declared index position
   *
   * @param index integer representing required position
   * @return MultifieldItem
   */
  public MultifieldItem getItemAtIndex(int index) {
    int itemsSize = items.size();
    if (itemsSize > index) {
      return items.get(index);
    }
    throw new BobcatRuntimeException(String
        .format("Tried to get item at index %s but there are only %s elements", index, itemsSize));
  }

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.MULTIFIELD.name();
  }

  public void addField() {
    addButton.click();
  }
}
