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
package com.cognifide.qa.bb.email.helpers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.cognifide.qa.bb.email.EmailData;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class EmailDataGenerator {

  private static final int RADIX_FOR_RANDOM_STRING_GENERATION = 32;

  private static final int NUMBER_OF_BITS = 130;

  @Inject
  @Named("email.address")
  private String addressTo;

  @Inject
  @Named("email.address")
  private String addressFrom;

  public List<EmailData> generateEmailData(int number) {
    List<EmailData> result = new ArrayList<>();

    for (int i = 0; i < number; i++) {
      String message = "this is bobcat test message [" + i + "]: " + getRandomString();
      String subject = "test message " + getRandomString();
      EmailData emailData = createEmailData(subject, message);

      result.add(emailData);
    }
    return result;
  }

  public EmailData generateEmailData(String subject) {
    String message = "this is bobcat test message : " + getRandomString();
    return createEmailData(subject, message);
  }

  private EmailData createEmailData(String subject, String message) {
    EmailData emailData = new EmailData();
    emailData.setAddressFrom(addressFrom);
    emailData.setAddressTo(addressTo);
    emailData.setMessageContent(message);
    emailData.setSubject(subject);
    emailData.setReceivedDateTime(emailData.getReceivedDateTime());
    return emailData;
  }

  private String getRandomString() {
    SecureRandom random = new SecureRandom();
    return new BigInteger(NUMBER_OF_BITS, random).toString(RADIX_FOR_RANDOM_STRING_GENERATION);
  }
}
