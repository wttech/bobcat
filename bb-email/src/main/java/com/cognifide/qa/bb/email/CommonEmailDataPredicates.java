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
import java.util.function.Predicate;

/**
 * This class offers several EmailData predicates.
 */
public final class CommonEmailDataPredicates {

  private CommonEmailDataPredicates() {
  }

  /**
   * A predicate that matches an email message with a given reception time.
   *
   * @param localDateTime preceding the email received attribute
   * @return Predicate for emails with received attribute after specific point in time
   */
  public static Predicate<EmailData> isReceivedAfter(final LocalDateTime localDateTime) {
    return emailData -> {
      if (emailData == null) {
        return false;
      } else {
        return emailData.getReceivedDateTime().isAfter(localDateTime);
      }
    };
  }

  /**
   * A predicate that matches an email message with given content.
   *
   * @param text to be present in email content
   * @return Predicate for emails that contain specific text in message content
   */
  public static Predicate<EmailData> containsText(final String text) {
    return emailData -> {
      if (emailData == null) {
        return false;
      } else {
        return emailData.getMessageContent().contains(text);
      }
    };
  }

  /**
   * A predicate that matches an email message with given recipient address.
   *
   * @param address of recipient
   * @return Predicate for emails with specific 'To' address
   */
  public static Predicate<EmailData> toAddress(final String address) {
    return emailData -> {
      if (emailData == null) {
        return false;
      } else {
        return emailData.getAddressTo().equals(address);
      }
    };
  }

  /**
   * A predicate that matches an email message with given sender address.
   *
   * @param address of sender
   * @return Predicate for emails with specific 'From' address
   */
  public static Predicate<EmailData> fromAddress(final String address) {
    return emailData -> {
      if (emailData == null) {
        return false;
      } else {
        return emailData.getAddressFrom().equals(address);
      }
    };
  }

}
