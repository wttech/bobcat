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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.cognifide.qa.bb.aem.dialog.configurer.ComponentConfig;
import com.cognifide.qa.bb.aem.dialog.configurer.ComponentConfigBuilder;
import com.cognifide.qa.bb.aem.dialog.configurer.ConfigurationEntry;
import com.cognifide.qa.bb.aem.dialog.configurer.JsonToComponentConfig;
import com.cognifide.qa.bb.scenario.ScenarioContext;
import com.google.inject.Inject;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class BobcatSteps {

  @Inject
  private ScenarioContext scenarioContext;

  @When("^I build a \"([^\"]*)\" configuration with ComponentConfigBuilder$")
  public void iBuildAConfigurationWithComponentConfigBuilder(String configName) {
    List<ConfigurationEntry> config = null;

    if ("default".equals(configName)) {
      config = new ComponentConfigBuilder()
          .add("text area", "Text Area", "textarea")
          .setTab("Tab1")
          .add("text field", "Text Field", "textfield")
          .add("path field", "Path Field", "Node1/Node2/Node3")
          .setTab("Tab2")
          .add("dropdown", "Dropdown", "dropdown")
          .setTab("Tab3")
          .add("checkbox", "Checkbox", "TRUE")
          .setTab("Tab4")
          .add("rich text", "Rich Text", "richtext")
          .add("tags", "Tags", "Namespace:Tag1/Tag2")
          .add("radio group", "Radio Group", "radiogroup")
          .addItemInMultifield("Multifield", 1, "radio group", "Radio Group", "radiogroup")
          .addItemInFieldset("Fieldset", "radio group", "Radio Group", "radiogroup")
          .build();
    }

    scenarioContext.add(configName, config);
  }

  @Then("^\"([^\"]*)\" config matches the Data Table$")
  public void configMatchesTheDataTable(String configName) {
    List<ConfigurationEntry> expected = scenarioContext.getList("config");
    List<ConfigurationEntry> actual = scenarioContext.getList(configName);

    assertThat("Lists have different size", expected.size(), is(actual.size()));
    assertTrue("List built with ComponentConfigBuilder does not contain all elements", actual
        .containsAll(expected));
  }

  @When("^I use the \"([^\"]*)\" configuration from JSON$")
  public void iUseTheConfigurationFromJSON(String configName) {
    ComponentConfig config = new JsonToComponentConfig().readConfig(configName);
    scenarioContext.add(configName, config.getConfig());
  }
}
