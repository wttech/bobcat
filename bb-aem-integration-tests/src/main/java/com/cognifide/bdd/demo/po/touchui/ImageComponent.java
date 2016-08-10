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
package com.cognifide.bdd.demo.po.touchui;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class ImageComponent {

  public static final String CSS = ".image";

  @FindBy(tagName = "img")
  private WebElement img;

  @FindBy(tagName = "small")
  private WebElement description;

  @FindBy(tagName = "script")
  private WebElement imgScript;

  @FindBy(tagName = "a")
  private WebElement linkTo;

  public String getImgScript() {
    return imgScript.getAttribute(HtmlTags.Properties.OUTER_HTML);
  }

  public String getImagePath() {
    return StringUtils.substringBetween(getImgScript(), "imageAsset: \"", "\"");
  }

  public String getLinkTo() {
    return linkTo.getAttribute(HtmlTags.Attributes.HREF);
  }

  public String getTitle() {
    return img.getAttribute(HtmlTags.Attributes.TITLE);
  }

  public String getAltText() {
    return img.getAttribute(HtmlTags.Attributes.ALT);
  }

  public String getDescription() {
    return description.getText();
  }

}
