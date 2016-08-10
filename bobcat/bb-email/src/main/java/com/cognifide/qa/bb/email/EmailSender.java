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
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Simple class for sending emails. It uses org.apache.commons.mail library.
 */
@Singleton
public class EmailSender {

  @Inject
  @Named(EmailConfigKeys.EMAIL_USERNAME)
  private String username;

  @Inject
  @Named(EmailConfigKeys.EMAIL_PASSWORD)
  private String password;

  @Inject
  @Named(EmailConfigKeys.SMTP_SERVER_ADDRESS)
  private String smtpServer;

  @Inject
  @Named(EmailConfigKeys.SMTP_SERVER_PORT)
  private int smtpPort;

  @Inject
  @Named(EmailConfigKeys.SMTP_SERVER_SECURE)
  private boolean secure;

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
