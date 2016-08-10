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
package com.cognifide.qa.bb.aem.dialog.classic.field;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.utils.FieldValuesSetter;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Represents a list field in a AemDialog.
 */
@PageObject
@Frame("$cq")
public class AemList implements Iterable<AemListItem> {

  @Inject
  private WebDriver webDriver;

  @Inject
  @Named(ConfigKeys.WEBDRIVER_DEFAULT_TIMEOUT)
  private int defaultTimeout;

  @FindBy(xpath = ".//button[contains(@class,'cq-multifield-add')]")
  private WebElement addItemButton;

  /**
   * unmodifiableList list proxy to web elements
   */
  @FindBy(xpath = ".//*[contains(@class, 'cq-multifield-item')]/..")
  private List<AemListItem> aemListItems;

  @Inject
  private BobcatWait bobcatWait;

  /**
   * Returns item from list, selected by index. Indices start from 0.
   *
   * @param index item index
   * @return This AemList instance.
   */
  public AemListItem getItem(int index) {
    return aemListItems.get(index);
  }

  /**
   * Selects list item by index and sets the value for the field identified by label.
   *
   * @param fieldValue field value
   * @param index item index
   * @param fieldLabel field label
   * @param dialogFieldType dialog field type
   * @return This AemList instance.
   */
  public AemList setItemValue(String fieldValue, int index, String fieldLabel,
      Class<?> dialogFieldType) {
    AemListItem item = getItemAddIfDoesntExist(index);
    FieldValuesSetter.setFieldValue(item.getField(fieldLabel, dialogFieldType), fieldValue);
    return this;
  }

  /**
   * Gets value of the field identified by label.
   *
   * @param index item index
   * @param fieldLabel field label
   * @param dialogFieldType dialog field typ
   * @return item value
   */
  public String getItemValue(int index, String fieldLabel, Class<?> dialogFieldType) {
    AemListItem item = getItemAddIfDoesntExist(index);
    return FieldValuesSetter.getFieldValue(item.getField(fieldLabel, dialogFieldType));
  }

  /**
   * Selects item by index and removes it from the list.
   * <p>
   * Do not use it in loop, because each removal causes creating of new collection with size -1. So
   * it is easy to get IndexOutOfBoundsException.
   *
   * @param index item index
   * @return This AemList instance.
   */
  public AemList removeItem(int index) {
    AemListItem item = getItem(index);
    return removeItem(item);
  }

  /**
   * Removes the item from the list and waits for 1 second.
   *
   * @param item list item
   * @return This AemList instance.
   */
  public AemList removeItem(final AemListItem item) {
    item.remove();
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> !aemListItems.contains(item), 2);
    return this;
  }

  /**
   * Clears the list.
   *
   * @return This AemList instance.
   */
  public AemList clear() {
    for (AemListItem item : this) {
      removeItem(item);
    }
    return this;
  }

  /**
   * Adds a single item at the end of the list.
   *
   * @return Newly created AemListItem.
   */
  public AemListItem addItem() {
    addItemButton.click();
    return getLastItem();
  }

  /**
   * @return If the list contains any element, returns last item in the list. Otherwise null.
   */
  public AemListItem getLastItem() {
    int size = size();
    if (size > 0) {
      return getItem(size - 1);
    }
    return null;
  }

  /**
   * @return Number of items in the list.
   */
  public int size() {
    webDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    int size = aemListItems.size();
    webDriver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
    return size;
  }

  @Override
  public Iterator<AemListItem> iterator() {
    return aemListItems.iterator();
  }

  private AemListItem getItemAddIfDoesntExist(int index) {
    int size = size();
    int searchedIndex = index + 1;
    while (size < searchedIndex) {
      addItem();
      size++;
    }
    return getItem(searchedIndex - 1);
  }
}
