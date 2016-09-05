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
package com.cognifide.qa.bb.aem.touch.siteadmin.common;

public interface SiteadminChildPage {

  /**
   * Returns title of requested page
   *
   * @return String title
   */
  String getTitle();

  /**
   * Selects requested page
   *
   * @return this SiteadminChildPage
   */
  SiteadminChildPage select();

  /**
   * Returns href of requestes page
   *
   * @return String href
   */
  String getHref();

  /**
   * Returns PageModificationInfo of requested page
   *
   * @return PageModificationInfo
   */
  PageModificationInfo getModificationInfo();

  /**
   * Returns PageActivationStatus of requested page
   *
   * @return PageActivationStatus
   */
  PageActivationStatus getPageActivationStatus();

  /**
   * Clicks in the scope of page
   */
  void click();
}
