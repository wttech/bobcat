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
package com.cognifide.bdd.demo.po.zg.components;

import org.openqa.selenium.support.FindBy;

import com.cognifide.bdd.demo.po.feedback.TitleComponent;
import com.cognifide.qa.bb.aem.page.AuthorPage;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
@Frame("$cq")
public class TitlePage extends AuthorPage {

  private static final String URL =
      "/cf#/content/zg-dev/sample-content/en_gb/home/components/title.desktop.html";

  private static final String PAGE_TITLE = "Overwritten Title";

  @FindBy(css = ".title.component.odd.first.section.cq-element-header_47title")
  private TitleComponent h1TitleComponent;

  @FindBy(css = ".title.component.even.section.cq-element-header_47title_950")
  private TitleComponent h2TitleComponent;

  @Override
  public String getContentPath() {
    return URL;
  }

  @Override
  public String getPageTitle() {
    return PAGE_TITLE;
  }

  public TitleComponent getH1TitleComponent() {
    return h1TitleComponent;
  }

  public TitleComponent getH2TitleComponent() {
    return h2TitleComponent;
  }
}
