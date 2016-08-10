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
package com.cognifide.qa.bb.provider.jcr.utils;

import javax.jcr.Session;

import com.cognifide.qa.bb.jcr.JcrHelper;
import com.cognifide.qa.bb.jcr.JcrHelperProvider;
import com.cognifide.qa.bb.provider.jcr.properties.InstanceProperties;
import com.cognifide.qa.bb.provider.jcr.session.JcrSessionProvider;

/**
 * Abstract class for JcrUtils. Author and publish environments has unique implementation
 */
public abstract class JcrUtils {

  private JcrSessionProvider jcrSessionProvider;

  private JcrHelperProvider jcrHelperProvider;

  /**
   * @return Session instance for desired environment
   */
  public Session getSession() {
    if (jcrSessionProvider == null) {
      jcrSessionProvider = new JcrSessionProvider(getProperties());
    }
    return jcrSessionProvider.get();
  }

  /**
   * @return JcrHelper instance for desired environment
   */
  public JcrHelper getJcrHelper() {
    if (jcrHelperProvider == null) {
      jcrHelperProvider = new JcrHelperProvider(getSession());
    }
    return jcrHelperProvider.get();
  }

  /**
   * @return InstanceProperties for desired
   */
  public abstract InstanceProperties getProperties();
}
