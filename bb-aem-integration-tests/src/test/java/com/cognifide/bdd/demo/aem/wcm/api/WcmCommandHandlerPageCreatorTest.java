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
package com.cognifide.bdd.demo.aem.wcm.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.content.WcmCommandHandler;
import com.cognifide.qa.bb.aem.ui.wcm.SiteAdminPage;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class WcmCommandHandlerPageCreatorTest {

  private static final String CONTENT_PATH = "/content/geometrixx-media/en/entertainment";

  private static final String PAGE_TITLE = "createPageTest";

  private static final String TEMPLATE_PATH = "/apps/geometrixx-media/templates/media-article-page";

  private static final String PATH_SEPARATOR = "/";

  @Inject
  private WcmCommandHandler wcmCommandHandler;

  @Inject
  private SiteAdminPage siteAdminPage;

  @Inject
  private AemLogin login;

  @Inject
  private WebDriver webDriver;

  @Before
  public void deletePage() {
    login.authorLogin();
    siteAdminPage.open(CONTENT_PATH);
    if (siteAdminPage.isPagePresent(PAGE_TITLE)) {
      siteAdminPage.deletePage(PAGE_TITLE);
    }
  }

  @Test
  public void createPageTest() throws IOException {
    wcmCommandHandler.createPage(CONTENT_PATH, PAGE_TITLE,
        TEMPLATE_PATH);
    siteAdminRefresh();
    assertTrue(siteAdminPage.isPagePresent(PAGE_TITLE));

    wcmCommandHandler.deletePage(CONTENT_PATH + PATH_SEPARATOR + PAGE_TITLE);
    siteAdminRefresh();
    assertFalse(siteAdminPage.isPagePresent(PAGE_TITLE));
  }

  private void siteAdminRefresh() {
    webDriver.navigate().refresh();
    siteAdminPage.open(CONTENT_PATH);
  }
}
