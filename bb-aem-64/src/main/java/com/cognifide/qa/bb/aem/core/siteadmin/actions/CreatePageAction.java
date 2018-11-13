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
package com.cognifide.qa.bb.aem.core.siteadmin.actions;

import com.cognifide.qa.bb.aem.core.api.ActionWithData;
import com.cognifide.qa.bb.aem.core.siteadmin.internal.SiteToolbar;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;

import io.qameta.allure.Step;

@PageObject
public class CreatePageAction implements ActionWithData<CreatePageActionData> {

  @FindPageObject
  private SiteToolbar toolbar;

  @Override
  @Step("Create page {actionData.title} with name {actionData.pageName} using {actionData.template} template")
  public void execute(CreatePageActionData data) {
    toolbar.createPage(data.getTemplate(), data.getTitle(), data.getPageName());
  }
}
