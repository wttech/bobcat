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
package com.cognifide.qa.bb.provider.jcr.properties;

import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation of InstanceProperties for author environment
 */
@Singleton
public class AuthorProperties implements InstanceProperties {

  @Inject
  @Named(AemConfigKeys.AUTHOR_IP)
  private String authorIp;

  @Inject
  @Named(AemConfigKeys.AUTHOR_LOGIN)
  private String authorLogin;

  @Inject
  @Named(AemConfigKeys.AUTHOR_PASSWORD)
  private String authorPassword;

  /**
   * @return IP address of author environment
   */
  @Override
  public String getIp() {
    return authorIp;
  }

  /**
   * @return login for author environment
   */
  @Override
  public String getLogin() {
    return authorLogin;
  }

  /**
   * @return password for author environment
   */
  @Override
  public String getPassword() {
    return authorPassword;
  }
}
