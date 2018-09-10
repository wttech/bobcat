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
package com.cognifide.qa.bb.aem.core.login;

import org.openqa.selenium.Cookie;

/**
 * Interface for classes setting authentication cookies for AEM
 */
public interface AemAuthCookieFactory {

  /**
   * Creates cookie
   * @param url
   * @param login
   * @param password
   * @return
   */
  Cookie getCookie(String url, String login, String password);

  /**
   * Removes cookie
   * @param url
   */
  void removeCookie(String url);
}
