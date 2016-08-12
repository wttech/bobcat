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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.product.TrianglePage;
import com.cognifide.bdd.demo.po.summer.ImageComponent;
import com.cognifide.bdd.demo.suite.SmokeTests;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.ui.AemContentFinder;
import com.cognifide.qa.bb.aem.ui.AemContentFinder.ResultsView;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemContentFinderTest {

  private static final String SEARCH_PHRASE_FOR_DRADDROP_TESTS = "train";

  private static final String[] TABS_NAMES = {"Documents", "Browse", "Images"};

  @Inject
  private TrianglePage page;

  @Inject
  private AemContentFinder contentFinder;

  @Inject
  private AemLogin aemLogin;

  @Before
  public void before() {
    aemLogin.authorLogin();
    openPageToTest();
  }

  @Test
  public void shouldResizeContentFinderWindowToDefinedValue500() {
    shouldResizeContentFinderWindowToDefinedValue(500, 500);
  }

  @Test
  public void shouldResizeContentFinderWindowToDefinedValue100() {
    shouldResizeContentFinderWindowToDefinedValue(100, 100);
  }

  @Test
  public void shouldResizeContentFinderWindowToDefinedValueMinus200() {
    shouldResizeContentFinderWindowToDefinedValue(-200, 0);
  }

  @Test
  public void shouldResizeContentFinderWindowToDefinedValue900() {
    shouldResizeContentFinderWindowToDefinedValue(900, 900);
  }

  @Test(expected = IllegalStateException.class)
  public void shouldThrowExceptionWhenClickingOnMosaicViewInPagesTab() throws Exception {
    // When
    contentFinder.clickTab("Pages");

    // Then
    contentFinder.clickMosaicView();
  }

  @Test(expected = IllegalStateException.class)
  public void shouldThrowExceptionWhenClickingOnListViewInPagesTab() throws Exception {
    // When
    contentFinder.clickTab("Pages");

    // Then
    contentFinder.clickListView();
  }

  @Test
  public void shouldFindExistingImage() {
    // When
    contentFinder.clickTab("Images");
    contentFinder.search("ai.jpeg");

    // Then
    assertThat(contentFinder.hasResults(), is(true));
  }

  @Test
  public void shouldOpenListViewOnImages() throws Exception {
    // When
    contentFinder.clickTab("Images");
    contentFinder.clickListView();

    // Then
    assertThat(contentFinder.getCurrentView(), is(ResultsView.LIST));
  }

  @Test
  public void shouldOpenMosaicViewOnImages() throws Exception {
    // When
    contentFinder.clickTab("Images");
    contentFinder.clickMosaicView();

    // Then
    assertThat(contentFinder.getCurrentView(), is(ResultsView.MOSAIC));
  }

  @Test
  public void shouldNotFindNonExistingImage() throws Exception {
    // When
    contentFinder.clickTab("Images");
    contentFinder.search("abcdefghij.jpeg");

    // Then
    assertThat(contentFinder.hasResults(), is(false));
  }

  @Test
  public void shouldOpenCorrectTabs() {
    contentFinder.clickTab(TABS_NAMES[0]);
    assertThat(contentFinder.getCurrentTabName(), is(TABS_NAMES[0]));
    // Resize to make element TABS_NAMES[1] clickable
    contentFinder.resize(500);
    contentFinder.clickTab(TABS_NAMES[1]);
    final String browseName = StringUtils.removeStart(contentFinder.getCurrentTabName(), "S7");
    assertThat(browseName, is(TABS_NAMES[1]));
    contentFinder.clickTab(TABS_NAMES[2]);
    assertThat(contentFinder.getCurrentTabName(), is(TABS_NAMES[2]));
  }

  @Test
  public void shouldCollapseAndExpandContentFinder() {
    contentFinder.collapse();
    assertTrue(contentFinder.isCollapsed());
    contentFinder.expand();
    assertTrue(contentFinder.isExpanded());
    // second check if collapse is working properly, because panel may be collapsed by default
    // (if yes, then collapse() method do nothing)
    contentFinder.collapse();
    assertTrue(contentFinder.isCollapsed());
  }

  @Test
  public void shouldOpenImageComponentAndInsertImageByIndexFromFinder0() {
    shouldOpenImageComponentAndInsertImageByIndexFromFinder(0);
  }

  @Test
  public void shouldOpenImageComponentAndInsertImageByIndexFromFinder1() {
    shouldOpenImageComponentAndInsertImageByIndexFromFinder(1);
  }

  @Test
  public void shouldOpenImageComponentAndInsertImageByIndexFromFinder2() {
    shouldOpenImageComponentAndInsertImageByIndexFromFinder(2);
  }

  @Category(SmokeTests.class)
  @Test
  public void shouldOpenImageComponentAndInsertImageByNameFromFinder0() {
    shouldOpenImageComponentAndInsertImageByNameFromFinder(0);
  }

  @Category(SmokeTests.class)
  @Test
  public void shouldOpenImageComponentAndInsertImageByNameFromFinder1() {
    shouldOpenImageComponentAndInsertImageByNameFromFinder(1);
  }

  @Category(SmokeTests.class)
  @Test
  public void shouldOpenImageComponentAndInsertImageByNameFromFinder2() {
    shouldOpenImageComponentAndInsertImageByNameFromFinder(2);
  }

  private void openPageToTest() {
    page.open();
    assertTrue(page.isDisplayed());
  }

  private List<String> getSearchResultsForTestPurpose() {
    contentFinder.clickTab("Images");
    contentFinder.search(SEARCH_PHRASE_FOR_DRADDROP_TESTS);
    return contentFinder.getResults();
  }

  private void shouldResizeContentFinderWindowToDefinedValue(int resizeTo, int expectedWidth) {
    // When
    contentFinder.resize(resizeTo);

    // Then
    assertThat(contentFinder.getCurrentWidth(), is(expectedWidth));
  }

  private void shouldOpenImageComponentAndInsertImageByIndexFromFinder(int elementIndex) {
    List<String> results = getSearchResultsForTestPurpose();
    ImageComponent imageComponent = page.getImageComponent();
    AemDialog dialog = imageComponent.getDialog();

    dialog.open();
    Draggable elementByIndex = contentFinder.getElementByIndex(elementIndex);
    imageComponent.insert(elementByIndex);
    assertTrue(imageComponent.getInfo().contains(results.get(elementIndex)));
    dialog.ok();
  }

  private void shouldOpenImageComponentAndInsertImageByNameFromFinder(int elementNameIndex) {
    List<String> results = getSearchResultsForTestPurpose();
    ImageComponent imageComponent = page.getImageComponent();
    AemDialog dialog = imageComponent.getDialog();

    dialog.open();
    Draggable elementByName = contentFinder.getElementByName(results.get(elementNameIndex));
    imageComponent.insert(elementByName);
    assertTrue(imageComponent.getInfo().contains(results.get(elementNameIndex)));
    dialog.ok();
  }

}
