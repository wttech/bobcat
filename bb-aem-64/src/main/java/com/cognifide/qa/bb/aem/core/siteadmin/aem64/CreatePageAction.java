/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.siteadmin.aem64;

import com.cognifide.qa.bb.aem.core.siteadmin.SiteAdminAction;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.openqa.selenium.WebDriver;

@PageObject
public class CreatePageAction implements SiteAdminAction<CreatePageActionData> {

  public static final String PAGE_CREATE = "pageCreateAction";

  @Inject
  @Named("author.url")
  private String authorUrl;

  @Inject
  private WebDriver webDriver;

  @FindPageObject
  private SiteToolbar toolbar;

  @Override
  public String getActionName() {
    return PAGE_CREATE;
  }

  @Override
  public void action(CreatePageActionData actionData) {
    webDriver.get(authorUrl+"/sites.html");
    toolbar.create();
  }
}
