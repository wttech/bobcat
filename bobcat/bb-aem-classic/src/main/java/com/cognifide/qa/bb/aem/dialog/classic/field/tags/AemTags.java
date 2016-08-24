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
package com.cognifide.qa.bb.aem.dialog.classic.field.tags;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.cognifide.qa.bb.qualifier.FindPageObject;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.DialogComponent;
import com.cognifide.qa.bb.aem.dialog.classic.field.Configurable;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

/**
 * Tags window in a AemDialog.
 */
@PageObject
@Frame("$cq")
@DialogComponent("tags")
public class AemTags implements Iterable<AemTagItem>, Configurable {

  /**
   * Colon.
   */
  public static final String NAMESPACE_SEPARATOR = ":";

  /**
   * Slash.
   */
  public static final String PARENT_PATH_SEPARATOR = "/";

  private static final String TAGNAME = ".tagname";

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private WebDriver webDriver;

  @FindPageObject
  private List<AemTagItem> items;

  @FindBy(xpath = ".//input")
  private WebElement input;

  /**
   * Remove all tags from the field.
   *
   * @return This AemTags instance.
   */
  public AemTags clear() {
    for (AemTagItem item : this) {
      item.remove();
    }
    return this;
  }

  /**
   * Search for item with provided text {@link #getTagItem(String)} and then invoke remove method.
   *
   * @param text tag text
   * @return This AemTags instance.
   */
  public AemTags removeTag(String text) {
    AemTagItem item = getTagItem(text);
    if (item != null) {
      item.remove();
    }
    return this;
  }

  /**
   * Method to search for tag item. Input text is quite flexible - for example to find tag:
   * "Facebook:Test/MyTagName", the text parameter can be either "Facebook:Test/MyTagName",
   * "Test/MyTagName" or "MyTagName". Be aware that text parameter is case sensitive and in case of
   * possible many results method will return the first one. If an element has not been found then
   * null will be returned.
   *
   * @param text tag text
   * @return AemTag if found. Null otherwise.
   */
  public AemTagItem getTagItem(String text) {
    final AemTagItem result;
    if (text.contains(NAMESPACE_SEPARATOR)) {
      result = searchByIdentifier(text);
    } else if (text.contains(PARENT_PATH_SEPARATOR)) {
      result = searchByPathAndTagName(text);
    } else {
      result = searchByTagName(text);
    }
    return result;
  }

  /**
   * Adds new tag. Parameters are case sensitive.
   *
   * @param namespace      - e.g. Facebook, if null or blank default namespace is used
   * @param pathAndTagName - path to tag e.g. Location/MyTagName
   * @return This AemTags instance.
   * @throws DeniedTagException if tag is denied
   */
  public AemTags addTag(String namespace, String pathAndTagName) throws DeniedTagException {
    final String textToSend;
    if (StringUtils.isNotBlank(namespace)) {
      textToSend = namespace + NAMESPACE_SEPARATOR + pathAndTagName;
    } else {
      textToSend = pathAndTagName;
    }
    return addTag(textToSend);
  }

  /**
   * Adds a tag. Assumed format of tag attribute: Namespace:Tag1/Tag2/Tag3 or Tag1/Tag2/Tag3 if
   * creating tag in default namespace.
   *
   * @param tag e.g. Facebook:Test/MyTagName, Test/MyTagName
   * @return This AemTags instance.
   * @throws DeniedTagException if tag is denied
   */
  public AemTags addTag(String tag) throws DeniedTagException {
    input.click();
    input.sendKeys(" ");
    StringTokenizer tagElements = new StringTokenizer(tag, "/", true);
    final int tagsCount = getTagCount();

    while (tagElements.hasMoreTokens()) {
      input.sendKeys(tagElements.nextToken());
    }
    waitUntilTagCreated(tagsCount);
    AemTagItem item = getLastAddedTag();
    if (item.isDenied()) {
      throw new DeniedTagException();
    }
    return this;
  }

  @Override
  public Iterator<AemTagItem> iterator() {
    return items.iterator();
  }

  /**
   * @return Numbers of already selected tags.
   */
  public int size() {
    return items.size();
  }

  @Override
  public void setValue(String value) {
    try {
      addTag(value);
    } catch (DeniedTagException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private AemTagItem searchByTagName(String text) {
    for (AemTagItem item : this) {
      if (item.getTagName().equals(text)) {
        return item;
      }
    }
    return null;
  }

  private AemTagItem searchByPathAndTagName(String text) {
    for (AemTagItem item : this) {
      if ((item.getParentPath() + item.getTagName()).equals(text)) {
        return item;
      }
    }
    return null;
  }

  private AemTagItem searchByIdentifier(String text) {
    for (AemTagItem item : this) {
      if (item.getIdentifier().equals(text)) {
        return item;
      }
    }
    return null;
  }

  private void waitUntilTagCreated(final int tagsCount) {
    bobcatWait.withTimeout(Timeouts.BIG).until(driver -> {
      input.sendKeys(Keys.RETURN);
      return getTagCount() == tagsCount + 1;
    }, 2);
  }

  private int getTagCount() {
    return webDriver.findElements(By.cssSelector(TAGNAME)).size();
  }

  private AemTagItem getLastAddedTag() {
    return items.get(size() - 1);
  }

}
