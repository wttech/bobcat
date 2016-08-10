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

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Connector for POP3.
 */
public class Pop3SecureConnector implements Connector {

  private static final Logger LOGGER = LoggerFactory.getLogger(Pop3SecureConnector.class);

  private final ConnectorConfig configuration;

  private Folder folder;
   
  private Store store;

  /**
   * Constructor. Initializes POP3 connector.
   *
   * @param configuration Mailbox configuration
   */
  @Inject
  public Pop3SecureConnector(ConnectorConfig configuration) {
    this.configuration = configuration;
  }

  @Override
  public void connect() {
    Properties properties = new Properties();
    try {
      int port = configuration.getPort();
      String portS = String.valueOf(port);
      properties.setProperty("mail.store.protocol", "pop3");
      properties.setProperty("mail.pop3.port", portS);
      properties.setProperty("mail.pop3.socketFactory.class",
          "javax.net.ssl.SSLSocketFactory");
      properties.setProperty("mail.pop3.socketFactory.fallback", "true");
      properties.setProperty("mail.pop3.port", portS);
      properties.setProperty("mail.pop3.socketFactory.port", portS);
      URLName url = new URLName("pop3s", configuration.getServer(), port, "",
          configuration.getUsername(), configuration.getPassword());

      Session session = Session.getInstance(properties, null);
      store = session.getStore(url);
      store.connect();
      folder = store.getFolder(configuration.getFolderName());
      folder.open(Folder.READ_WRITE);
    } catch (MessagingException e) {
      LOGGER.error("error - cannot connect to mail server", e);
      throw new ConnectorException(e);
    }
  }

  @Override
  public Folder getFolder() {
    return folder;
  }

  @Override
  public void close() {
    try {
      folder.close(true);
      store.close();
    } catch (MessagingException e) {
      LOGGER.error("error when closing e-mail client connection", e);
      throw new ConnectorException(e);
    }
  }
}
