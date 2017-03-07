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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.geosearch.GeoSearchPage;
import com.cognifide.bdd.demo.po.geosearch.SearchComponent;
import com.cognifide.bdd.demo.suite.SmokeTests;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.dialog.classic.field.lookup.AemLookupField;
import com.cognifide.qa.bb.aem.dialog.classic.field.lookup.AemPathWindow;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemLookupFieldTest {

  private static final String XPATH_TO_LOOKUP_DIALOG =
      "//span[@class='x-window-header-text' and text()='Select Path']";

  private static final String TEST_STRING = "test String";

  private static final int OPENING_PAGE_TIMEOUT_IN_SECONDS = 200;

  @Inject
  private GeoSearchPage geoSearchPage;

  @Inject
  private AemLogin aemLogin;

  @Inject
  private WebDriver webDriver;

  @Inject
  private FrameSwitcher switcher;

  @Inject
  private BobcatWait wait;

  private AemLookupField lookupField;

  @Before
  public void before() {
    aemLogin.authorLogin();
    openPageToTest();
    lookupField = getLookupField();
  }

  @Category(SmokeTests.class)
  @Test
  public void testType() {
    lookupField.type(TEST_STRING);
    assertEquals(TEST_STRING, lookupField.read());
  }

  @Category(SmokeTests.class)
  @Test
  public void testClear() {
    lookupField.type(TEST_STRING);
    lookupField.clear();
    assertEquals("", lookupField.read());
  }

  @Test
  public void testClickLookupField() {
    lookupField.openPathWindow();
    try {
      switcher.switchTo("$cq");
      webDriver.findElement(By.xpath(XPATH_TO_LOOKUP_DIALOG));
    } catch (NoSuchElementException e) {
      fail("Dialog not found");
    } finally {
      switcher.switchBack();
    }
  }

  @Test
  public void testSelectByPathWindow() {
    lookupField.setValue("Websites/Geometrixx Outdoors Mobile Site/English/Men's/Coats");
    assertEquals("/content/geometrixx-outdoors-mobile/en/men/coats", lookupField.read());
  }

  @Test
  public void testSelectByPathWindowFolder() {
    lookupField.setValue("Websites/Geometrixx Demo Site/English/Toolbar");
    assertEquals("/content/geometrixx/en/toolbar", lookupField.read());
  }

  @Test
  public void testSelectByPathWindowRootNode() {
    lookupField.setValue("Websites");
    assertEquals("/content", lookupField.read());
  }

  @Test
  public void testSelectByPathWindowWithTrailingSlash() {
    lookupField.setValue("Websites/");
    assertEquals("/content", lookupField.read());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSelectByPathWindowEmptyPath() {
    lookupField.setValue("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSelectByPathWindowNullPath() {
    AemPathWindow pathWindow = lookupField.openPathWindow();
    pathWindow.getContentTree().selectPath(null);
    pathWindow.clickOk();
  }

  @Test(expected = TimeoutException.class)
  public void testSelectByPathWindowBlankPath() {
    wait.withTimeout(Timeouts.MINIMAL).until(input -> {
      AemPathWindow pathWindow = lookupField.openPathWindow();
      pathWindow.getContentTree().selectPath(" ");
      return pathWindow.clickOk();
    });
  }

  private void openPageToTest() {
    boolean loaded = geoSearchPage.openPageWithRefresh(OPENING_PAGE_TIMEOUT_IN_SECONDS);
    assertTrue("page should be opened", loaded);
  }

  private AemLookupField getLookupField() {
    SearchComponent searchComponent = geoSearchPage.getSearchComponent();
    AemDialog dialog = searchComponent.getDialog();
    dialog.open();
    AemLookupField seacrchCompLookupField = searchComponent.getLookupField();
    seacrchCompLookupField.clear();
    return seacrchCompLookupField;
  }
}
