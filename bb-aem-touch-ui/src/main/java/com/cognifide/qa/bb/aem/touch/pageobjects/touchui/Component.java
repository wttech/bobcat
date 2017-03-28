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

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.aem.touch.data.componentconfigs.ComponentConfiguration;
import com.cognifide.qa.bb.aem.touch.util.Conditions;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

import static com.cognifide.qa.bb.aem.touch.util.ContentHelper.JCR_CONTENT;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * Class representing page component.
 */
@PageObject(css = ".cq-Overlay--component:not(.cq-Overlay--container):not(.is-disabled)")
public class Component {

  @Inject
  private Conditions conditions;

  @CurrentScope
  @Inject
  private WebElement currentScope;

  @Global
  @FindPageObject
  private ComponentToolbar componentToolbar;

  @Global
  @FindPageObject
  private ConfigDialog configDialog;

  @Global
  @FindPageObject
  private DeleteDialog deleteDialog;

  /**
   * @return data path of the component.
   */
  public String getDataPath() {
    String rawValue = conditions.staleSafe(currentScope, checked -> checked.getAttribute(
        HtmlTags.Attributes.DATA_PATH));
    return StringUtils.substringAfter(rawValue, JCR_CONTENT);
  }

  /**
   * Method verifies if the component is displayed, clicks on it and verifies if component toolbar is now
   * displayed.
   *
   * @return components toolbar.
   */
  public ComponentToolbar select() {
    verifyIsDisplayed();
    currentScope.click();
    componentToolbar.verifyIsDisplayed();
    return componentToolbar;
  }

  /**
   * Method configures component using given map of fields configuration.
   *
   * @param config map of list configurations.
   */
  public void configure(ComponentConfiguration config) {
    openDialog();
    configDialog.configureWith(config);
  }

  /**
   * Method opens dialog to enable further configuration by single fields
   *
   * @return ConfigDialog for chained configuration
   */
  public ConfigDialog openDialog() {
    select().clickOption(ToolbarOptions.CONFIGURE);
    configDialog.verifyIsDisplayed();
    return configDialog;
  }

  /**
   * Method selects delete option on component toolbar, then confirms if the component is deleted and not visible.
   */
  public void delete() {
    select().clickOption(ToolbarOptions.DELETE);
    deleteDialog.confirmDelete();
    verifyIsHidden();
  }

  /**
   * Method makes ajax post call to ensure if component is displayed.
   */
  public void verifyIsDisplayed() {
    conditions.verifyPostAjax(visibilityOf(currentScope));
  }

  /**
   * Method makes ajax post call to ensure if component is hidden.
   */
  public void verifyIsHidden() {
    conditions.verifyPostAjax(webDriver -> {
      try {
        return !currentScope.isDisplayed();
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        return Boolean.TRUE;
      }
    });
  }
}
