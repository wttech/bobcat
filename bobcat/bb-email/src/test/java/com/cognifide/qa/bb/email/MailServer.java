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
package com.cognifide.qa.bb.email;

import java.security.Security;

import com.cognifide.qa.bb.email.connector.ConnectorConfig;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;

public class MailServer {

  @Inject
  private ConnectorConfig config;

  @Inject
  @Named("email.address")
  private String address;

  private GreenMail mailServer;

  public void start() {
    Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
    // from javadoc
    // smtp 3025
    // smtps 3465
    // pop3 3110
    // pop3s 3995
    // imap 3143
    // imaps 3993
    mailServer = new GreenMail();
    mailServer.start();
    mailServer.setUser(address, config.getUsername(), config.getPassword());
  }

  public void stop() {
    mailServer.stop();
  }
}
