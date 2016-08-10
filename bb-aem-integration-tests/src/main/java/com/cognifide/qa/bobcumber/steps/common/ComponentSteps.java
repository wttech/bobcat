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
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.cognifide.bdd.demo.po.feedback.FeedbackPage;
import com.cognifide.bdd.demo.po.feedback.StartFormComponent;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemList;
import com.cognifide.qa.bb.aem.ui.menu.AemToolbar;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.aem.dialog.configurer.ComponentConfigurer;
import com.cognifide.qa.bb.aem.dialog.configurer.ComponentConfigurerFactory;
import com.cognifide.qa.bb.aem.dialog.configurer.ConfigurationEntry;
import com.cognifide.qa.bb.scenario.ScenarioContext;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemListItem;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.aem.utils.AemClassicAuthorHelper;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bobcumber.DemoComponents;
import com.google.inject.Inject;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class ComponentSteps {

  private static final String PARSYS_NAME_KEY = "parsysName";

  private static final String CONFIG_KEY = "config";

  private static final String AEM_DIALOG_KEY = "aemDialog";

  private static final String ITEMS_MULTIFIELD_LABEL = "Items";

  @Inject
  private ScenarioContext scenarioContext;

  @Inject
  private ComponentConfigurerFactory componentConfigurerFactory;

  @Inject
  private AemClassicAuthorHelper authorHelper;

  @Inject
  private FeedbackPage feedbackPage;

  @Inject
  private FrameSwitcher frameSwitcher;

  @Inject
  private BobcatWait wait;

  @Inject
  private ParsysSteps parsysSteps;

  private AemDialog aemDialog;

  @Then("^\"([^\"]*)\" component is displayed$")
  public void componentIsDisplayed(String componentName) {
    assertTrue(
        parsysSteps.getParsys()
            .isComponentPresent(DemoComponents.fromString(componentName).getComponentClass()));
  }

  @When("^I insert \"([^\"]*)\" component$")
  public void iInsertComponent(String componentName) {
    parsysSteps.getParsys().openInsertWindow().insertComponent(
        DemoComponents.fromString(componentName).getComponentClass());
  }

  @And("^I edit the \"([^\"]*)\" component$")
  public void iEditTheComponent(String componentName) {
    aemDialog = authorHelper.getDialog(scenarioContext.getString(PARSYS_NAME_KEY),
        DemoComponents.fromString(componentName));
    scenarioContext.add(AEM_DIALOG_KEY, aemDialog);
    aemDialog.open();
  }

  @And("^I edit the \"([^\"]*)\" component with index \"([^\"]*)\"$")
  public void iEditTheComponentWithIndex(String componentName, Integer x) {
    aemDialog = authorHelper.getDialog(scenarioContext.getString(PARSYS_NAME_KEY),
        DemoComponents.fromString(componentName), x);
    scenarioContext.add(AEM_DIALOG_KEY, aemDialog);
    aemDialog.open();
  }

  @And("^I edit the \"([^\"]*)\" component located on \"([^\"]*)\" parsys$")
  public void iEditTheComponentLocated(String componentName, String parsysName) {
    parsysSteps.parsysImWorkingWithIsNamed(parsysName);
    iEditTheComponent(componentName);
  }

  @And("^I set the rest of the properties using my component configuration data$")
  public void iSetTheRestOfThePropertiesUsingMyComponentConfigurationData() {
    componentConfigurerFactory.create(aemDialog)
        .configureDialog(scenarioContext.getList(CONFIG_KEY));
  }

  @And("^I configure the properties in the dialog using my component configuration data$")
  public void iConfigureThePropertiesInTheDialogUsingMyComponentConfigurationData() {
    iSetTheRestOfThePropertiesUsingMyComponentConfigurationData();
    aemDialog.ok();
  }

  @And("^I configure the \"([^\"]*)\" component index \"([^\"]*)\" using my component configuration data$")
  public void iConfigureTheComponentIndexXUsingMyComponentConfigurationData(String componentName,
      Integer x) {
    iEditTheComponentWithIndex(componentName, x);
    iConfigureThePropertiesInTheDialogUsingMyComponentConfigurationData();
  }

  @And("^I configure the \"([^\"]*)\" component using my component configuration data$")
  public void iConfigureTheComponentUsingMyComponentConfigurationData(String componentName) {
    iEditTheComponent(componentName);
    iSetTheRestOfThePropertiesUsingMyComponentConfigurationData();
    aemDialog.ok();
  }

  @When("^I delete \"([^\"]*)\" component$")
  public void iDeleteComponent(String componentName) {
    authorHelper.getParsys(scenarioContext.getString(PARSYS_NAME_KEY)).removeFirstComponentOfType(
        DemoComponents.fromString(componentName).getComponentClass());
  }

  @Then("^I check if \"([^\"]*)\" component is deleted$")
  public void iCheckIfComponentIsDeleted(String componentName) {
    authorHelper.getParsys(scenarioContext.getString(PARSYS_NAME_KEY)).isComponentPresent(
        DemoComponents.fromString(componentName).getComponentClass());
  }

  @And("^my component configuration data is:$")
  public void myComponentConfigurationDataIs(List<ConfigurationEntry> config) {
    scenarioContext.add(CONFIG_KEY, config);
  }

  @And("^I edit start form component$")
  public void iEditStartFormComponent() {
    StartFormComponent startFormComponent = feedbackPage.getStartFormComponent();
    final AemToolbar toolbar = startFormComponent.getToolbar();
    aemDialog = startFormComponent.getDialog();

    /* Selenium has a problem with clicking the toolbar on a slow PC. The function that hovers over
       to the edit button triggers some markup changes as the cursor passes over components above
       the startFormComponent and the click does not actually interact with the edit button.
       Therefore we check if the operation succeeded and retry if necessary. */
    wait.withTimeout(Timeouts.BIG).until(driver -> {
      toolbar.edit();
      return aemDialog.isVisible();
    });
  }

  @Then("^component is configured according to configuration data$")
  public void componentIsConfiguredAccordingToConfigurationData() {
    List<ConfigurationEntry> input = scenarioContext.getList(CONFIG_KEY);
    ComponentConfigurer componentConfigurer = componentConfigurerFactory.create(aemDialog);
    List<ConfigurationEntry> createdConfigurations =
        componentConfigurer.getDialogConfiguration(input);

    assertThat(createdConfigurations, contains(input.toArray()));
  }

  @And("^I removed all items from multifield$")
  public void iRemovedAllItemsFromMultifield() {
    int itemsCount = aemDialog.getFieldFromCurrentTab(ITEMS_MULTIFIELD_LABEL, AemList.class).size();
    for (int i = 0; i < itemsCount; i++) {
      aemDialog.getFieldFromCurrentTab(ITEMS_MULTIFIELD_LABEL, AemListItem.class).remove();
    }
  }

  @And("^I added (\\d+) (?:item|items) to multifield$")
  public void iAddedItemsToMultifield(int itemsCount) {
    for (int i = 0; i < itemsCount; i++) {
      aemDialog.getFieldFromCurrentTab(ITEMS_MULTIFIELD_LABEL, AemList.class).addItem();
    }
  }

  @Then("^multifield contains (\\d+) elements$")
  public void multifieldContainsElements(int itemsCount) {
    assertThat("number of elements in multifield",
        aemDialog.getFieldFromCurrentTab(ITEMS_MULTIFIELD_LABEL, AemList.class).size(),
        is(itemsCount));
  }

  @When("^I edit the Radio Group component$")
  public void iEditTheRadioGroupComponent() {
    aemDialog = authorHelper.getDialog(DemoComponents.RADIO_GROUP).open();
  }

  @Then("^Radio Group component has \"([^\"]*)\" item$")
  public void radioGroupComponentHasItem(String radioText) {
    WebElement webElement =
        authorHelper.getComponentScope(scenarioContext.getString(PARSYS_NAME_KEY),
            DemoComponents.RADIO_GROUP);
    frameSwitcher.switchTo("$cq");
    try {
      assertThat("check radio group value", webElement.getText(), is(radioText));
    } finally {
      frameSwitcher.switchBack();
    }
  }
}
