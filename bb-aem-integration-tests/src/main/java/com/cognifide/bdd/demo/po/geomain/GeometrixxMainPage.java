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
package com.cognifide.bdd.demo.po.geomain;

import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.page.AuthorPage;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
@Frame("$cq")
public class GeometrixxMainPage extends AuthorPage {

  private static final String PAGE_TITLE = "English";

  private static final String EXAMPLE_PAGE_URL = "/cf#/content/geometrixx/en.html";

  @FindBy(css = ".cq-element-carousel")
  private CarouselComponent carouselComponent;

  @Override
  public String getContentPath() {
    return EXAMPLE_PAGE_URL;
  }

  @Override
  public String getPageTitle() {
    return PAGE_TITLE;
  }

  public CarouselComponent getCarouselComponent() {
    return carouselComponent;
  }

}
