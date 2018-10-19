/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.sitepanel;

import com.cognifide.qa.bb.aem.core.sitepanel.internal.SidePanel;
import com.cognifide.qa.bb.aem.core.sitepanel.internal.SidePanelTabs;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

@PageObject
public class EditComponentAction implements SidePanelAction<EditComponentActonData> {

  public static final String EDIT_COMPONENT = "editComponentAction";

  @Inject
  private WebDriver webDriver;

  @FindPageObject
  private SidePanel sidePanel;

  @Override
  public String getActionName() {
    return EDIT_COMPONENT;
  }

  @Override
  @Step("Edit component")
  public void action(EditComponentActonData actionData) {
    sidePanel.selectTab(SidePanelTabs.CONTENT_TREE.getCssClass());
    sidePanel.selectComponentToEdit(actionData.getComponentPath(),actionData.getComponentName(),actionData.getComponentOrder()).click();

  }
}
