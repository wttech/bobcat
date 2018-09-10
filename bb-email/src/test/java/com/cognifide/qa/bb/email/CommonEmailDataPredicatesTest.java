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

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class CommonEmailDataPredicatesTest {

  private static final LocalDateTime RECEIVED_DATE = LocalDateTime.of(
      2010, 2, 14, 17, 0);

  private EmailData sampleEmailData;

  @DataPoints(value = "isReceivedAfter")
  public static Object[][] forIsReceivedAfter() {
    return new Object[][]{
        {LocalDateTime.of(1999, 1, 1, 0, 0), true},
        {LocalDateTime.of(2010, 2, 14, 16, 59), true},
        {LocalDateTime.of(2010, 2, 14, 17, 0), false}
    };
  }

  @DataPoints(value = "containsText")
  public static Object[][] forContainsText() {
    return new Object[][]{
        {"sample", true},
        {"sample message content", true},
        {"", true},
        {"null", false},
        {"subject", false}
    };
  }

  @DataPoints(value = "emails")
  public static Object[][] forEmailTests() {
    return new Object[][]{
        // email, isSender, isRecipient
        {"alice@example.com", true, false},
        {"bob@example.com", false, true},
        {"alice@google.com", false, false},
        {"eve@example.com", false, false}
    };
  }

  @Before
  public void createObjectsForTests() {
    sampleEmailData = new EmailData();
    sampleEmailData.setAddressFrom("alice@example.com");
    sampleEmailData.setAddressTo("bob@example.com");
    sampleEmailData.setReceivedDateTime(RECEIVED_DATE);
    sampleEmailData.setSubject("sample message subject");
    sampleEmailData.setMessageContent("sample message content");
  }

  @Theory
  public void isReceivedAfter(@FromDataPoints("isReceivedAfter") Object[] dataPoint) {
    // given
    LocalDateTime dateTime = (LocalDateTime) dataPoint[0];
    boolean expected = (boolean) dataPoint[1];
    // when
    Predicate<EmailData> predicate = CommonEmailDataPredicates.isReceivedAfter(dateTime);
    boolean actual = predicate.test(sampleEmailData);
    // then
    assertEquals("is received after should return " + expected, expected, actual);
  }

  @Theory
  public void containsText(@FromDataPoints("containsText") Object[] dataPoint) {
    // given
    String textToTest = (String) dataPoint[0];
    boolean expected = (boolean) dataPoint[1];
    // when
    Predicate<EmailData> predicate = CommonEmailDataPredicates.containsText(textToTest);
    boolean actual = predicate.test(sampleEmailData);
    // then
    assertEquals("contains text should return " + expected, expected, actual);
  }

  @Theory
  public void fromAddress(@FromDataPoints("emails") Object[] dataPoint) {
    // given
    String addressToTest = (String) dataPoint[0];
    boolean isSender = (boolean) dataPoint[1];
    // when
    Predicate<EmailData> predicate = CommonEmailDataPredicates.fromAddress(addressToTest);
    boolean actual = predicate.test(sampleEmailData);
    // then
    assertEquals("checking: " + addressToTest + " as a sender should return: " + isSender,
        isSender, actual);
  }

  @Theory
  public void toAddress(@FromDataPoints("emails") Object[] dataPoint) {
    // given
    String addressToTest = (String) dataPoint[0];
    boolean isRecipient = (boolean) dataPoint[2];
    // when
    Predicate<EmailData> predicate = CommonEmailDataPredicates.toAddress(addressToTest);
    boolean actual = predicate.test(sampleEmailData);
    // then
    assertEquals("checking: " + addressToTest + " as a recipient should return: " + isRecipient,
        isRecipient, actual);
  }

}
