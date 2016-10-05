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

import java.time.LocalDateTime;
import java.util.UUID;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.touch.siteadmin.aem62.SiteadminPage;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.ActivationStatus;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.PageModificationInfo;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

import static org.junit.Assert.*;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class Siteadmin62Test {

  private static final String CONTEXT_PATH =
      "/content/geometrixx-outdoors-mobile/en/user/mailbox/sent-items";

  public static final String COMPOSE_MESSAGE_PATH =
      "/content/geometrixx-outdoors-mobile/en/user/mailbox/compose-message";

  public static final String DEFAULT_TEMPLATE = "Mobile Product Page";

  @Inject
  private SiteadminPage siteadminPage;

  @Inject
  private AemLogin login;

  @Inject
  private WebDriver driver;

  @Inject
  private BobcatWait wait;

  private final LocalDateTime scheduleTime =
      LocalDateTime.now().plusMonths(2).plusDays(1).plusMinutes(2);

  @Before
  public void setUp() {
    login.authorLogin();
    siteadminPage.open(CONTEXT_PATH);
  }

  @After
  public void cleanUp() {
    siteadminPage.open(CONTEXT_PATH);
    siteadminPage.deleteSubPages();
    siteadminPage.open(COMPOSE_MESSAGE_PATH);
    siteadminPage.deleteSubPages();
  }

  @Test
  public void shouldOpenSiteadminInDesiredContext() {
    assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith(CONTEXT_PATH));
  }

  @Test
  public void shouldCreateNewPage() {
    String newPage = generateRandomPageName();
    String newPageTitle = newPage.toLowerCase();
    String newPagePath = CONTEXT_PATH + "/" + newPageTitle;
    siteadminPage.createNewPage(newPage, newPageTitle, DEFAULT_TEMPLATE);
    siteadminPage.open(newPagePath);
    assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith(newPagePath));
  }

  @Test
  public void shouldDeletePage() {
    String testedPage = generateRandomPageName();
    createPageInContext(testedPage, CONTEXT_PATH);
    siteadminPage.deletePage(testedPage);
    assertFalse(siteadminPage.isPagePresent(testedPage));
  }

  @Test
  public void shouldPublishAndUnpublishPageProperly() {
    String testedPage = generateRandomPageName();
    createPageInContext(testedPage, CONTEXT_PATH);
    siteadminPage.publishPage(testedPage);
    assertThat(
        siteadminPage.getPageFromList(testedPage).getPageActivationStatus().getActivationStatus(),
        CoreMatchers.is(ActivationStatus.PUBLISHED));
    siteadminPage.unpublishPage(testedPage);
    assertThat(
        siteadminPage.getPageFromList(testedPage).getPageActivationStatus().getActivationStatus(),
        CoreMatchers.is(ActivationStatus.NOT_PUBLISHED));
  }

  @Test
  public void shouldPublishPageLater() {
    String testedPage = generateRandomPageName();
    createPageInContext(testedPage, CONTEXT_PATH);
    siteadminPage.publishPageLater(testedPage, scheduleTime);
    assertThat(
        siteadminPage.getPageFromList(testedPage).getPageActivationStatus().getActivationStatus(),
        CoreMatchers.is(ActivationStatus.SCHEDULED));
  }

  @Test
  public void shouldUnpublishPageLater() {
    String testedPage = generateRandomPageName();
    createPageInContext(testedPage, CONTEXT_PATH);
    siteadminPage.unpublishPageLater(testedPage, scheduleTime);
    assertThat(
        siteadminPage.getPageFromList(testedPage).getPageActivationStatus().getActivationStatus(),
        CoreMatchers.is(ActivationStatus.SCHEDULED));
  }

  @Test
  public void shouldMovePage() {
    String testedPage = generateRandomPageName();
    createPageInContext(testedPage, CONTEXT_PATH);
    siteadminPage.movePage(testedPage, COMPOSE_MESSAGE_PATH);
    wait.withTimeout(Timeouts.MEDIUM)
        .until(input -> siteadminPage.isPagePresent(testedPage), Timeouts.SMALL);
    siteadminPage.open(COMPOSE_MESSAGE_PATH);
    assertTrue(siteadminPage.isPagePresent(testedPage));
  }

  @Test
  public void shouldCopyPage() {
    String testedPage = generateRandomPageName();
    createPageInContext(testedPage, CONTEXT_PATH);
    siteadminPage.copyPage(testedPage, COMPOSE_MESSAGE_PATH);
    wait.withTimeout(Timeouts.MEDIUM)
        .until(input -> siteadminPage.isPagePresent(testedPage), Timeouts.SMALL);
    siteadminPage.open(COMPOSE_MESSAGE_PATH);
    assertTrue(siteadminPage.isPagePresent(testedPage));
  }

  @Test
  public void shouldGetPageModificationInfo() {
    String testedPage = generateRandomPageName();
    createPageInContext(testedPage, CONTEXT_PATH);
    PageModificationInfo pageModificationInfo =
        siteadminPage.getPageFromList(testedPage).getModificationInfo();
    assertEquals(pageModificationInfo.getModifiedBy(), "Administrator");
    assertFalse((pageModificationInfo.getWhenModified().isEmpty()));
  }

  private void createPageInContext(String title, String destinationPath) {
    siteadminPage.open(destinationPath).createNewPage(title, title.toLowerCase(), DEFAULT_TEMPLATE);
  }

  private String generateRandomPageName() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
