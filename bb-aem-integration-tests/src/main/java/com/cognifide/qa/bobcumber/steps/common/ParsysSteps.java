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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.scenario.ScenarioContext;
import com.cognifide.qa.bb.aem.ui.parsys.AemParsys;
import com.cognifide.qa.bb.aem.utils.AemClassicAuthorHelper;
import com.google.inject.Inject;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class ParsysSteps {

  private static final String PARSYS_NAME_KEY = "parsysName";

  @Inject
  private ScenarioContext scenarioContext;

  @Inject
  private WebDriver webDriver;

  @Inject
  private AemClassicAuthorHelper authorHelper;

  @When("^there is \"([^\"]*)\" aemParsys available$")
  public void thereIsParsysAvailable(String parsysName) {
    List<WebElement> paragraphSystems = webDriver
        .findElements(By.cssSelector("div.paragraphSystem." + parsysName));
    assertFalse(paragraphSystems.isEmpty());
  }

  @And("^parsys I'm working with is named \"([^\"]*)\"$")
  public void parsysImWorkingWithIsNamed(String parsysName) {
    scenarioContext.add(PARSYS_NAME_KEY, parsysName);
  }

  @Then("^in my parsys there are no components$")
  public void inMyParsysThereAreNoComponents() {
    assertThat(getParsys().componentsCount(),
        is(0));
  }

  public AemParsys getParsys() {
    return authorHelper.getParsys(scenarioContext.getString(PARSYS_NAME_KEY));
  }
}
