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

import com.cognifide.qa.bb.api.actions.ActionData;

/**
 * {@link ActionData} for {@link CreatePageAction}
 */
public class CreatePageActionData implements ActionData {

  private String pageName;

  private String template;

  private String title;

  public CreatePageActionData(String template, String title, String pageName) {
    this.pageName = pageName;
    this.template = template;
    this.title = title;
  }

  public String getPageName() {
    return pageName;
  }

  public String getTemplate() {
    return template;
  }

  public String getTitle() {
    return title;
  }
}
