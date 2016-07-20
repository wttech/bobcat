/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.bdd.demo.aem.touchui;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.data.pages.Pages;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemRichtextTest {

  private static final String PAGE_TITLE = "Title - Update&Read";

  @Inject
  private AemLogin aemLogin;

  @Inject
  private Pages pages;

  @Inject
  private AuthorPageFactory authorPageFactory;

  private AuthorPage feedbackPage;

  private String parsys;

  @Before
  public void before() {
    aemLogin.authorLogin();
    feedbackPage = authorPageFactory.create(pages.getPath(PAGE_TITLE));
    parsys = pages.getParsys("Title - Update&Read");
    feedbackPage.open();
    assertThat("Page has not loaded", feedbackPage.isLoaded(), is(true));
  }

  @Test
  public void test() {
    feedbackPage.configureComponent(parsys, "Text", "valid Text");
  }

  @After
  public void cleanUp() {
    feedbackPage.deleteComponent(parsys, "Text");
  }
}
