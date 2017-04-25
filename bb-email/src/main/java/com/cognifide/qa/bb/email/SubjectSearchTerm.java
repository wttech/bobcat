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

import java.util.Optional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubjectSearchTerm extends SearchTerm {

  private static final Logger LOGGER = LoggerFactory.getLogger(SubjectSearchTerm.class);

  private final String subject;

  public SubjectSearchTerm(String subject) {
    this.subject = subject;
  }

  @Override
  public boolean match(Message msg) {
    return Optional.ofNullable(msg)
        .map(this::getSubjectFromMessage)
        .map(emailSubject -> StringUtils.equals(emailSubject, subject))
        .orElse(false);
  }

  private String getSubjectFromMessage(Message msg) {
    String result = null;
    try {
      result = msg.getSubject();
    } catch (MessagingException e) {
      LOGGER.error("error when getting email subject", e);
    }
    return result;
  }
}
