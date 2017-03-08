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
package com.cognifide.bdd.demo.aem.wcm;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.ui.wcm.SiteAdminPage;
import com.cognifide.qa.bb.aem.ui.wcm.constants.ActivationStatus;
import com.cognifide.qa.bb.aem.ui.wcm.constants.PageStatus;
import com.cognifide.qa.bb.aem.ui.wcm.constants.SiteAdminButtons;
import com.cognifide.qa.bb.aem.ui.wcm.elements.SiteAdminGridRow;
import com.cognifide.qa.bb.aem.ui.wcm.windows.ActivateReferencesWindow;
import com.cognifide.qa.bb.aem.ui.wcm.windows.CreatePageWindow;
import com.cognifide.qa.bb.aem.ui.wcm.windows.CreateSiteWindow;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.logging.ReportEntryLogger;
import com.cognifide.qa.bb.qualifier.Retry;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class SiteAdminTest {

  private static final String CREATE_PAGE_TEMPLATE = "Media Article";

  private static final String BASE_PARENT_URL = "/content/geometrixx-media/en/entertainment";

  private static final String DESTINATION_PARENT_URL = "/content/geometrixx-media/en/events";

  private static final String DESTINATION_PATH = "Websites/Geometrixx Media/English/Events";

  private static final String CREATED_PAGE_TITLE = "What is bobcat";

  private static final String CREATED_SITE_TITLE = "What is bobcat site";

  private static final String PAGE_WITH_REFERENCE_ASSETS_TITLE =
      "Summer Blockbuster Hits and Misses";

  @Inject
  private ReportEntryLogger reportEntryLogger;

  @Inject
  private AemLogin aemLogin;

  @Inject
  private SiteAdminPage page;

  @Before
  public void openSiteadminPage() {
    aemLogin.authorLogin();
    page.open(BASE_PARENT_URL);
  }

  @Test
  public void shouldICreateNewSite() {
    List<String> languages = Arrays.asList("English (United Kingdom)", "Japanese");
    List<String> chapters = Arrays.asList("Products (products)", "Support (support)");
    List<String> rollOutConfigs = Arrays.asList("Push on modify", "Promote Launch");
    CreateSiteWindow createSiteWindow = page.openCreateSiteWindow();
    createSiteWindow.selectBlueprint(CREATED_SITE_TITLE, "", "Geometrixx Site");
    createSiteWindow.next();
    createSiteWindow.selectLanguages(languages);
    createSiteWindow.next();
    createSiteWindow.selectChapters(chapters);
    createSiteWindow.next();
    createSiteWindow.fillLiveCopy("Administrator", false, rollOutConfigs);
    createSiteWindow.next();
    createSiteWindow.createSite();
    page.getGrid().waitForLoaderNotPresent();
    assertTrue(page.isPagePresent(CREATED_SITE_TITLE));
  }

  @Test
  public void shouldISeeTemplates() {
    List<String> templateNames =
        Arrays.asList("Media Category", "Media Article", "New Forum Topic", "Media Community");
    CreatePageWindow createPageWindow = page.openCreatePageWindow();
    assertEquals(4, createPageWindow.getTemplatesNumber());
    assertEquals(templateNames, createPageWindow.getTemplatesNames());
  }

  @Test
  @Retry
  public void shouldActivateAndDeactivatePageProperly() {
    SiteAdminGridRow createdPageGridRow;
    createPage();

    page.activatePage(CREATED_PAGE_TITLE);
    createdPageGridRow = page.getGrid().selectPageByTitle(CREATED_PAGE_TITLE);
    assertThat(createdPageGridRow.getActivationStatus(), is(ActivationStatus.ACTIVATED));

    page.deactivatePage(CREATED_PAGE_TITLE);
    createdPageGridRow = page.getGrid().selectPageByTitle(CREATED_PAGE_TITLE);
    assertThat(createdPageGridRow.getActivationStatus(), is(ActivationStatus.DEACTIVATED));
  }

  @Test
  public void shouldActivatePageLater() {
    createPage();
    page.activatePageLater(CREATED_PAGE_TITLE, "21/06/16", "10:12 AM");
    SiteAdminGridRow createdPageGridRow = page.getGrid().selectPageByTitle(CREATED_PAGE_TITLE);
    assertThat(createdPageGridRow.getPageStatusToolTip(),
        containsString(PageStatus.SCHEDULED_ACTIVATION.getStatusCss()));
  }

  @Test
  public void shouldDeactivatePageLater() {
    createPage();
    page.deactivatePageLater(CREATED_PAGE_TITLE, "21/06/16", "10:12 AM");
    SiteAdminGridRow createdPageGridRow = page.getGrid().selectPageByTitle(CREATED_PAGE_TITLE);
    assertThat(createdPageGridRow.getPageStatusToolTip(),
        containsString(PageStatus.SCHEDULED_DEACTIVATION.getStatusCss()));
  }

  @Test
  public void shouldOpenActivateAssetsWindow() {
    assertTrue(page.isPagePresent(PAGE_WITH_REFERENCE_ASSETS_TITLE));
    ActivateReferencesWindow activateReferencesWindow =
        page.getGrid().getActivateReferencesWindow();
    page.getGrid().getActionBar()
        .clickOnButton(SiteAdminButtons.ACTIVATE, activateReferencesWindow);
    activateReferencesWindow.cancel();
  }

  @Test
  public void shouldOpenPageWhenItsCopiedAndPasted() {
    createPage();
    page.copyPage(CREATED_PAGE_TITLE);
    page.pastePage(DESTINATION_PARENT_URL);
    page.open(DESTINATION_PARENT_URL);
    assertTrue(page.isPagePresent(CREATED_PAGE_TITLE));
  }

  @Test
  public void shouldOpenMovedPageAtDestinationUrl() {
    createPage();
    page.movePage(CREATED_PAGE_TITLE, DESTINATION_PATH);
    page.open(DESTINATION_PARENT_URL);
    assertTrue(page.isPagePresent(CREATED_PAGE_TITLE));
  }

  @After
  public void cleanUp() {
    removePage(BASE_PARENT_URL, CREATED_PAGE_TITLE);
    removePage(DESTINATION_PARENT_URL, CREATED_PAGE_TITLE);
    removeSite(BASE_PARENT_URL, CREATED_SITE_TITLE);
  }

  private void removeSite(String parentPath, String siteTitle) {
    page.open(parentPath);
    if (page.isPagePresent(siteTitle)) {
      page.clickDeleteAndConfirm(siteTitle);
    }
  }

  private void removePage(String parentPath, String pageTitle) {
    page.open(parentPath);
    if (page.isPagePresent(pageTitle)) {
      page.deletePage(pageTitle);
    }
  }

  private void createPage() {
    if (!page.isPagePresent(CREATED_PAGE_TITLE)) {
      page.createNewPage(CREATED_PAGE_TITLE, CREATE_PAGE_TEMPLATE);
    }
    assertTrue(page.isPagePresent(CREATED_PAGE_TITLE));
    assertTrue(page.isTemplateOnTheList(CREATED_PAGE_TITLE, CREATE_PAGE_TEMPLATE));
    reportEntryLogger.info("test page created");
  }
}
