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

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * This class represents email message.
 */
public class EmailData {

  private LocalDateTime receivedDateTime;

  private String messageContent;

  private String subject;

  private String addressFrom;

  private String addressTo;

  /**
   * @return Email content.
   */
  public String getMessageContent() {
    return messageContent;
  }

  /**
   * Sets email content.
   *
   * @param messageContent the content of email
   */
  public void setMessageContent(String messageContent) {
    this.messageContent = messageContent.trim();
  }

  /**
   * @return Email message receive DateTime.
   */
  public LocalDateTime getReceivedDateTime() {
    return receivedDateTime;
  }

  /**
   * Sets email receive DateTime.
   *
   * @param receivedDateTime received date of email
   */
  public void setReceivedDateTime(LocalDateTime receivedDateTime) {
    this.receivedDateTime = receivedDateTime;
  }

  /**
   * @return Email message subject.
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Sets email subject.
   *
   * @param subject email subject
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * @return Email sender address.
   */
  public String getAddressFrom() {
    return addressFrom;
  }

  /**
   * Sets email sender address.
   *
   * @param addressFrom sender email address
   */
  public void setAddressFrom(String addressFrom) {
    this.addressFrom = addressFrom;
  }

  /**
   * @return Email receiver address.
   */
  public String getAddressTo() {
    return addressTo;
  }

  /**
   * Sets email receiver address.
   *
   * @param addressTo receiver email address
   */
  public void setAddressTo(String addressTo) {
    this.addressTo = addressTo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, messageContent, addressTo);
  }

  // this method is needed for comparing emailData instances in tests
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    EmailData emailData = (EmailData) obj;
    return new EqualsBuilder()
        .append(subject, emailData.getSubject())
        .append(messageContent, emailData.getMessageContent())
        .append(addressTo, emailData.getAddressTo())
        .isEquals();
  }

  @Override
  public String toString() {
    return "EmailData [receivedDateTime=" + receivedDateTime + ", messageContent=" + messageContent
        + ", subject=" + subject + ", addressFrom=" + addressFrom + ", addressTo=" + addressTo
        + "]";
  }
}
