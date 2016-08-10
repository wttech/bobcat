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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for IMAP.
 */
public class ImapConnector implements Connector {

  private static final Logger LOG = LoggerFactory.getLogger(ImapConnector.class);
  private final ConnectorConfig configuration;
  private Folder folder;
  private Store store;

  /**
   * Constructor. Initializes IMAP connector.
   *
   * @param configuration Mailbox configuration
   */
  public ImapConnector(ConnectorConfig configuration) {
    this.configuration = configuration;
  }

  @Override
  public void connect() {
    connectWithProtocol("imap");
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
      LOG.error("error when closing e-mail client connection", e);
      throw new ConnectorException(e);
    }

  }

  protected void connectWithProtocol(String protocol) {
    try {
      Properties properties = new Properties();
      properties.setProperty("mail.store.protocol", protocol);
      Session session = Session.getDefaultInstance(properties, null);
      store = session.getStore(protocol);
      store.connect(configuration.getServer(), configuration.getPort(),
          configuration.getUsername(),
          configuration.getPassword());
      folder = store.getFolder(configuration.getFolderName());
      folder.open(Folder.READ_WRITE);

    } catch (MessagingException e) {
      LOG.error("error - cannot connect to mail server", e);
      throw new ConnectorException(e);
    }
  }
}
