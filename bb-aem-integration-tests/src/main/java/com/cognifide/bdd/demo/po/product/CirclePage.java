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
package com.cognifide.bdd.demo.po.product;

import org.openqa.selenium.support.FindBy;

import com.cognifide.bdd.demo.po.feedback.TitleComponent;
import com.cognifide.qa.bb.aem.page.AuthorPage;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
@Frame("$cq")
public class CirclePage extends AuthorPage {

  private static final String PAGE_URL = "/cf#/content/geometrixx/en/products/circle.html";

  private static final String PAGE_TITLE = "Circle";

  @FindBy(css = ".container_16 .grid_16 div.title")
  private TitleComponent titleComponent;

  @Override
  public String getContentPath() {
    return PAGE_URL;
  }

  @Override
  public String getPageTitle() {
    return PAGE_TITLE;
  }

  public TitleComponent getTitleComponent() {
    return titleComponent;
  }
}
