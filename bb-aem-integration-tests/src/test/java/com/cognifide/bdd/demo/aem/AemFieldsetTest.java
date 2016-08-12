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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.feedback.FeedbackPage;
import com.cognifide.bdd.demo.po.feedback.StartFormComponent;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemFieldset;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemTextField;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.aem.ui.menu.AemToolbar;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.utils.WebElementUtils;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemFieldsetTest {

  @Inject
  private FeedbackPage feedBackPage;

  @Inject
  private AemLogin aemLogin;

  private AemFieldset fieldset;

  @Inject
  private WebElementUtils webElementUtils;

  @Before
  public void before() {
    aemLogin.authorLogin();
    openPageToTest();
    fieldset = getFieldset();
  }

  @Test
  public void testFoldAndExpand() {
    fieldset.fold();
    assertTrue(fieldset.isFolded());
    fieldset.expand();
    assertTrue(fieldset.isExpanded());
  }

  @Test
  public void testGetField() {
    AemTextField field = fieldset.getField("Content Path", AemTextField.class);
    field.clear().type("/tmp/formtest/*");
    assertThat(field.read(), is("/tmp/formtest/*"));
  }

  @Test
  public void testGetNotExistingField() {
    boolean fieldExists = webElementUtils.isConditionMet(driver ->
        fieldset.getField("Not exists label", AemTextField.class), Timeouts.MINIMAL
    );
    assertFalse("Field exists", fieldExists);
  }

  private void openPageToTest() {
    feedBackPage.open();
    assertTrue(feedBackPage.isDisplayed());
  }

  private AemFieldset getFieldset() {
    StartFormComponent startFormComponent = feedBackPage.getStartFormComponent();
    AemDialog dialog = startFormComponent.getDialog();
    AemToolbar toolbar = startFormComponent.getToolbar();
    toolbar.edit();
    dialog.clickTab("Advanced");
    AemFieldset startFormFieldset = startFormComponent.getFieldset();
    startFormFieldset.expand();
    return startFormFieldset;
  }
}
