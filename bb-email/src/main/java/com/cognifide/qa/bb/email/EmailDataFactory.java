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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.email.connector.EmailException;
import com.cognifide.qa.bb.email.constants.EmailConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This class creates EmailData.
 */
public final class EmailDataFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailDataFactory.class);

  private final Pattern addressPattern;

  @Inject
  public EmailDataFactory(
      @Named(EmailConfigKeys.EMAIL_ADDRESS_PATTERN) String addressPatternString) {
    this.addressPattern = Pattern.compile(addressPatternString);
  }

  /**
   * This method converts {@link javax.mail.Message} object to
   * {@link com.cognifide.qa.bb.email.EmailData}.
   *
   * @param message Email message.
   * @return EmailData object.
   */
  public EmailData create(Message message) {
    try {
      EmailData emailData = new EmailData();

      String messageContentString = getMessageContent(message);
      emailData.setMessageContent(messageContentString);

      String subject = getSubjectString(message);
      emailData.setSubject(subject);

      LocalDateTime receivedDateTime = getReceivedDate(message);
      emailData.setReceivedDateTime(receivedDateTime);
      emailData.setAddressFrom(getAddressFrom(message));
      emailData.setAddressTo(getAddressTo(message));

      LOGGER.debug("received email: {}", emailData);
      return emailData;
    } catch (IOException | MessagingException e) {
      throw new EmailException(e);
    }
  }

  private String getAddressTo(final Message message) throws MessagingException {
    Address address = message.getRecipients(Message.RecipientType.TO)[0];
    return matchEmailAddress(address.toString());
  }

  private String getAddressFrom(final Message message) throws MessagingException {
    Address address = message.getFrom()[0];
    return matchEmailAddress(address.toString());
  }

  private String matchEmailAddress(String text) {
    Matcher matcher = addressPattern.matcher(text);
    if (matcher.find()) {
      return matcher.group() != null ? matcher.group().replace("\"", "") : "";
    } else {
      throw new IllegalArgumentException(
          "Cant resolve address from " + text
              + ". Check your " + EmailConfigKeys.EMAIL_ADDRESS_PATTERN
              + " property configuration");
    }
  }

  private LocalDateTime getReceivedDate(Message message) throws MessagingException {
    return message.getReceivedDate() != null ? message.getReceivedDate().toInstant()
        .atZone(ZoneId.systemDefault()).toLocalDateTime() : LocalDateTime.now();
  }

  private String getSubjectString(Message message) throws MessagingException {
    return message.getSubject();
  }

  private String getMessageContent(Message message) throws IOException, MessagingException {
    String contentString = null;
    Object content = message.getContent();
    if (content instanceof Multipart) {
      StringBuilder contentBuilder = processMultipart((Multipart) content);
      contentString = contentBuilder.toString();
    } else if (message.getContentType().toLowerCase().contains("text")) {
      contentString = message.getContent().toString();
    }
    return contentString;
  }

  private StringBuilder processMultipart(final Multipart multipart)
      throws MessagingException,
      IOException {
    StringBuilder messageContent = new StringBuilder();
    Part part;
    for (int i = 0; i < multipart.getCount(); i++) {
      part = multipart.getBodyPart(i);
      if (part.getContent() instanceof Multipart) {
        messageContent.append(processMultipart((Multipart) part.getContent()));
      } else if (isPartTextLike(part)) {
        messageContent.append(part.getContent()).append('\n');
      }
    }
    return messageContent;
  }

  private boolean isPartTextLike(final Part part) throws MessagingException {
    return part.getContentType().toLowerCase().contains("text");
  }
}
