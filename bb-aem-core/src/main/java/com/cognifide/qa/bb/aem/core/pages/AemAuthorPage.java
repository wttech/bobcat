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
package com.cognifide.qa.bb.aem.core.pages;

import com.cognifide.qa.bb.page.Page;
import com.google.inject.Inject;
import io.qameta.allure.Step;
import javax.inject.Named;

/**
 * Abstract class that marks page as being from AEM
 */
public class AemAuthorPage<T extends AemAuthorPage> extends Page {

  @Inject
  @Named("author.url")
  protected String authorUrl;

  /**
   * open the page in browser
   */
  @Step("Open page")
  public T open() {
    webDriver.get(authorUrl + getFullUrl());
    return (T) this;
  }

}
