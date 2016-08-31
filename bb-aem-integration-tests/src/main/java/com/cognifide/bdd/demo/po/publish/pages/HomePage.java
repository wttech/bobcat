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
package com.cognifide.bdd.demo.po.publish.pages;

import com.cognifide.qa.bb.qualifier.FindPageObject;
import org.openqa.selenium.support.FindBy;

import com.cognifide.bdd.demo.po.publish.components.PublishImage;
import com.cognifide.bdd.demo.po.publish.components.TopNav;
import com.cognifide.qa.bb.aem.page.PublishPage;
import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class HomePage extends PublishPage {
  private static final String CONTENT_PATH = "/content/geometrixx-outdoors/en.html";

  private static final String TITLE = "English";

  @FindPageObject
  protected TopNav topNav;

  @FindBy(css = "img.cq-dd-image")
  protected PublishImage teaserImage;

  @Override
  public String getContentPath() {
    return adjustContentPath(CONTENT_PATH);
  }

  @Override
  public String getPageTitle() {
    return TITLE;
  }

  public TopNav getTopNav() {
    return topNav;
  }

  public PublishImage getTeaserImage() {
    return teaserImage;
  }

}
