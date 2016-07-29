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

import static com.cognifide.qa.bb.aem.util.ContentHelper.JCR_CONTENT;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.util.Conditions;
import com.google.inject.Inject;

@PageObject
public class Component {

  public static final String CSS = //
          ".cq-Overlay--component:not(.cq-Overlay--container):not(.is-disabled)";

  @Inject
  private Conditions conditions;

  @CurrentScope
  @Inject
  private WebElement component;

  @Global
  @FindBy(css = ComponentToolbar.CSS)
  private ComponentToolbar componentToolbar;

  @Global
  @FindBy(css = ConfigDialog.CSS)
  private ConfigDialog configDialog;

  @Global
  @FindBy(css = DeleteDialog.CSS)
  private DeleteDialog deleteDialog;

  public String getDataPath() {
    String rawValue = conditions.staleSafe(component, checked -> checked.getAttribute(
            HtmlTags.Attributes.DATA_PATH));
    return StringUtils.substringAfter(rawValue, JCR_CONTENT);
  }

  public ComponentToolbar select() {
    verifyIsDisplayed();
    component.click();
    componentToolbar.verifyIsDisplayed();
    return componentToolbar;
  }

  public void configure(Map<String, List<FieldConfig>> config) {
    select().clickOption(ToolbarOptions.CONFIGURE);
    configDialog.verifyIsDisplayed();
    configDialog.configureWith(config);
  }

  public void delete() {
    select().clickOption(ToolbarOptions.DELETE);
    deleteDialog.confirmDelete();
    verifyIsHidden();
  }

  public void verifyIsDisplayed() {
    conditions.verifyPostAjax(visibilityOf(component));
  }

  public void verifyIsHidden() {
    conditions.verifyPostAjax(webDriver -> {
      try {
        return !component.isDisplayed();
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        return true;
      }
    });
  }
}
