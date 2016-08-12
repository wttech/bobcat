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
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.user.ProfilePage;
import com.cognifide.bdd.demo.suite.SmokeTests;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemTextArea;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemTextAreaTest {

  private static final String TEST_TEXT = "test";

  private static final Object TEST_TEXT_NEW_LINE = "test\ntest";

  private final TestRunner runner;

  @Inject
  private ProfilePage profilePage;

  @Inject
  private AemLogin aemLogin;

  public AemTextAreaTest() {
    runner = new TestRunner();
  }

  @Before
  public void before() {
    aemLogin.authorLogin();
    openPageToTest();
  }

  @Category(SmokeTests.class)
  @Test
  public void testClear() {
    TestTemplate template = textArea -> {
      textArea.type(TEST_TEXT);
      textArea.clear();
      assertTrue(textArea.read().isEmpty());
    };

    runner.run(template);
  }

  @Category(SmokeTests.class)
  @Test
  public void testType() {
    TestTemplate template = textArea -> {
      textArea.type(TEST_TEXT);
      assertThat(textArea.read(), is(TEST_TEXT));
    };

    runner.run(template);
  }

  @Test
  public void testTypeLine() {
    TestTemplate template = textArea -> {
      textArea.typeLine(TEST_TEXT);
      textArea.type(TEST_TEXT);
      assertThat(textArea.read(), is(TEST_TEXT_NEW_LINE));
    };

    runner.run(template);
  }

  @Test
  public void testTypeNewLine() {
    TestTemplate template = textArea -> {
      textArea.type(TEST_TEXT);
      textArea.typeNewLine();
      textArea.type(TEST_TEXT);
      assertThat(textArea.read(), is(TEST_TEXT_NEW_LINE));
    };

    runner.run(template);
  }

  private void openPageToTest() {
    profilePage.open();
    assertTrue(profilePage.isDisplayed());
  }

  private interface TestTemplate {
    void test(AemTextArea textArea);
  }

  private class TestRunner {
    public void run(TestTemplate template) {
      AemTextArea textArea = profilePage.getHobbiesInput();
      textArea.clear();
      template.test(textArea);
    }
  }
}
