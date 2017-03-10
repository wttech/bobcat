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
package com.cognifide.qa.bb.aem.page;


import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents page on author instance.
 */
public abstract class AuthorPage extends AbstractPage {

  private static final Logger LOG = LoggerFactory.getLogger(AuthorPage.class);

  @Inject
  @Named(AemConfigKeys.AUTHOR_URL)
  private String authorUrl;

  /**
   * @return Full URL of the page which means: domain plus "content path".
   */
  @Override
  public String getFullUrl() {
    return authorUrl + getContentPath();
  }

  /**
   * Tries opening the page with default {@link AemConfigKeys#PAGE_TITLE_TIMEOUT} and additional
   * refreshing if the page was not opened with the first retry. Each next refresh will take
   * {@link AemConfigKeys#PAGE_TITLE_TIMEOUT} for verification if page was loaded.
   *
   * @param timeoutForRefreshing additional timeout (in seconds) for page refreshing.
   * @return <code>true</code> if page is loaded
   */
  public boolean openPageWithRefresh(int timeoutForRefreshing) {
    webDriver.get(getFullUrl());
    boolean success = isDisplayed();

    if (!success) {
      success =
          webElementUtils.isConditionMet(driver -> {
            LOG.debug("Error while loading page. Refreshing...");
            webDriver.navigate().refresh();
            return isDisplayed();
          }, timeoutForRefreshing);
    }
    return success;
  }
}
