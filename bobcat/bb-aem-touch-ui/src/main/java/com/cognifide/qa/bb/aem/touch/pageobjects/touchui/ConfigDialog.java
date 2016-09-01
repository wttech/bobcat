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
package com.cognifide.qa.bb.aem.touch.pageobjects.touchui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.touch.data.componentconfigs.ComponentConfiguration;
import com.cognifide.qa.bb.aem.touch.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.dialogfields.DialogField;
import com.cognifide.qa.bb.aem.touch.util.Conditions;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * This class represents config dialog of the TouchUI component.
 */
@PageObject(css = "form.cq-dialog")
public class ConfigDialog {

  public static final String FULLSCREEN_CLASS = "cq-dialog-fullscreen";

  private static final String CSS = "form.cq-dialog";

  @Inject
  private Conditions conditions;

  @Inject
  private DialogConfigurer dialogConfigurer;

  @Inject
  @CurrentScope
  private WebElement dialog;

  @FindBy(css = "button.cq-dialog-submit")
  private WebElement okButton;

  @FindBy(css = "button.cq-dialog-layouttoggle")
  private WebElement toggleFullscreenButton;

  @FindBy(css = ".coral-TabPanel-tab")
  private List<WebElement> tabs;

  @FindBy(css = ".coral-TabPanel-pane.is-active")
  private WebElement activeTab;

  /**
   * Method used to verify if this element is visible.
   */
  public void verifyIsDisplayed() {
    conditions.verifyPostAjax(visibilityOfElementLocated(By.cssSelector(CSS)));
  }

  /**
   * Method can be used to verify if this element is hidden.
   */
  public void verifyIsHidden() {
    conditions.verifyPostAjax(invisibilityOfElementLocated(By.cssSelector(CSS)));
  }

  /**
   * Method can be used to determine if dialog is open in fullscreen.
   */
  public void verifyFullscreen() {
    conditions
        .verify(webDriver -> containsIgnoreCase(dialog.getAttribute(HtmlTags.Attributes.CLASS),
            FULLSCREEN_CLASS));
  }

  /**
   * Method used to confirm changes in the dialog
   */
  public void confirm() {
    okButton.click();
    verifyIsHidden();
  }

  /**
   * Method used to toggle dialog in fullscreen mode.
   */
  public void toggleFullscreen() {
    toggleFullscreenButton.click();
    verifyFullscreen();
  }

  /**
   * Method used to configure component with specified config in parameter
   *
   * @param config ComponentConfiguration
   */
  public void configureWith(ComponentConfiguration config) {
    verifyIsDisplayed();
    configure(config);
    confirm();
  }

  /**
   * Method used to access DialogField for further configuration.
   *
   * @param label     Label of element to access
   * @param tab       Name of a tab that element is placed on
   * @param fieldType Type of field we would like to access
   * @return DialogField with our field
   */
  public DialogField getFieldOnTab(String label, String tab, String fieldType) {
    switchTab(tab);
    return getField(label, fieldType);
  }

  /**
   * Method used to configure a field on a dialog.
   *
   * @param label     Label of field to configure
   * @param fieldType Type of field to configure
   * @param value     Value that field will be filled with
   * @return ConfigDialog this instance
   */
  public ConfigDialog setField(String label, String fieldType, Object value) {
    getField(label, fieldType).setValue(value);
    return this;
  }

  /**
   * Method can be used to access a field on currently opened tab
   *
   * @param label     Field's label to access
   * @param fieldType Type of field to access
   * @return DialogField with our field
   */
  public DialogField getField(String label, String fieldType) {
    WebElement parent = determineParentScope();
    return dialogConfigurer.getDialogField(parent, label, fieldType);
  }

  /**
   * Method used to change actual opened tab on a dialog
   *
   * @param tabLabel Label of tab to be opened
   * @return ConfigDialog this instance
   */
  public ConfigDialog switchTab(String tabLabel) {
    if (!tabs.isEmpty()) {
      tabs.stream() //
          .filter(tab -> containsIgnoreCase(tab.getText(), tabLabel)) //
          .findFirst() //
          .orElseThrow(() -> new IllegalStateException("Tab not found")) //
          .click();
    }
    return this;
  }

  private void configure(ComponentConfiguration config) {
    config.getTabs().stream() //
        .forEach(tab -> {
          switchTab(tab.getTabName());
          setFields(config.getConfigurationForTab(tab.getTabName()));
        });
  }

  private void setFields(List<FieldConfig> value) {
    WebElement parent = determineParentScope();
    value.stream() //
        .forEach(fieldConfig -> dialogConfigurer
            .getDialogField(parent, fieldConfig.getLabel(), fieldConfig.getType())
            .setValue(fieldConfig.getValue()));
  }

  private WebElement determineParentScope() {
    WebElement parent;
    if (tabs.isEmpty()) {
      parent = dialog;
    } else {
      parent = activeTab;
    }
    return parent;
  }
}
