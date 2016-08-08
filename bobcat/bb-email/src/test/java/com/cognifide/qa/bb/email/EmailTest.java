/*-
 * #%L
 * Bobcat Parent
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
    return Arrays.asList(new Object[][] {
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
    for (EmailData emailData : sentEmails) {
      sender.sendEmail(emailData);
    }

    client.connect();

    List<EmailData> receivedEmails = client.getLatest(emailsNumber);
    client.removeLastEmails(emailsNumber);
    client.close();

    Assert.assertTrue("email data objects should be equal", sentEmails.equals(receivedEmails));
  }
}
