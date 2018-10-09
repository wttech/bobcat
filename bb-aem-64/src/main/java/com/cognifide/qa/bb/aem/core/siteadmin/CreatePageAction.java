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
package com.cognifide.qa.bb.aem.core.siteadmin;

import com.cognifide.qa.bb.aem.core.siteadmin.internal.SiteToolbar;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

@PageObject
public class CreatePageAction implements SiteAdminAction<CreatePageActionData> {

  public static final String PAGE_CREATE = "pageCreateAction";

  @Inject
  private WebDriver webDriver;

  @FindPageObject
  private SiteToolbar toolbar;

  @Override
  public String getActionName() {
    return PAGE_CREATE;
  }

  @Override
  @Step("Create page {actionData.pageName}")
  public void action(CreatePageActionData actionData) {
    toolbar.create("Page");
  }
}
