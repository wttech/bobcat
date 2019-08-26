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

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import com.cognifide.qa.bb.email.connector.EmailException;
import com.cognifide.qa.bb.email.constants.EmailConfigKeys;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Simple class for sending emails. It uses org.apache.commons.mail library.
 */
@Singleton
public class EmailSender {

  private String username;

  private String password;

  private String smtpServer;

  private int smtpPort;

  private boolean secure;

  @AssistedInject
  public EmailSender(@Assisted String id, EmailConfigFactory factory) {
    EmailConfig emailConfig = factory.create(id);
    this.username = emailConfig.getParameter(EmailConfigKeys.EMAIL_USERNAME);
    this.password = emailConfig.getParameter(EmailConfigKeys.EMAIL_PASSWORD);
    this.smtpServer = emailConfig.getParameter(EmailConfigKeys.SMTP_SERVER_ADDRESS);
    this.smtpPort = emailConfig.getParameter(Integer.class, EmailConfigKeys.SMTP_SERVER_PORT);
    this.secure = emailConfig.getParameter(Boolean.class, EmailConfigKeys.SMTP_SERVER_SECURE);
  }

  public void sendEmail(final EmailData emailData) {
    try {
      Email email = new SimpleEmail();
      email.setHostName(smtpServer);
      email.setSmtpPort(smtpPort);
      email.setAuthenticator(new DefaultAuthenticator(username, password));
      email.setSSLOnConnect(secure);
      email.setFrom(emailData.getAddressFrom());
      email.setSubject(emailData.getSubject());
      email.setMsg(emailData.getMessageContent());
      email.addTo(emailData.getAddressTo());
      email.send();
    } catch (org.apache.commons.mail.EmailException e) {
      throw new EmailException(e);
    }
  }
}
