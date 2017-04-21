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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.email.connector.Connector;
import com.google.inject.Inject;

/**
 * This class represents email client.
 */
public class EmailClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailClient.class);

  private final Connector connection;

  private final EmailDataFactory emailDataFactory;

  private Folder folder;

  /**
   * @param connection MailBox protocol connection.
   * @param emailDataFactory Email data factory
   */
  @Inject
  public EmailClient(Connector connection, EmailDataFactory emailDataFactory) {
    this.connection = connection;
    this.emailDataFactory = emailDataFactory;
  }

  /**
   * Connects with mailbox.
   */
  public void connect() {
    connection.connect();
    this.folder = connection.getFolder();
  }

  /**
   * Closes connection with mailbox.
   */
  public void close() {
    connection.close();
  }

  /**
   * @param subject Email message subject.
   * @return List of EmailData with subject from parameter.
   */
  public List<EmailData> getLatest(String subject) {
    Collection<Message> messageList = getMessageList(subject);
    return createEmailDataList(messageList);
  }

  /**
   * @param limit Messages amount.
   * @return Last n EmailData from inbox.
   */
  public List<EmailData> getLatest(int limit) {
    Collection<Message> messageList = getMessageList(limit);
    return createEmailDataList(messageList);
  }

  /**
   * Deletes all emails.
   */
  public void removeAllEmails() {
    try {
      removeLastEmails(folder.getMessageCount());
    } catch (MessagingException e) {
      LOGGER.error("error when removing all emails", e);
    }
  }

  /**
   * Deletes all emails with subject.
   *
   * @param subject Subject of message.
   */
  public void removeAllEmails(String subject) {
    getMessageList(subject)
        .forEach(this::removeEmail);
  }

  /**
   * Deletes last n emails.
   *
   * @param limit emails amount.
   */
  public void removeLastEmails(int limit) {
    getMessageList(limit)
        .forEach(this::removeEmail);
  }

  /**
   * @param subject Email message subject.
   * @return Array of Messages with subject from parameter.
   */
  protected List<Message> getMessageList(String subject) {

    Message[] messages = {};
    try {
      messages = folder.search(new SubjectSearchTerm(subject));
    } catch (MessagingException e) {
      LOGGER.error("error when getting email messages", e);
    }
    return Arrays.asList(messages);
  }

  /**
   * @param limit Messages amount.
   * @return Last n Messages from inbox as list.
   */
  protected List<Message> getMessageList(int limit) {
    Message[] messages = {};
    try {
      int messageCount = folder.getMessageCount();
      if (messageCount > 0) {
        int startIndex = messageCount - Math.min(limit, messageCount) + 1;
        messages = folder.getMessages(startIndex, messageCount);
      }
    } catch (MessagingException e) {
      LOGGER.error("error when getting email messages", e);
    }
    return Arrays.asList(messages);
  }

  /**
   * This method converts list of Message objects to list of EmailData.
   *
   * @param messages List of Messages.
   * @return Collection of EmailData objects.
   */
  private List<EmailData> createEmailDataList(Collection<Message> messages) {
    return messages.stream()
        .map(emailDataFactory::create)
        .collect(Collectors.toList());
  }

  /**
   * This method deletes mail
   *
   * @param message Mail reference
   */
  private void removeEmail(Message message) {
    try {
      message.setFlag(Flags.Flag.DELETED, true);
    } catch (MessagingException e) {
      LOGGER.error("error when removing  emails from server", e);
    }
  }

}
