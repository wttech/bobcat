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
package com.cognifide.bdd.demo.aem;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.customersurvey.CustomerSurveyPage;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.ui.messages.AemBubbleMessage;
import com.cognifide.qa.bb.aem.ui.sidekick.AemSidekick;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemBubbleMessageTest {

  private static final String PAGE_SUCCESSFULLY_ACTIVATED_MESSAGE = "Page successfully activated";

  @Inject
  private CustomerSurveyPage page;

  @Inject
  private AemLogin aemLogin;

  @Inject
  private AemSidekick sidekick;

  @Inject
  private AemBubbleMessage bubbleMessage;

  @Before
  public void openPage() {
    aemLogin.authorLogin();
    openPageToTest();
  }

  @Test
  public void shouldProduceSameMessageAsWasExpected() {
    sidekick.activatePage();
    String bubbleMessageText = bubbleMessage.waitForAemBubbleMessage().getBubbleMessageText();
    assertThat(bubbleMessageText, is(PAGE_SUCCESSFULLY_ACTIVATED_MESSAGE));
  }

  private void openPageToTest() {
    page.open();
    assertTrue(page.isDisplayed());
  }
}
