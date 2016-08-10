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
package com.cognifide.qa.bobcumber.steps.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.cognifide.bdd.demo.po.customersurvey.CustomerSurveyPage;
import com.cognifide.bdd.demo.po.feedback.FeedbackPage;
import com.cognifide.bdd.demo.po.feedback.TextFieldComponent;
import com.cognifide.bdd.demo.po.feedback.TitleComponent;
import com.cognifide.qa.bb.scenario.ScenarioContext;
import com.google.inject.Inject;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class AuthorPageSteps {

  @Inject
  private ScenarioContext scenarioContext;

  @Inject
  private FeedbackPage feedbackPage;

  @Inject
  private CustomerSurveyPage customerSurveyPage;

  @Inject
  private ParsysSteps parsysSteps;

  @Then("^I have opened Feedback page$")
  public void iHaveOpenedFeedbackPage() {
    feedbackPage.open();
    scenarioContext.add("pageTitle", feedbackPage.getPageTitle());
    assertTrue(feedbackPage.isDisplayed());
  }

  @And("^my page is located under \"([^\"]*)\"$")
  public void myPageIsLocatedUnder(String customPath) {
    scenarioContext.add("pagePath", customPath);
  }

  @Then("^Text Field Component with index \"([^\"]*)\" Title field is visible$")
  public void textFieldComponentWithIndexTitleFieldIsVisible(Integer index) {
    TextFieldComponent textFieldComponent =
        parsysSteps.getParsys().getNthComponentOfType(TextFieldComponent.class,
            index);
    assertTrue(textFieldComponent.isLabelVisible());
  }

  @Then("^Text Field Component with index \"([^\"]*)\" Title field is not visible$")
  public void textFieldComponentWithIndexTitleFieldIsNotVisible(Integer index) {
    TextFieldComponent textFieldComponent =
        parsysSteps.getParsys().getNthComponentOfType(TextFieldComponent.class,
            index);
    assertFalse(textFieldComponent.isLabelVisible());
  }

  @Then("^Title's component font size is set to \"([^\"]*)\"$")
  public void titleSComponentFontSizeIsSetToSmall(String size) {
    TitleComponent titleComponent =
        parsysSteps.getParsys().getFirstComponentOfType(TitleComponent.class);
    assertThat(titleComponent.getTitleFormattingTag(), is(size));
  }

  @Given("^I have opened Customer survey page$")
  public void iHaveOpenedCustomerSurveyPage() {
    customerSurveyPage.open();
    scenarioContext.add("pageTitle", customerSurveyPage.getPageTitle());
    assertTrue(customerSurveyPage.isDisplayed());
  }
}
