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
package com.cognifide.qa.bobcumber.steps;

import static org.junit.Assert.assertTrue;

import com.cognifide.bdd.demo.po.login.LoginPage;
import com.cognifide.bdd.demo.po.login.ProjectsScreen;
import com.cognifide.qa.bb.aem.AemLogin;
import com.google.inject.Inject;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class LoginPageSteps {

  @Inject
  private LoginPage loginPage;

  @Inject
  private ProjectsScreen projectsScreen;

  @Inject
  private AemLogin aemLogin;

  @Given("^I have opened login page$")
  public void iHaveOpenedLoginPage() {
    assertTrue(loginPage.openLoginPage().loginPageIsDisplayed());
  }

  @When("^I enter following credentials \"(.+)\", \"(.+)\"$")
  public void iEnterFollowingCredentials(String login, String password) {
    loginPage.getLoginBox().enterLogin(login).enterPassword(password);
  }

  @When("^I press login button$")
  public void iPressLoginButton() {
    loginPage.getLoginBox().clickSignIn();
  }

  @Then("^Authorization error message should appear$")
  public void errorMessageShouldAppear() {
    assertTrue(loginPage.getLoginBox().isErrorMessageVisible());
  }

  @Given("^I am logged in$")
  public void iAmLoggedIn() {
    loginPage.openLoginPage();
    loginPage.loginAsAuthor();
    assertTrue(projectsScreen.projectScreenIsDisplayed());
  }

  @Given("^I am logged into AEM as admin$")
  public void iAmLoggedIntoAemAsAdmin() {
    aemLogin.authorLogin();
  }

}
