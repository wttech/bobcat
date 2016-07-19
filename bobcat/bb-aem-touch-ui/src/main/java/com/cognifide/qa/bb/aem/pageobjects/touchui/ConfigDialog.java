package com.cognifide.qa.bb.aem.pageobjects.touchui;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
    conditions.verify(webDriver -> containsIgnoreCase(dialog.getAttribute("class"),
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
    tabs.stream() //
        .filter(tab -> containsIgnoreCase(tab.getText(), tabLabel)) //
        .findFirst() //
        .orElseThrow(() -> new IllegalStateException("Tab not found")) //
        .click();
  }

  private void configure(Map<String, List<FieldConfig>> config) {
    config.entrySet().stream() //
        .forEach(tabConfig -> {
          switchTab(tabConfig.getKey());
          setFields(tabConfig.getValue());
        });
  }

  private void setFields(List<FieldConfig> value) {
    value.stream() //
        .forEach(fieldConfig -> dialogConfigurer
            .getDialogField(activeTab, fieldConfig.getLabel(), fieldConfig.getType())
            .setValue(fieldConfig.getValue()));
  }
}
