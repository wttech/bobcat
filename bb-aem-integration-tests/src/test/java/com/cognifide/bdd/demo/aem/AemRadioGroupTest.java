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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.press.LightBoxComponent;
import com.cognifide.bdd.demo.po.press.PressPage;
import com.cognifide.bdd.demo.suite.SmokeTests;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemRadioGroup;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemRadioOption;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemRadioGroupTest {

  private final String[] values = {"on", "off"};

  private final String[] labels = {"Yes", "No"};

  @Inject
  private PressPage pressPage;

  @Inject
  private WebDriver webDriver;

  @Inject
  private FrameSwitcher switcher;

  @Inject
  private AemLogin aemLogin;

  private AemRadioGroup radioGroup;

  @Before
  public void before() {
    aemLogin.authorLogin();
    openPageToTest();
    radioGroup = getRadioGroup();
  }

  @Category(SmokeTests.class)
  @Test
  public void testSelectByLabel() {
    radioGroup.selectByLabel(labels[0]);
    AemRadioOption selected = radioGroup.getSelected();
    assertThat(selected.getLabel(), is(labels[0]));
    radioGroup.selectByLabel(labels[1]);
    selected = radioGroup.getSelected();
    assertThat(selected.getLabel(), is(labels[1]));
  }

  @Test
  public void testSelectByValue() {
    radioGroup.selectByValue(values[0]);
    AemRadioOption selected = radioGroup.getSelected();
    assertThat(selected.getValue(), is(values[0]));
    radioGroup.selectByValue(values[1]);
    selected = radioGroup.getSelected();
    assertThat(selected.getValue(), is(values[1]));
  }

  @Test
  public void testIteration() {
    int index = 0;
    for (AemRadioOption option : radioGroup) {
      assertThat(option.getValue(), is(values[index]));
      assertThat(option.getLabel(), is(labels[index]));
      index++;
    }
  }

  @Test
  public void testIsAnySelected() {
    radioGroup.selectByLabel(labels[1]);
    assertTrue(radioGroup.isAnySelected());

    if (webDriver instanceof JavascriptExecutor) {
      selectNoneByJsCall();
      assertFalse(radioGroup.isAnySelected());
    } else {
      fail("Can not test this method with JS disabled");
    }
  }

  @Test
  public void testGetSelectedReturnNull() {
    radioGroup.selectByLabel(labels[1]);
    AemRadioOption selected = radioGroup.getSelected();
    assertThat(selected, is(notNullValue()));

    if (webDriver instanceof JavascriptExecutor) {
      selectNoneByJsCall();
      assertThat(radioGroup.getSelected(), is(nullValue()));
    } else {
      fail("Can not test this method with JS disabled");
    }

  }

  @Test
  public void testGetSelectedValue() {
    radioGroup.selectByLabel(labels[1]);
    assertThat(radioGroup.getSelectedValue(), is(values[1]));
  }

  private void openPageToTest() {
    pressPage.open();
    assertTrue(pressPage.isDisplayed());
  }

  private void selectNoneByJsCall() {
    switcher.switchTo("$cq");
    ((JavascriptExecutor) webDriver)
        .executeScript("$('input[checked][name=\"./allowThumbnails\"]').removeAttr('checked')");
    switcher.switchBack();
  }

  private AemRadioGroup getRadioGroup() {
    LightBoxComponent lightboxComponent = pressPage.getLightboxComponent();
    AemDialog dialog = lightboxComponent.getDialog();
    dialog.open();
    dialog.clickTab("Download");
    return lightboxComponent.getRadioGroup();
  }
}
