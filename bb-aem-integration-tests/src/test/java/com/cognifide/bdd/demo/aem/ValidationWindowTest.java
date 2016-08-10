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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.customersurvey.AccountNameComponent;
import com.cognifide.bdd.demo.po.customersurvey.CustomerSurveyPage;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.ui.window.ValidationWindow;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class ValidationWindowTest {

  @Inject
  private CustomerSurveyPage page;

  @Inject
  private AemLogin aemLogin;

  @Before
  public void openPage() {
    aemLogin.authorLogin();
    page.open();
    assertTrue(page.isDisplayed());
  }

  @Test
  public void dialogConfirmWithValidationWindow() {
    AccountNameComponent component =
        page.getFormParsys().insertComponent(AccountNameComponent.class);

    ValidationWindow window = component.getAemDialog().open().okExpectingValidation();
    assertThat("Header contains unexpected text", window.getHeaderText(), is("Validation Failed"));
    assertThat("Validation message does not match expected text",
        window.getValidationMessage(), is("Verify the values of the marked fields."));
    window.confirm();
  }

  @After
  public void cleanup() {
    page.getFormParsys().removeFirstComponentOfType(AccountNameComponent.class);
  }
}
