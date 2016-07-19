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
package com.cognifide.bdd.demo.po.touchui;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.data.componentproperties.Property;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class TitleComponent {

  public static final String CSS = ".atom-title";

  @Inject
  @CurrentScope
  private WebElement component;

  @FindBy(css = "h2")
  private WebElement title;

  public String getTitle() {
    return title.getText();
  }

  @Property("title:title")
  public String getVariants() {
    Set<String> cssClasses = getAttributeValues(component, "class");
    cssClasses.remove(CSS.substring(1));
    return "";
  }

  private Set<String> getAttributeValues(WebElement element, String attribute) {
    return Arrays.stream(StringUtils.split(element.getAttribute(attribute))).collect(toSet());
  }
}
