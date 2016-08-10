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
import com.cognifide.qa.bb.constants.ConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This class represents page on publish instance.
 */
public abstract class PublishPage extends AbstractPage {

  @Inject
  @Named(AemConfigKeys.PUBLISH_URL)
  private String publishUrl;

  @Inject
  @Named(ConfigKeys.WEBDRIVER_MOBILE)
  private boolean isMobile;

  /**
   * @return Full URL of the page which means: domain plus "content path".
   */
  @Override
  public String getFullUrl() {
    return publishUrl + getContentPath();
  }

  /**
   * Removes '-mobile' from path if test does not run on mobile device.
   *
   * @param path path to adjust
   * @return correctly path
   */
  protected String adjustContentPath(String path) {
    return isMobile ? path : path.replaceFirst("-mobile", "");
  }

}
