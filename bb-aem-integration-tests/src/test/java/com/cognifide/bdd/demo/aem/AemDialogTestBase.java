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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.ComponentWithDialog;
import com.cognifide.bdd.demo.po.summer.SummerBlockbusterHitsPage;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.aem.ui.parsys.AemParsys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemDialogTestBase {

  private static final Logger LOG = LoggerFactory.getLogger(AemDialogTestBase.class);

  private static final int OPENING_PAGE_TIMEOUT_IN_SECONDS = 200;

  @Inject
  protected WebDriver webDriver;

  @Inject
  protected SummerBlockbusterHitsPage summerBlockbusterHitsPage;

  @Inject
  private BobcatWait bobcatWait;

  @Inject
  private AemLogin aemLogin;

  @Before
  public void before() {
    aemLogin.authorLogin();
    openPageToTest();
    initializeFields();
  }

  protected void openPageToTest() {
    boolean pageIsOpened =
        summerBlockbusterHitsPage.openPageWithRefresh(OPENING_PAGE_TIMEOUT_IN_SECONDS);
    assertTrue("page should be opened", pageIsOpened);

    AemParsys topParsys =
        bobcatWait.withTimeout(Timeouts.BIG).until(input -> {
          AemParsys topParsys1 = null;
          try {
            topParsys1 = summerBlockbusterHitsPage.getTopParsys();
          } catch (TimeoutException te) {
            LOG.debug("timeout while waiting for top parsys. page: '{}'",
                summerBlockbusterHitsPage.getFullUrl());
          }
          return topParsys1;
        });
    topParsys.waitToBeReady();
  }

  protected void initializeFields() {
    // empty hook method
  }

  protected AemDialog findDialog(ComponentWithDialog component) {
    AemDialog aemDialog = component.getDialog();
    assertThat(aemDialog, is(notNullValue()));
    return aemDialog;
  }

  protected void openDialog(AemDialog aemDialog) {
    aemDialog.open();
    assertTrue(aemDialog.isVisible());
  }
}
