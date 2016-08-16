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

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import com.cognifide.qa.bb.aem.touch.data.componentconfigs.ComponentConfiguration;
import java.util.List;

import com.cognifide.qa.bb.aem.touch.pageobjects.touchui.dialogfields.DialogField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.touch.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.touch.util.Conditions;
import com.google.inject.Inject;

/**
 * This class represents config dialog of the TouchUI component.
 */
@PageObject
public class ConfigDialog {

  public static final String CSS = "form.cq-dialog";

  public static final String FULLSCREEN_CLASS = "cq-dialog-fullscreen";

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

  public void verifyIsDisplayed() {
    conditions.verifyPostAjax(visibilityOfElementLocated(By.cssSelector(CSS)));
  }

  public void verifyIsHidden() {
    conditions.verifyPostAjax(invisibilityOfElementLocated(By.cssSelector(CSS)));
  }

  public void verifyFullscreen() {
    conditions
        .verify(webDriver -> containsIgnoreCase(dialog.getAttribute(HtmlTags.Attributes.CLASS),
            FULLSCREEN_CLASS));
  }

  public void confirm() {
    okButton.click();
    verifyIsHidden();
  }

  public void toggleFullscreen() {
    toggleFullscreenButton.click();
    verifyFullscreen();
  }

  public void configureWith(ComponentConfiguration config) {
    verifyIsDisplayed();
    configure(config);
    confirm();
  }

  public DialogField getFieldOnTab(String label, String tab, Class fieldType) {
    switchTab(tab);
    return getField(label, fieldType);
  }

  public DialogField getField(String label, Class fieldType) {
    WebElement parent;
    if (tabs.isEmpty()) {
      parent = dialog;
    } else {
      parent = activeTab;
    }
    return dialogConfigurer.getDialogField(parent, label, fieldType);
  }

  private void switchTab(String tabLabel) {
    if (!tabs.isEmpty()) {
      tabs.stream() //
          .filter(tab -> containsIgnoreCase(tab.getText(), tabLabel)) //
          .findFirst() //
          .orElseThrow(() -> new IllegalStateException("Tab not found")) //
          .click();
    }
  }

  private void configure(ComponentConfiguration config) {
    config.getTabs().stream() //
        .forEach(tab -> {
          switchTab(tab.getTabName());
          setFields(config.getConfigurationForTab(tab.getTabName()));
        });
  }

  private void setFields(List<FieldConfig> value) {
    WebElement parent;
    if (tabs.isEmpty()) {
      parent = dialog;
    } else {
      parent = activeTab;
    }
    value.stream() //
        .forEach(fieldConfig -> dialogConfigurer
          .getDialogField(parent, fieldConfig.getLabel(), fieldConfig.getType())
          .setValue(fieldConfig.getValue()));
  }
}
