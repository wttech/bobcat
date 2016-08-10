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
package com.cognifide.bdd.demo.po.feedback;

import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.page.AuthorPage;
import com.cognifide.qa.bb.aem.ui.parsys.AemParsys;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
@Frame("$cq")
public class FeedbackPage extends AuthorPage {

  private static final String URL = "/cf#/content/geometrixx/en/toolbar/feedback.html";

  private static final String PAGE_TITLE = "Feedback";

  @FindBy(css = ".cq-element-par_470002")
  private TextFieldComponent textFieldComponent;

  @FindBy(css = ".cq-element-par_47title")
  private TitleComponent titleComponent;

  @FindBy(css = ".cq-element-par_470000")
  private RichtextComponent richtextComponent;

  @FindBy(css = ".cq-element-par_470001")
  private StartFormComponent startFormComponent;

  @FindBy(css = "div.body_container .parsys")
  private AemParsys mainParsys;

  @Override
  public String getContentPath() {
    return URL;
  }

  @Override
  public String getPageTitle() {
    return PAGE_TITLE;
  }

  public TextFieldComponent getTextFieldComponent() {
    return textFieldComponent;
  }

  public TitleComponent getTitleComponent() {
    return titleComponent;
  }

  public RichtextComponent getRichtextComponent() {
    return richtextComponent;
  }

  public StartFormComponent getStartFormComponent() {
    return startFormComponent;
  }

  public AemParsys getMainParsys() {
    return mainParsys;
  }
}
