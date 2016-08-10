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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.bdd.demo.po.summer.ImageComponent;
import com.cognifide.bdd.demo.po.summer.SummerBlockbusterHitsPage;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.aem.ui.sidekick.AemSidekick;
import com.cognifide.qa.bb.aem.ui.sidekick.PageOperation;
import com.cognifide.qa.bb.aem.ui.sidekick.SidekickSection;
import com.cognifide.qa.bb.aem.ui.sidekick.SidekickTab;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.dragdrop.Droppable;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemSidekickTest {

  @Inject
  private AemLogin aemLogin;

  @Inject
  private SummerBlockbusterHitsPage page;

  @Inject
  private AemSidekick sidekick;

  @Inject
  private AemDialog dialog;

  @Inject
  private WebDriver webDriver;

  @Inject
  private WebElementUtils webElementUtils;

  @Before
  public void openPage() {
    aemLogin.authorLogin();
    openPageToTest();

    // added for tests stability
    boolean isComponentGroupPresent =
        webElementUtils.isConditionMet(input -> sidekick.isComponentGroupPresent("General"));
    assertTrue("'General' component group should be present", isComponentGroupPresent);
  }

  @Test
  public void testClickTab() {
    sidekick.clickTab(SidekickTab.PAGE);
    webDriver.findElement(By.cssSelector(".x-tab-strip-active .cq-sidekick-tab-icon-page"));
  }

  @Test
  public void testClickOperation() {
    sidekick.clickTab(SidekickTab.PAGE);
    sidekick.clickOperation(PageOperation.PAGE_PROPERTIES);
    assertTrue(dialog.isVisible());
  }

  @Test
  public void testMinMaxSidekick() {
    sidekick.toggle();
    webDriver.findElement(By.cssSelector("#cq-sk.x-panel-collapsed"));

    sidekick.toggle();
    final String classes =
        webDriver.findElement(By.cssSelector("#cq-sk")).getAttribute(HtmlTags.Attributes.CLASS);
    assertFalse(classes.contains(".x-panel-collapsed"));
  }

  @Test
  public void testGroupNames() {
    final List<String> groupNames = sidekick.getComponentGroupNames();
    assertThat(groupNames,
        is(Arrays.asList("General", "Communities", "Geometrixx Media")));
    assertTrue(sidekick.isComponentGroupPresent("General"));
    assertFalse(sidekick.isComponentGroupPresent("Xyz"));
  }

  @Test
  public void testClickComponentGroup() {
    sidekick.clickComponentGroupToggle("General");
    assertFalse(
        webDriver.findElement(By.cssSelector(".cq-cmpts-General .x-panel-bwrap")).isDisplayed());
  }

  @Test
  public void testPageOperation() {
    for (PageOperation operation : PageOperation.values()) {
      if (!(operation == PageOperation.PROMOTE_LAUNCH || operation == PageOperation.LESS)) {
        sidekick.clickTab(operation.getTab());
        if (!operation.getSection().getSectionName().isEmpty()) {
          sidekick.expandSectionIfCollapsed(operation.getSection());
        }
        if (operation.getOptionName().contains("#")) {
          sidekick.expandFieldsetIfCollapsed(operation.getOptionName().split("#")[1]);
        }
        assertTrue("operation " + operation.getOptionName() + " is not displayed",
            sidekick.getOperation(operation).isDisplayed());
      }
    }
  }

  @Test
  public void testGridByIndex() {
    sidekick.clickTab(SidekickTab.VERSIONING);
    sidekick.expandSectionIfCollapsed(SidekickSection.LAUNCHES);
    assertTrue("Checkbox is not selected",
        sidekick.getGrid(SidekickSection.LAUNCHES).getGridRow(0).selectGridCheckbox()
            .isGridRowCheckboxSelected());
  }

  @Test
  public void testGridByValue() {
    sidekick.clickTab(SidekickTab.VERSIONING);
    sidekick.expandSectionIfCollapsed(SidekickSection.LAUNCHES);
    assertTrue("Checkbox is not selected",
        sidekick.getGrid(SidekickSection.LAUNCHES).getGridRow("Production").selectGridCheckbox()
            .isGridRowCheckboxSelected());
  }

  @Test
  public void testDragToParsys() {
    Draggable draggable = sidekick.getDraggable(ImageComponent.class);
    Droppable droppable = page.getTopParsys().getDroppable();
    draggable.dropTo(droppable);
    webElementUtils
        .isConditionMet(input -> page.getTopParsys().isComponentPresent(ImageComponent.class));
    page.getTopParsys().removeFirstComponentOfType(ImageComponent.class);
  }

  @Test
  public void isComponentPresent() {
    assertTrue(sidekick.isComponentPresent("Title", "General"));
    assertTrue(sidekick.isComponentPresent("Article", "Geometrixx Media"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void isComponentPresent_groupNotExists() {
    assertTrue(sidekick.isComponentPresent("Comments", "Incorrect group"));
  }

  @Test
  public void isComponentPresent_componentNotExists() {
    assertFalse(sidekick.isComponentPresent("Incorrect component", "General"));
  }

  private void openPageToTest() {
    page.open();
    assertTrue(page.isDisplayed());
  }
}
