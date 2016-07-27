/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb.aem.pageobjects.touchui;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.util.Conditions;
import com.google.inject.Inject;

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
  private WebElement toggleFullscreen;

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
    toggleFullscreen.click();
    verifyFullscreen();
  }

  public void configureWith(Map<String, List<FieldConfig>> config) {
    verifyIsDisplayed();
    configure(config);
    confirm();
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

  private void configure(Map<String, List<FieldConfig>> config) {
    config.entrySet().stream() //
        .forEach(tabConfig -> {
          switchTab(tabConfig.getKey());
          setFields(tabConfig.getValue());
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
