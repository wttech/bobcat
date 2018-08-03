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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.cognifide.qa.bb.email.helpers.EmailDataGenerator;
import com.cognifide.qa.bb.module.PropertiesLoaderModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

@RunWith(Parameterized.class)
public class EmailTest {

  private final String propertiesFileName;

  @Inject
  private EmailClient client;

  @Inject
  private EmailSender sender;

  @Inject
  private EmailDataGenerator dataGenerator;

  @Inject
  private MailServer mailServer;

  public EmailTest(String propertiesFileName) {
    this.propertiesFileName = propertiesFileName;
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][]{
        {"email.imap.properties"},
        {"email.imaps.properties"},
        {"email.pop3s.properties"},
    });
  }

  @Before
  public void setUpClass() {
    Injector injector = Guice.createInjector(new PropertiesLoaderModule(propertiesFileName),
        new EmailModule());
    injector.injectMembers(this);
    mailServer.start();
  }

  @After
  public void tearDown() {
    mailServer.stop();
  }

  @Test
  public void canReceiveLatestEmails() {
    int emailsNumber = 5;
    List<EmailData> sentEmails = dataGenerator.generateEmailData(emailsNumber);

    sentEmails.forEach(sender::sendEmail);

    client.connect();

    List<EmailData> receivedEmails = client.getLatest(emailsNumber);
    client.removeLastEmails(emailsNumber);
    client.close();

    Assert.assertEquals("email data objects should be equal", sentEmails, receivedEmails);
  }

  @Test
  public void canReceiveMailBySubject() {
    String subject = "test";
    EmailData sentEmail = dataGenerator.generateEmailData(subject);

    sender.sendEmail(sentEmail);

    int emailsNumber = 5;
    List<EmailData> sentEmails = dataGenerator.generateEmailData(emailsNumber);
    sentEmails.forEach(sender::sendEmail);

    client.connect();

    List<EmailData> receivedEmail = client.getLatest(subject);
    client.close();

    Assert.assertTrue(receivedEmail.contains(sentEmail));

    boolean topicMatches = receivedEmail.stream()
        .map(EmailData::getSubject)
        .allMatch(subject::equals);

    Assert.assertTrue(topicMatches);
  }

  @Test
  public void canRemoveAllMailsWithSubject() {
    String subject = "test";
    EmailData subjectMail = dataGenerator.generateEmailData(subject);
    sender.sendEmail(subjectMail);

    int emailsNumber = 5;
    List<EmailData> sentEmails = dataGenerator.generateEmailData(emailsNumber);
    sentEmails.forEach(sender::sendEmail);

    client.connect();

    client.removeAllEmails(subject);

    //reconnect to apply delete of messages
    client.close();
    client.connect();

    List<EmailData> latest = client.getLatest(emailsNumber + 1);

    client.close();

    Assert.assertFalse(latest.contains(subjectMail));
    Assert.assertTrue(latest.containsAll(sentEmails));
  }

  @Test
  public void canRemoveLastEmails() {
    int emailsNumber = 5;
    int emailsToDelete = 2;
    List<EmailData> sentEmails = dataGenerator.generateEmailData(emailsNumber);
    sentEmails.forEach(sender::sendEmail);

    client.connect();

    client.removeLastEmails(emailsToDelete);

    //reconnect to apply delete of messages
    client.close();
    client.connect();

    List<EmailData> latest = client.getLatest(emailsNumber);

    client.close();

    List<EmailData> limitedList =
        sentEmails.stream()
            .limit(emailsNumber - (long) emailsToDelete)
            .collect(Collectors.toList());

    Assert.assertEquals(latest, limitedList);
  }

  @Test
  public void canRemoveAllEmails() {
    int emailsNumber = 5;
    List<EmailData> sentEmails = dataGenerator.generateEmailData(emailsNumber);
    sentEmails.forEach(sender::sendEmail);

    client.connect();

    client.removeAllEmails();

    //reconnect to apply delete of messages
    client.close();
    client.connect();

    List<EmailData> latest = client.getLatest(emailsNumber);

    client.close();

    Assert.assertTrue(latest.isEmpty());
  }
}
