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
package com.cognifide.qa.bb.email.connector;

import com.cognifide.qa.bb.email.constants.EmailConfigKeys;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * This class configures email connector using data from property file.
 */
@Singleton
public class ConnectorConfig {

  @Inject
  @Named(EmailConfigKeys.EMAIL_USERNAME)
  private String username;

  @Inject
  @Named(EmailConfigKeys.EMAIL_PASSWORD)
  private String password;

  @Inject
  @Named(EmailConfigKeys.EMAIL_SERVER_PORT)
  private int port;

  @Inject
  @Named(EmailConfigKeys.EMAIL_FOLDER_NAME)
  private String folderName;

  @Inject
  @Named(EmailConfigKeys.EMAIL_SERVER_ADDRESS)
  private String server;

  /**
   * @return Name of inbox folder.
   */
  public String getFolderName() {
    return folderName;
  }

  /**
   * @return Inbox username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * @return Inbox password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * @return Server address.
   */
  public String getServer() {
    return server;
  }

  /**
   * @return Server port.
   */
  public int getPort() {
    return port;
  }
}
