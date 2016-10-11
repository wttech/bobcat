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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.touch.siteadmin.aem61.SiteadminPage;
import com.cognifide.qa.bb.aem.touch.siteadmin.aem61.list.ChildPageRow;
import com.cognifide.qa.bb.aem.touch.siteadmin.common.ActivationStatus;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class SiteadminTest {

  private static final String CONTEXT_PATH
          = "/content/geometrixx-outdoors-mobile/en/user/mailbox/sent-items";

  private static final String COMPOSE_MESSAGE_PATH
          = "/content/geometrixx-outdoors-mobile/en/user/mailbox/compose-message";

  @Inject
  private SiteadminPage siteadminPage;

  @Inject
  private AemLogin login;

  @Inject
  private WebDriver driver;

  @Inject
  private BobcatWait wait;

  private final LocalDateTime scheduleTime
          = LocalDateTime.now().plusMonths(2).plusDays(1).plusMinutes(2);

  private String testedPageName;

  @Before
  public void setUp() {
    login.authorLogin();
    siteadminPage.open(CONTEXT_PATH);
    testedPageName = UUID.randomUUID().toString().replace("-", "");
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
    assertThat(driver.getCurrentUrl(), endsWith(CONTEXT_PATH));
  }

  @Test
  public void shouldPublishPageProperly() {
    createPage(testedPageName);
    siteadminPage.publishPage(testedPageName);
    siteadminPage.refresh();
    assertThat(getPageFromList(testedPageName).getPageActivationStatus().getActivationStatus(),
        is(ActivationStatus.PUBLISHED));
  }

  @Test
  public void shouldPublishAndUnpublishPageProperly() {
    createPage(testedPageName);
    siteadminPage.publishPage(testedPageName);
    siteadminPage.refresh();
    siteadminPage.unpublishPage(testedPageName);
    siteadminPage.refresh();
    assertThat(getPageFromList(testedPageName).getPageActivationStatus().getActivationStatus(),
        is(ActivationStatus.NOT_PUBLISHED));
  }

  @Test
  public void shouldPublishPageLaterProperly() {
    createPage(testedPageName);
    siteadminPage.publishPageLater(testedPageName, scheduleTime);
    siteadminPage.open(CONTEXT_PATH);
    assertThat(getPageFromList(testedPageName).getPageActivationStatus().getActivationStatus(),
        is(ActivationStatus.SCHEDULED));
  }

  @Test
  public void shouldPublishAndUnpublishPageLaterProperly() {
    createPage(testedPageName);
    siteadminPage.publishPage(testedPageName);
    siteadminPage.unpublishPageLater(testedPageName, scheduleTime);
    siteadminPage.open(CONTEXT_PATH);
    assertThat(getPageFromList(testedPageName).getPageActivationStatus().getActivationStatus(),
        is(ActivationStatus.SCHEDULED));
  }

  @Test
  public void shouldCopyPageProperly() {
    createPage(testedPageName);
    siteadminPage.copyPage(testedPageName, COMPOSE_MESSAGE_PATH);
    siteadminPage.open(COMPOSE_MESSAGE_PATH);
    assertThat(getPageFromList(testedPageName), isA(ChildPageRow.class));
  }

  @Test
  public void shouldMovePageProperly() {
    createPage(testedPageName);
    siteadminPage.movePage(testedPageName, COMPOSE_MESSAGE_PATH);
    wait.withTimeout(Timeouts.SMALL).until(input -> siteadminPage.isLoaded());
    siteadminPage.open(COMPOSE_MESSAGE_PATH);
    assertThat(getPageFromList(testedPageName), isA(ChildPageRow.class));
  }

  private void createPage(String testedPage) {
    siteadminPage.open(CONTEXT_PATH)
        .createNewPage(testedPage, testedPage.toLowerCase(), "Mobile Product Page");
    driver.navigate().refresh();
  }

  private ChildPageRow getPageFromList(String pageTitle) {
    return siteadminPage.getPageFromList(pageTitle);
  }

}
