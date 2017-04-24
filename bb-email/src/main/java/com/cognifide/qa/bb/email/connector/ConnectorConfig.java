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

import com.cognifide.qa.bb.email.EmailConfig;
import com.cognifide.qa.bb.email.constants.EmailConfigKeys;

/**
 * This class configures email connector using data from property file.
 */
public class ConnectorConfig {

  private String username;

  private String password;

  private int port;

  private String folderName;

  private String server;

  public ConnectorConfig(EmailConfig emailConfig) {
    this.username = emailConfig.getParameter(EmailConfigKeys.EMAIL_USERNAME);
    this.password = emailConfig.getParameter(EmailConfigKeys.EMAIL_PASSWORD);
    this.port = emailConfig.getParameter(Integer.class, EmailConfigKeys.EMAIL_SERVER_PORT);
    this.folderName = emailConfig.getParameter(EmailConfigKeys.EMAIL_FOLDER_NAME);
    this.server = emailConfig.getParameter(EmailConfigKeys.EMAIL_SERVER_ADDRESS);
  }

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
