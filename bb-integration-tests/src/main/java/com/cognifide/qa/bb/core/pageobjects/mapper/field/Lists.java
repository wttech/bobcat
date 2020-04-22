/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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
package com.cognifide.qa.bb.core.pageobjects.mapper.field;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.Cached;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class Lists {

  @FindBy(css = ".fruit")
  List<WebElement> webElementList;

  @Cached
  @FindBy(css = ".fruit")
  List<WebElement> cachedWebElementList;

  @FindPageObject
  List<Item> poList;

  @Cached
  @FindPageObject
  List<Item> cachedPoList;

  @Inject
  private JavascriptExecutor javascriptExecutor;

  public List<WebElement> getWebElementList() {
    return webElementList;
  }

  public List<WebElement> getCachedWebElementList() {
    return cachedWebElementList;
  }

  public List<Item> getPoList() {
    return poList;
  }

  public List<Item> getCachedPoList() {
    return cachedPoList;
  }

  public void modifyPoListElement() {
    javascriptExecutor
        .executeScript("document.getElementsByClassName('drink')[0].textContent = 'test'");
  }

  public void addElementToWebelementList() {
    javascriptExecutor.executeScript(
        "var li = document.createElement('li'); li.appendChild(document.createTextNode('test')); li.setAttribute('class', 'fruit');document.getElementById('fruit-list').appendChild(li);");
  }

  public void addElementToPoList() {
    javascriptExecutor.executeScript(
        "var li = document.createElement('li'); li.appendChild(document.createTextNode('test')); li.setAttribute('class', 'drink');document.getElementById('drink-list').appendChild(li);");
  }
}
